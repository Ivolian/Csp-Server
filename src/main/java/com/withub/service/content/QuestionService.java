package com.withub.service.content;

import com.withub.entity.Question;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public Question getQuestion(String id) {
        return questionDao.findOne(id);
    }

    public void saveQuestion(Question entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }
        questionDao.save(entity);
    }

    public void deleteQuestion(String id) {
        Question question = getQuestion(id);
        question.setDeleteFlag(1);
        questionDao.save(question);
    }

    public Page<Question> getQuestion(Map<String, Object> searchParams, int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.ASC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Question> spec = buildSpecificationQuestion(searchParams);
        return questionDao.findAll(spec, pageRequest);
    }

    private Specification<Question> buildSpecificationQuestion(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Question> spec = DynamicSpecifications.bySearchFilter(filters.values(), Question.class);
        return spec;
    }

}
