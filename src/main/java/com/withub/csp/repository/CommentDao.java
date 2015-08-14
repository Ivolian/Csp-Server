package com.withub.csp.repository;

import com.withub.csp.entity.Comment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface CommentDao extends PagingAndSortingRepository<Comment, String>, JpaSpecificationExecutor<Comment> {

    @Query("select count(o) from Comment o where o.deleteFlag=0 and o.news.id=?1")
    public Integer getCommentCountOfNews(String newsId);

}
