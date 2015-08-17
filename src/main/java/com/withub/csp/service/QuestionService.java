package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.User;
import com.withub.csp.entity.Question;
import com.withub.csp.repository.UserDao;
import com.withub.csp.repository.QuestionDao;
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
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    //

    public Page<Question> getQuestion(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Question> spec = buildSpecificationQuestion(searchParams);
        return questionDao.findAll(spec, pageRequest);
    }

    private Specification<Question> buildSpecificationQuestion(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Question.class);
    }

    public JSONObject create(String userId, String content) {

        JSONObject jsonObject = new JSONObject();

        User user = userDao.findOne(userId);
        if (user == null) {
            jsonObject.put("result", false);
            return jsonObject;
        }

        Question question = new Question();
        question.setId(Identities.uuid());
        question.setContent(content);
        question.setUser(user);
        question.setEventTime(new Date());
        question.setDeleteFlag(0);
        questionDao.save(question);

        jsonObject.put("result", true);
        return jsonObject;
    }

}
