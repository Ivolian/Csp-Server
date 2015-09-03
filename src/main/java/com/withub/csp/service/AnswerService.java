package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Answer;
import com.withub.csp.entity.Question;
import com.withub.csp.entity.User;
import com.withub.csp.repository.AnswerDao;
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
public class AnswerService extends BaseService {


    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;


    // ======================= Methods =======================

    // 问题列表查询方法，供后台和手机端使用
    public Page<Answer> getAnswer(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "eventTime","question.id");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Answer> spec = buildSpecificationAnswer(searchParams);
        return answerDao.findAll(spec, pageRequest);
    }


    // 创建提问，供手机端使用
    public JSONObject create(String userId, String questionId, String content) {

        JSONObject result = new JSONObject();

        // 检查用户
        User user = userDao.findOneByIdAndDeleteFlag(userId, 0);
        if (user == null) {
            result.put("result", false);
            result.put("errorMsg", "用户不存在");        // 用户登录后，用户在后台被删除了。
            return result;
        }

        // 检查提问
        Question question = questionDao.findOneByIdAndDeleteFlag(questionId, 0);
        if (question == null) {
            result.put("result", false);
            result.put("errorMsg", "该提问已被删除");     // 发表回答前，提问在后台被删除了。
            return result;
        }

        // 正常创建回答
        Answer answer = new Answer();
        initEntity(answer);
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setContent(content);
        answerDao.save(answer);

        result.put("result", true);
        return result;
    }


    // 基本无视的方法
    private Specification<Answer> buildSpecificationAnswer(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Answer.class);
    }

}
