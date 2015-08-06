package com.withub.repository;

import com.withub.entity.Answer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AnswerDao extends PagingAndSortingRepository<Answer, String>, JpaSpecificationExecutor<Answer> {


}
