package com.withub.service.content;

import com.withub.entity.Content;
import com.withub.entity.CspUser;
import com.withub.entity.Favorite;
import com.withub.repository.ContentDao;
import com.withub.repository.CspUserDao;
import com.withub.repository.FavoriteDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Map;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;

    @Autowired
    private CspUserDao userDao;

    @Autowired
    private ContentDao newsDao;

    public boolean saveFavorite(String userId, String newsId) {

        Favorite favorite = favoriteDao.findOneByUserIdAndNewsIdAndDeleteFlag(userId, newsId, 0);
        if (favorite != null) {
            return false;
        }

        favorite = new Favorite();
        favorite.setUser(userDao.findOne(userId));
        favorite.setNews(newsDao.findOne(newsId));
        if (StringUtils.isEmpty(favorite.getId())) {
            favorite.setId(Identities.uuid());
            favorite.setDeleteFlag(0);
        }
        favoriteDao.save(favorite);
        return true;
    }

    public void deleteFavorite(String userId, String newsId) {

        Favorite favorite = favoriteDao.findOneByUserIdAndNewsIdAndDeleteFlag(userId, newsId, 0);
        favorite.setDeleteFlag(1);
        favoriteDao.save(favorite);
    }

    public Page<Favorite> getFavorite(Map<String, Object> searchParams, int pageNo, int pageSize, String userId) {

        //
        searchParams.put("EQ_user.id", userId);
        // TODO sort
//        Sort sort = new Sort(Direction.DESC, "user.id");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
        Specification<Favorite> spec = buildSpecificationComment(searchParams);
        return favoriteDao.findAll(spec, pageRequest);
    }

    private Specification<Favorite> buildSpecificationComment(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Favorite> spec = DynamicSpecifications.bySearchFilter(filters.values(), Favorite.class);
        return spec;
    }

}
