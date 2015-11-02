package com.withub.csp.service;

import com.withub.common.DynamicSpecifications;
import com.withub.common.SearchFilter;
import com.withub.csp.entity.News;
import com.withub.csp.entity.NewsData;
import com.withub.csp.entity.NewsRead;
import com.withub.csp.entity.User;
import com.withub.csp.repository.NewsDao;
import com.withub.csp.repository.NewsDataDao;
import com.withub.csp.repository.NewsReadDao;
import com.withub.csp.repository.UserDao;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Identities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Service
@Transactional
public class NewsService extends BaseService {

    @Value("${exploded.path}")
    private String explodedPath;

    @Value("${temp.path}")
    private String tempPath;

    //

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private NewsDataDao newsDataDao;

    @Autowired
    private NewsReadDao newsReadDao;

    @Autowired
    private UserDao userDao;

    //

    public Page<News> getNews(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "postTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<News> spec = buildSpecification(searchParams);
        return newsDao.findAll(spec, pageRequest);
    }

    public void saveNews(News news) {

        if (StringUtils.isEmpty(news.getId())) {
            initEntity(news);
            NewsData newsData = news.getNewsData();
            newsData.setId(Identities.uuid());
        }

        // save picture
        if (news.getPictureAttachment() != null && StringUtils.isNotEmpty(news.getPictureAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                news.setPicture(distPath + "/" + news.getPictureAttachment().getTempFileName() + "." + FilenameUtils.getExtension(news.getPictureAttachment().getFileName()));
                news.setPictureFilename(news.getPictureAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + news.getPictureAttachment().getTempFileName()), new File(explodedPath + news.getPicture()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // save newsData
        NewsData newsData = news.getNewsData();
        newsData.setNews(news);
        news.setNewsData(null);
        newsDao.save(news);
        newsDataDao.save(newsData);
    }


    // 添加用户阅读新闻的记录
    public void addNewsRead(String userId, String newsId) {

        NewsRead newsRead = newsReadDao.findOneByUserIdAndNewsId(userId, newsId);
        // 若记录已存在
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


    // 基本无视的方法

    public News getNews(String id) {
        return newsDao.findOne(id);
    }

    public void deleteNews(String id) {

        News news = getNews(id);
        news.setDeleteFlag(1);
        newsDao.save(news);
    }

    private Specification<News> buildSpecification(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), News.class);
    }

}
