package com.withub.service.content;

import com.withub.csp.entity.FavoriteNews;
import com.withub.csp.repository.NewsDao;
import com.withub.csp.repository.UserDao;
import com.withub.repository.FavoriteDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.util.Map;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NewsDao newsDao;

    public boolean saveFavorite(String userId, String newsId) {

        FavoriteNews favoriteNews = favoriteDao.findOneByUserIdAndNewsIdAndDeleteFlag(userId, newsId, 0);
        if (favoriteNews != null) {
            return false;
        }

        favoriteNews = new FavoriteNews();
        favoriteNews.setUser(userDao.findOne(userId));
        favoriteNews.setNews(newsDao.findOne(newsId));
        if (StringUtils.isEmpty(favoriteNews.getId())) {
            favoriteNews.setId(Identities.uuid());
            favoriteNews.setDeleteFlag(0);
        }
        favoriteDao.save(favoriteNews);
        return true;
    }

    public void deleteFavorite(String userId, String newsId) {

        FavoriteNews favoriteNews = favoriteDao.findOneByUserIdAndNewsIdAndDeleteFlag(userId, newsId, 0);
        favoriteNews.setDeleteFlag(1);
        favoriteDao.save(favoriteNews);
    }

    public Page<FavoriteNews> getFavorite(Map<String, Object> searchParams, int pageNo, int pageSize, String userId) {

        //
        searchParams.put("EQ_user.id", userId);
        // TODO sort
//        Sort sort = new Sort(Direction.DESC, "user.id");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
        Specification<FavoriteNews> spec = buildSpecificationComment(searchParams);
        return favoriteDao.findAll(spec, pageRequest);
    }

    private Specification<FavoriteNews> buildSpecificationComment(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FavoriteNews> spec = DynamicSpecifications.bySearchFilter(filters.values(), FavoriteNews.class);
        return spec;
    }

}
