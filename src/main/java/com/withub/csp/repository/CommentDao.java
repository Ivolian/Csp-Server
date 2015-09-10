package com.withub.csp.repository;

import com.withub.csp.entity.Comment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;


public interface CommentDao extends PagingAndSortingRepository<Comment, String>, JpaSpecificationExecutor<Comment> {

    @Query("select count(o) from Comment o where o.deleteFlag=0 and o.user.id=:userId and o.eventTime between :beginDate and :endDate")
    public Integer getCommentCountByUserIdAndDateRange(@Param("userId") String userId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

}
