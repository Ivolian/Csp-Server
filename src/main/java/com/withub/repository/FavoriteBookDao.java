package com.withub.repository;

import com.withub.csp.entity.FavoriteBook;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FavoriteBookDao extends PagingAndSortingRepository<FavoriteBook, String>, JpaSpecificationExecutor<FavoriteBook> {

    public FavoriteBook findOneByUserIdAndBookIdAndDeleteFlag(String userId, String bookId, Integer deleteFlag);

}
