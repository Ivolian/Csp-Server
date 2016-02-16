package com.withub.csp.repository;


import com.withub.csp.entity.SystemMenu;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SystemMenuDao extends PagingAndSortingRepository<SystemMenu, String>, JpaSpecificationExecutor<SystemMenu> {

    List<SystemMenu> findByParent(SystemMenu parent);

}
