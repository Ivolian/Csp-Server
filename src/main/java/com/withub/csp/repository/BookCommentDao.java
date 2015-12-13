package com.withub.csp.repository;

import com.withub.csp.entity.BookComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BookCommentDao extends PagingAndSortingRepository<BookComment, String>, JpaSpecificationExecutor<BookComment> {

}
