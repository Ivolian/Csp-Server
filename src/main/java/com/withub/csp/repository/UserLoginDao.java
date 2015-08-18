package com.withub.csp.repository;

import com.withub.csp.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserLoginDao extends PagingAndSortingRepository<UserLogin, String>, JpaSpecificationExecutor<UserLogin> {

}
