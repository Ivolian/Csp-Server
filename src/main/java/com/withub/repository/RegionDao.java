package com.withub.repository;

import com.withub.entity.Region;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RegionDao extends PagingAndSortingRepository<Region, String>, JpaSpecificationExecutor<Region>{

    public Region parentIsNull();
}
