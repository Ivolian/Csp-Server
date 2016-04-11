package com.withub.csp.repository;

import com.withub.csp.entity.NewsData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;


public interface NewsDataDao extends CrudRepository<NewsData, String>, JpaSpecificationExecutor<NewsData> {

    public NewsData findOneByNewsId(String newsId);

}
