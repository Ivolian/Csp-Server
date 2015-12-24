package com.withub.csp.repository;

import com.withub.csp.entity.Notice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoticeDao extends PagingAndSortingRepository<Notice, String>, JpaSpecificationExecutor<Notice> {



}
