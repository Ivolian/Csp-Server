package com.withub.repository;

import com.withub.entity.Question;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionDao extends PagingAndSortingRepository<Question, String>, JpaSpecificationExecutor<Question> {

}
