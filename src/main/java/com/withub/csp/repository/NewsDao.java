package com.withub.csp.repository;

import com.withub.csp.entity.News;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface NewsDao extends PagingAndSortingRepository<News, String>, JpaSpecificationExecutor<News> {

    public News findOneByIdAndDeleteFlag(String id, Integer deleteFlag);

    public List<News> findByTopAndDeleteFlag(Integer top,Integer deleteFlag);

}
