package com.withub.csp.repository;

import com.withub.csp.entity.NewsRead;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface NewsReadDao extends PagingAndSortingRepository<NewsRead, String>, JpaSpecificationExecutor<NewsRead> {

    public NewsRead findOneByUserIdAndNewsId(String userId, String newsId);

}
