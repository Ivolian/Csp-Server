package com.withub.csp.repository;

import com.withub.csp.entity.Thumb;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ThumbDao extends PagingAndSortingRepository<Thumb, String>, JpaSpecificationExecutor<Thumb> {

    public Thumb findOneByUserIdAndNewsIdAndDeleteFlag(String userId,String newsId,Integer deleteFlag);

}

