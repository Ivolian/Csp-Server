package com.withub.repository;

import com.withub.entity.CspUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CspUserDao extends PagingAndSortingRepository<CspUser, String>, JpaSpecificationExecutor<CspUser> {

    public CspUser findOneByUsernameAndPassword(String username, String password);

}
