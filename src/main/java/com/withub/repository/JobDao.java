package com.withub.repository;

import com.withub.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface JobDao extends PagingAndSortingRepository<Job, String>, JpaSpecificationExecutor<Job> {


}
