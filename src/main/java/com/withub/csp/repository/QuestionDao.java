package com.withub.csp.repository;

import com.withub.csp.entity.Question;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionDao extends PagingAndSortingRepository<Question, String>, JpaSpecificationExecutor<Question> {

    public Question findOneByIdAndDeleteFlag(String id, Integer deleteFlag);

}
