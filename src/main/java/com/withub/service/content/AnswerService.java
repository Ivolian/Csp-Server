package com.withub.service.content;

import com.withub.entity.*;
import com.withub.repository.AnswerDao;
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

    public Page<Answer> getAnswer(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.ASC, "eventTime");
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

    public List<Answer> getAllAnswer() {
        Sort sort = new Sort(Direction.ASC, "orderNo");
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_publish", "1");
        return answerDao.findAll(buildSpecificationAnswer(searchParams), sort);
    }

}
