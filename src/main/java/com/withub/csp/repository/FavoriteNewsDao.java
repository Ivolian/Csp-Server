package com.withub.csp.repository;

import com.withub.csp.entity.FavoriteNews;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FavoriteNewsDao extends PagingAndSortingRepository<FavoriteNews, String>, JpaSpecificationExecutor<FavoriteNews> {

    public FavoriteNews findOneByUserIdAndNewsIdAndDeleteFlag(String userId,String newsId,Integer deleteFlag);

}
