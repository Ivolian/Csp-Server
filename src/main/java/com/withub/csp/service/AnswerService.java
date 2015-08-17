package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Answer;
import com.withub.csp.entity.Question;
import com.withub.csp.entity.User;
import com.withub.csp.repository.AnswerDao;
import com.withub.csp.repository.UserDao;
import com.withub.csp.repository.QuestionDao;
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

import java.util.*;


@Component
@Transactional
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    //

    public Page<Answer> getAnswer(Map<String, Object> searchParams, int pageNo, int pageSize,String questionId) {

        searchParams.put("EQ_question.id", questionId);
        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Answer> spec = buildSpecificationAnswer(searchParams);
        return answerDao.findAll(spec, pageRequest);
    }

    private Specification<Answer> buildSpecificationAnswer(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return  DynamicSpecifications.bySearchFilter(filters.values(), Answer.class);
    }

    public JSONObject create(String userId,String questionId, String content) {

        JSONObject jsonObject = new JSONObject();

        User user = userDao.findOne(userId);
        Question question = questionDao.findOne(questionId);
        if (user == null ) {
            jsonObject.put("result", false);
            return jsonObject;
        }

        Answer answer = new Answer();
        answer.setId(Identities.uuid());
        answer.setContent(content);
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setEventTime(new Date());
        answer.setDeleteFlag(0);
        answerDao.save(answer);

        jsonObject.put("result", true);
        return jsonObject;
    }

}
