package com.withub.service.content;

import com.alibaba.fastjson.JSONObject;
import com.withub.entity.Comment;
import com.withub.entity.Content;
import com.withub.entity.CspUser;
import com.withub.repository.CommentDao;
import com.withub.repository.ContentDao;
import com.withub.repository.CspUserDao;
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

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class CommentService {

    private CommentDao commentDao;

    @Autowired
    private CspUserDao cspUserDao;

    @Autowired
    private ContentDao contentDao;

    @Autowired
    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public Comment getComment(String id) {
        return commentDao.findOne(id);
    }

    public void saveComment(Comment entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }
        commentDao.save(entity);
    }

    public void deleteComment(String id) {
        Comment comment = getComment(id);
        comment.setDeleteFlag(1);
        commentDao.save(comment);
    }

    public Page<Comment> getComment(Map<String, Object> searchParams, int pageNo, int pageSize,String contentId) {

        //
        searchParams.put("EQ_content.id",contentId);

        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Comment> spec = buildSpecificationComment(searchParams);
        return commentDao.findAll(spec, pageRequest);
    }

    private Specification<Comment> buildSpecificationComment(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Comment> spec = DynamicSpecifications.bySearchFilter(filters.values(), Comment.class);
        return spec;
    }

    // 新增的

    public JSONObject create(String userId, String contentId, String words) {

        JSONObject jsonObject = new JSONObject();

        CspUser cspUser = cspUserDao.findOne(userId);
        Content content = contentDao.findOne(contentId);
        if (cspUser == null) {
            jsonObject.put("result", false);
            jsonObject.put("errorMsg", "用户" + userId + " 不存在");
        }
        if (content == null) {
            jsonObject.put("result", false);
            jsonObject.put("errorMsg", "新闻" + contentId + " 不存在");
        }

        Comment comment = new Comment();
        comment.setId(Identities.uuid());
//        comment.setCspUser(cspUser);
        comment.setContent(content);
        comment.setWords(words);
        comment.setEventTime(new Date());
        comment.setDeleteFlag(0);
        commentDao.save(comment);

        jsonObject.put("result", true);
        return jsonObject;
    }

}
