package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Question;
import com.withub.csp.entity.User;
import com.withub.csp.repository.QuestionDao;
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
public class QuestionService extends BaseService{

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;


    // ======================= Methods =======================

    // 问题列表查询方法，供后台和手机端使用
    public Page<Question> getQuestion(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Question> spec = buildSpecificationQuestion(searchParams);
        return questionDao.findAll(spec, pageRequest);
    }

    // 创建提问，供手机端使用
    public JSONObject create(String userId, String content) {

        JSONObject result = new JSONObject();

        // 检查用户
        User user = userDao.findOneByIdAndDeleteFlag(userId, 0);
        if (user == null) {
            result.put("result", false);
            result.put("errorMsg", "用户不存在");        // 用户登录后，用户在后台被删除了。
            return result;
        }

        // 创建提问
        Question question = new Question();
        initEntity(question);
        question.setUser(user);
        question.setContent(content);
        questionDao.save(question);

        result.put("result", true);
        return result;
    }

    public Question getQuestion(String id) {
        return questionDao.findOne(id);
    }

    public void deleteQuestion(String id) {

        Question question = getQuestion(id);
        question.setDeleteFlag(1);
        questionDao.save(question);
    }

    // 基本无视的方法
    private Specification<Question> buildSpecificationQuestion(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Question.class);
    }

}
