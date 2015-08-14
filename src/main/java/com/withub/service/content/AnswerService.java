package com.withub.service.content;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Answer;
import com.withub.csp.entity.Question;
import com.withub.csp.entity.User;
import com.withub.repository.AnswerDao;
import com.withub.csp.repository.UserDao;
import com.withub.repository.QuestionDao;
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

    public Answer getAnswer(String id) {
        return answerDao.findOne(id);
    }

    public void saveAnswer(Answer entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }
        answerDao.save(entity);
    }

    public void deleteAnswer(String id) {
        Answer answer = getAnswer(id);
        answer.setDeleteFlag(1);
        answerDao.save(answer);
    }

    public Page<Answer> getAnswer(Map<String, Object> searchParams, int pageNo, int pageSize,String questionId) {

        searchParams.put("EQ_question.id", questionId);
        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Answer> spec = buildSpecificationAnswer(searchParams);
        return answerDao.findAll(spec, pageRequest);
    }

    private Specification<Answer> buildSpecificationAnswer(Map<String, Object> searchParams) {

        if (searchParams.isEmpty()) {
        } else {
            searchParams.put("_LIKE_region.parent.name", searchParams.get("_LIKE_region"));
            searchParams.remove("_LIKE_region");
        }
        searchParams.put("EQ_deleteFlag", "0");

        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Answer> spec = DynamicSpecifications.bySearchFilter(filters.values(), Answer.class);
        return spec;
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
