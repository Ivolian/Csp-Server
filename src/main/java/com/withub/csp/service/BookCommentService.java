package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Book;
import com.withub.csp.entity.BookComment;
import com.withub.csp.entity.User;
import com.withub.csp.repository.BookCommentDao;
import com.withub.csp.repository.BookDao;
import com.withub.csp.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import java.util.Map;


@Service
@Transactional
public class BookCommentService extends BaseService {

    @Autowired
    private BookCommentDao bookCommentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BookDao bookDao;

    //

    public Page<BookComment> getBookComment(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<BookComment> spec = buildSpecificationBookComment(searchParams);
        return bookCommentDao.findAll(spec, pageRequest);
    }

    // 手机端创建评论
    public JSONObject create(String userId, String newsId, String content) {

        JSONObject result = new JSONObject();

        // 检查用户
        User user = userDao.findOneByIdAndDeleteFlag(userId, 0);
        if (user == null) {
            result.put("result", false);
            result.put("errorMsg", "用户不存在");        // 用户登录后，用户在后台被删除了。
            return result;
        }

        // 检查提问
        Book book = bookDao.findOne(newsId);
        if (book == null) {
            result.put("result", false);
            result.put("errorMsg", "该书籍已被删除");     // 发表回答前，新闻在后台被删除了。
            return result;
        }

        // 创建评论
        BookComment comment = new BookComment();
        initEntity(comment);
        comment.setUser(user);
        comment.setBook(book);
        comment.setContent(content);
        bookCommentDao.save(comment);

        result.put("result", true);
        return result;
    }


    // 基本无视的方法

    private Specification<BookComment> buildSpecificationBookComment(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), BookComment.class);
    }

    public BookComment getBookComment(String id) {
        return bookCommentDao.findOne(id);
    }

    public void deleteBookComment(String id) {
        BookComment comment = getBookComment(id);
        comment.setDeleteFlag(1);
        bookCommentDao.save(comment);
    }


}
