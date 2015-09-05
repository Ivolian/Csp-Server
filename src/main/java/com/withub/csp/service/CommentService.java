package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Comment;
import com.withub.csp.entity.News;
import com.withub.csp.entity.User;
import com.withub.csp.repository.CommentDao;
import com.withub.csp.repository.NewsDao;
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
public class CommentService extends BaseService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NewsDao newsDao;

    //

    public Page<Comment> getComment(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Comment> spec = buildSpecificationComment(searchParams);
        return commentDao.findAll(spec, pageRequest);
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
        News news = newsDao.findOneByIdAndDeleteFlag(newsId, 0);
        if (news == null) {
            result.put("result", false);
            result.put("errorMsg", "该新闻已被删除");     // 发表回答前，新闻在后台被删除了。
            return result;
        }

        // 创建评论
        Comment comment = new Comment();
        initEntity(comment);
        comment.setUser(user);
        comment.setNews(news);
        comment.setContent(content);
        commentDao.save(comment);

        result.put("result", true);
        return result;
    }


    // 基本无视的方法

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


}
