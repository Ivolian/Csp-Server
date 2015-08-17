package com.withub.csp.repository;

import com.withub.csp.entity.Book;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BookDao extends PagingAndSortingRepository<Book, String>, JpaSpecificationExecutor<Book> {

    @Query("select max(u.orderNo) from Book u")
    public Integer getMaxOrderNo();

}
