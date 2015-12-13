package com.withub.csp.service;

import com.withub.csp.entity.BookThumb;
import com.withub.csp.repository.BookDao;
import com.withub.csp.repository.BookThumbDao;
import com.withub.csp.repository.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import java.util.Map;


@Service
@Transactional
public class BookThumbService extends BaseService{

    @Autowired
    private BookThumbDao bookThumbDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BookDao bookDao;

    //

    public void saveBookThumb(BookThumb thumb) {

        if (StringUtils.isEmpty(thumb.getId())) {
           initEntity(thumb);
        }
        bookThumbDao.save(thumb);
    }

    public boolean create(String userId, String bookId) {

        BookThumb bookThumb = bookThumbDao.findOneByUserIdAndBookIdAndDeleteFlag(userId, bookId, 0);
        if (bookThumb != null) {
            return false;
        }

        bookThumb = new BookThumb();
        bookThumb.setUser(userDao.findOne(userId));
        bookThumb.setBook(bookDao.findOne(bookId));
        saveBookThumb(bookThumb);
        return true;
    }

    public Page<BookThumb> getBookThumb(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Sort.Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<BookThumb> spec = buildSpecification(searchParams);
        return bookThumbDao.findAll(spec, pageRequest);
    }

    // 基本无视的方法

    private Specification<BookThumb> buildSpecification(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), BookThumb.class);
    }

}
