package com.withub.service.content;

import com.withub.csp.entity.FavoriteBook;
import com.withub.repository.CspUserDao;
import com.withub.repository.FavoriteBookDao;
import com.withub.repository.PositionDao;
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
public class FavoriteBookService {

    @Autowired
    private FavoriteBookDao favoriteBookDao;

    @Autowired
    private CspUserDao userDao;

    @Autowired
    private PositionDao bookDao;

    public boolean saveFavoriteBook(String userId, String bookId) {

        FavoriteBook favoriteBook = favoriteBookDao.findOneByUserIdAndBookIdAndDeleteFlag(userId, bookId, 0);
        if (favoriteBook != null) {
            return false;
        }

        favoriteBook = new FavoriteBook();
        favoriteBook.setUser(userDao.findOne(userId));
        favoriteBook.setBook(bookDao.findOne(bookId));
        if (StringUtils.isEmpty(favoriteBook.getId())) {
            favoriteBook.setId(Identities.uuid());
            favoriteBook.setDeleteFlag(0);
        }
        favoriteBookDao.save(favoriteBook);
        return true;
    }

    public void deleteFavoriteBook(String userId, String bookId) {

        FavoriteBook favoriteBook = favoriteBookDao.findOneByUserIdAndBookIdAndDeleteFlag(userId, bookId, 0);
        favoriteBook.setDeleteFlag(1);
        favoriteBookDao.save(favoriteBook);
    }

    public Page<FavoriteBook> getFavoriteBook(Map<String, Object> searchParams, int pageNo, int pageSize, String userId) {

        //
        searchParams.put("EQ_user.id", userId);
        // TODO sort
//        Sort sort = new Sort(Direction.DESC, "user.id");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
        Specification<FavoriteBook> spec = buildSpecificationComment(searchParams);
        return favoriteBookDao.findAll(spec, pageRequest);
    }

    private Specification<FavoriteBook> buildSpecificationComment(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FavoriteBook> spec = DynamicSpecifications.bySearchFilter(filters.values(), FavoriteBook.class);
        return spec;
    }

}
