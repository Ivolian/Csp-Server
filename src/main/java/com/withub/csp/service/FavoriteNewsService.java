package com.withub.csp.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.FavoriteNews;
import com.withub.csp.entity.News;
import com.withub.csp.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.util.*;


@Component
@Transactional
public class FavoriteNewsService {

    @Autowired
    private FavoriteNewsDao favoriteNewsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ThumbDao thumbDao;

    //

    public boolean saveFavoriteNews(String userId, String newsId) {

        FavoriteNews favoriteNews = favoriteNewsDao.findOneByUserIdAndNewsIdAndDeleteFlag(userId, newsId, 0);
        if (favoriteNews != null) {
            return false;
        }

        favoriteNews = new FavoriteNews();
        favoriteNews.setUser(userDao.findOne(userId));
        favoriteNews.setNews(newsDao.findOne(newsId));
        if (StringUtils.isEmpty(favoriteNews.getId())) {
            favoriteNews.setId(Identities.uuid());
            favoriteNews.setEventTime(new Date());
            favoriteNews.setDeleteFlag(0);
        }
        favoriteNewsDao.save(favoriteNews);
        return true;
    }

    public void deleteFavoriteNews(String userId, String newsId) {

        FavoriteNews favoriteNews = favoriteNewsDao.findOneByUserIdAndNewsIdAndDeleteFlag(userId, newsId, 0);
        favoriteNews.setDeleteFlag(1);
        favoriteNewsDao.save(favoriteNews);
    }

    public Page<FavoriteNews> getFavoriteNews(Map<String, Object> searchParams, int pageNo, int pageSize, String userId) {

        searchParams.put("EQ_user.id", userId);
        Sort sort = new Sort(Sort.Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<FavoriteNews> spec = buildSpecificationComment(searchParams);
        return favoriteNewsDao.findAll(spec, pageRequest);
    }

    private Specification<FavoriteNews> buildSpecificationComment(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), FavoriteNews.class);
    }

    public JSONObject listForMobile(Integer pageNo, Integer pageSize, String userId) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<FavoriteNews> favoritePage = getFavoriteNews(searchParams, pageNo, pageSize, userId);
        List<FavoriteNews> favoriteNewsList = favoritePage.getContent();
        List<News> newsList = new ArrayList<>();
        for (FavoriteNews favoriteNews : favoriteNewsList) {
            newsList.add(favoriteNews.getNews());
        }

        JSONArray jsonArray = new JSONArray();
        for (News news : newsList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", news.getId());
            jsonObject.put("title", news.getTitle());
            jsonObject.put("picture", news.getPicture());
            jsonObject.put("postTime", news.getPostTime());
            jsonObject.put("commentCount", news.getCommentList().size());
            jsonObject.put("thumbCount", thumbDao.getThumbCountOfNews(news.getId()));
            jsonArray.add(jsonObject);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", jsonArray);
        jsonObject.put("lastPage", favoritePage.isLastPage());
        jsonObject.put("totalPages", favoritePage.getTotalPages());
        return jsonObject;
    }

}
