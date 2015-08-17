package com.withub.csp.service;

import com.withub.csp.entity.FavoriteBook;
import com.withub.csp.repository.UserDao;
import com.withub.csp.repository.FavoriteBookDao;
import com.withub.csp.repository.BookDao;
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

import java.util.Date;
import java.util.Map;


@Component
@Transactional
public class FavoriteBookService {

    @Autowired
    private FavoriteBookDao favoriteBookDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BookDao bookDao;

    //

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
            favoriteBook.setEventTime(new Date());
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

        searchParams.put("EQ_user.id", userId);
        Sort sort = new Sort(Sort.Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
        Specification<FavoriteBook> spec = buildSpecificationComment(searchParams);
        return favoriteBookDao.findAll(spec, pageRequest);
    }

    private Specification<FavoriteBook> buildSpecificationComment(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), FavoriteBook.class);
    }

}
