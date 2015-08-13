package com.withub.repository;

import com.withub.csp.entity.Comment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentDao extends PagingAndSortingRepository<Comment, String>, JpaSpecificationExecutor<Comment> {

}
