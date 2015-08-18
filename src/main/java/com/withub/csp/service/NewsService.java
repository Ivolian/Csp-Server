package com.withub.csp.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.NewsData;
import com.withub.csp.entity.News;
import com.withub.csp.entity.NewsRead;
import com.withub.csp.entity.User;
import com.withub.csp.repository.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Transactional
public class NewsService {

    @Value("${exploded.path}")
    private String explodedPath;

    @Value("${temp.path}")
    private String tempPath;

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private NewsDataDao newsDataDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ThumbDao thumbDao;

    @Autowired
    private NewsReadDao newsReadDao;

    @Autowired
    private UserDao userDao;

    //

    public News getNews(String id) {

        return newsDao.findOne(id);
    }

    public Page<News> getNews(Map<String, Object> searchParams, int pageNo, int pageSize, String menuId, String keyword) {

        if (menuId != null) {
            searchParams.put("EQ_menu.id", menuId);
        }
        if (keyword != null) {
            searchParams.put("_LIKE_title", keyword);
        }
        return getNews(searchParams, pageNo, pageSize);
    }

    public Page<News> getNews(Map<String, Object> searchParams, int pageNo, int pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNo, pageSize);
        Specification<News> spec = buildSpecification(searchParams);
        return newsDao.findAll(spec, pageRequest);
    }

    private PageRequest buildPageRequest(int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "postTime");
        return new PageRequest(pageNo - 1, pageSize, sort);
    }

    private Specification<News> buildSpecification(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), News.class);
    }

    public void saveNews(News entity) {

        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);
            NewsData newsData = entity.getNewsData();
            newsData.setId(Identities.uuid());
        }

        // save picture
        if (entity.getPictureAttachment() != null && StringUtils.isNotEmpty(entity.getPictureAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                entity.setPicture(distPath + "/" + entity.getPictureAttachment().getTempFileName() + "." + FilenameUtils.getExtension(entity.getPictureAttachment().getFileName()));
                entity.setPictureFilename(entity.getPictureAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + entity.getPictureAttachment().getTempFileName()), new File(explodedPath + entity.getPicture()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // save newsData
        NewsData newsData = entity.getNewsData();
        newsData.setNews(entity);
        entity.setNewsData(null);
        newsDao.save(entity);
        newsDataDao.save(newsData);
    }

    public void deleteNews(String id) {

        News news = getNews(id);
        news.setDeleteFlag(1);
        newsDao.save(news);
    }

    public JSONObject listForMobile(int pageNo, int pageSize, String menuId, String keyword) {

        Map<String, Object> searchParams = new HashMap<String, Object>();
        Page<News> newsPage = getNews(searchParams, pageNo, pageSize, menuId, keyword);
        List<News> newsList = newsPage.getContent();
        JSONArray jsonArray = new JSONArray();
        for (News news : newsList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", news.getId());
            jsonObject.put("title", news.getTitle());
            jsonObject.put("picture", news.getPicture());
            jsonObject.put("postTime", news.getPostTime());
            jsonObject.put("commentCount", commentDao.getCommentCountOfNews(news.getId()));
            jsonObject.put("thumbCount", thumbDao.getThumbCountOfNews(news.getId()));
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", newsPage.isLastPage());
        response.put("totalPages", newsPage.getTotalPages());
        return response;
    }

    public void addNewsRead(String userId, String newsId) {

        NewsRead newsRead = newsReadDao.findOneByUserIdAndNewsId(userId, newsId);
        if (newsRead != null) {
            return;
        }

        newsRead = new NewsRead();
        newsRead.setId(Identities.uuid());
        User user = userDao.findOne(userId);
        newsRead.setUser(user);
        News news = newsDao.findOne(newsId);
        newsRead.setNews(news);
        newsRead.setEventTime(new Date());
        newsReadDao.save(newsRead);
    }

}
