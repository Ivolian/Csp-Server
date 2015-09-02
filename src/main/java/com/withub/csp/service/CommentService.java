package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Book;
import com.withub.csp.entity.Comment;
import com.withub.csp.entity.News;
import com.withub.csp.entity.User;
import com.withub.csp.repository.CommentDao;
import com.withub.csp.repository.NewsDao;
import com.withub.csp.repository.UserDao;
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

import java.util.Date;
import java.util.Map;


@Component
@Transactional
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NewsDao newsDao;

    //

    public Page<Comment> getComment(Map<String, Object> searchParams, int pageNo, int pageSize, String newsId) {

        searchParams.put("EQ_news.id", newsId);
        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Comment> spec = buildSpecificationComment(searchParams);
        return commentDao.findAll(spec, pageRequest);
    }

    private Specification<Comment> buildSpecificationComment(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Comment.class);
    }

    public Comment getComment(String id) {
        return commentDao.findOne(id);
    }


    public void deleteComment(String id) {
        Comment comment = getComment(id);
        comment.setDeleteFlag(1);
        commentDao.save(comment);
    }

    public JSONObject create(String userId, String newsId, String content) {

        JSONObject jsonObject = new JSONObject();

        User user = userDao.findOne(userId);
        News news = newsDao.findOne(newsId);
        if (user == null || news == null) {
            jsonObject.put("result", false);
            return jsonObject;
        }

        Comment comment = new Comment();
        comment.setId(Identities.uuid());
        comment.setUser(user);
        comment.setNews(news);
        comment.setContent(content);
        comment.setEventTime(new Date());
        comment.setDeleteFlag(0);
        commentDao.save(comment);

        jsonObject.put("result", true);
        return jsonObject;
    }

}
