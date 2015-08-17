package com.withub.csp.repository;

import com.withub.csp.entity.Answer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AnswerDao extends PagingAndSortingRepository<Answer, String>, JpaSpecificationExecutor<Answer> {


}
