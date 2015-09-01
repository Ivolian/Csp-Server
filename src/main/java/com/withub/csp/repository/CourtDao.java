package com.withub.csp.repository;

import com.withub.csp.entity.Court;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CourtDao extends PagingAndSortingRepository<Court, String>, JpaSpecificationExecutor<Court>{

    public Court parentIsNull();

}
