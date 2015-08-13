package com.withub.repository;

import com.withub.csp.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CspUserDao extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {

    public User findOneByUsernameAndPasswordAndDeleteFlag(String username, String password,Integer deleteFlag);

}
