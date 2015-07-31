package com.withub.repository;

import com.withub.entity.Menu;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MenuDao extends PagingAndSortingRepository<Menu, String>, JpaSpecificationExecutor<Menu>{

    public Menu parentIsNull();

}
