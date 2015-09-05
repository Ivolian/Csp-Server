package com.withub.csp.repository;

import com.withub.csp.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface UserDao extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {

    public User findOneByIdAndDeleteFlag(String id, Integer deleteFlag);

    public User findOneByUsernameAndDeleteFlag(String username, Integer deleteFlag);

    @Query("select o from User o where o.deleteFlag=0 and o.username=:username and o.id<>:userId")
    public User isUserExist(@Param("username") String username, @Param("userId") String userId);

}
