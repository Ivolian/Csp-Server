package com.withub.csp.repository;

import com.withub.csp.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserDao extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {

    public User findOneByIdAndDeleteFlag(String id, Integer deleteFlag);

    public User findOneByUsernameAndDeleteFlag(String username, Integer deleteFlag);

    public User findOneByUsername(String username);

}
