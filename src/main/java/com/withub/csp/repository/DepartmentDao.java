package com.withub.csp.repository;

import com.withub.csp.entity.Department;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepartmentDao extends PagingAndSortingRepository<Department, String>, JpaSpecificationExecutor<Department>{

}
