package com.withub.csp.repository;

import com.withub.csp.entity.NewsRead;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;


public interface NewsReadDao extends PagingAndSortingRepository<NewsRead, String>, JpaSpecificationExecutor<NewsRead> {

    public NewsRead findOneByUserIdAndNewsId(String userId, String newsId);

    @Query("select count(o) from NewsRead o where o.user.id=:userId and o.eventTime between :beginDate and :endDate")
    public Integer getReadTimesByUserIdAndDateRange(@Param("userId") String userId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);


}
