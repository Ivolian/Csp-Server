package com.withub.csp.repository;

import com.withub.csp.entity.Thumb;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ThumbDao extends PagingAndSortingRepository<Thumb, String>, JpaSpecificationExecutor<Thumb> {

    public Thumb findOneByUserIdAndNewsIdAndDeleteFlag(String userId, String newsId, Integer deleteFlag);

    @Query("select count(o) from Thumb o where o.deleteFlag=0 and o.user.id=:userId and o.eventTime between :beginDate and :endDate")
    public Integer getThumbCountByUserIdAndDateRange(@Param("userId") String userId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    @Query("select count(o) from Thumb o where o.deleteFlag=0 and o.news.id=:newsId")
    public Integer countByNewsId(@Param("newsId") String newsId);

}

