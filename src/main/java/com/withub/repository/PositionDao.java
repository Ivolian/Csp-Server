package com.withub.repository;

import com.withub.entity.Book;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PositionDao extends PagingAndSortingRepository<Book, String>, JpaSpecificationExecutor<Book> {

    @Query("select max(u.orderNo) from Book u")
    public Integer getMaxOrderNo();

}
