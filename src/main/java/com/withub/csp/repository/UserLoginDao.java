package com.withub.csp.repository;

import com.withub.csp.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;


public interface UserLoginDao extends PagingAndSortingRepository<UserLogin, String>, JpaSpecificationExecutor<UserLogin> {

    @Query("select count(o) from UserLogin o where o.user.id=:userId and o.eventTime between :beginDate and :endDate")
    public Integer getLoginTimesByUserIdAndDateRange(@Param("userId") String userId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

}
