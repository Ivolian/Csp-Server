package com.withub.csp.repository;

import com.withub.csp.entity.District;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DistrictDao extends PagingAndSortingRepository<District, String>, JpaSpecificationExecutor<District> {

}
