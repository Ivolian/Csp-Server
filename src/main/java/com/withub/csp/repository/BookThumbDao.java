package com.withub.csp.repository;

import com.withub.csp.entity.BookThumb;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookThumbDao extends PagingAndSortingRepository<BookThumb, String>, JpaSpecificationExecutor<BookThumb> {

    public BookThumb findOneByUserIdAndBookIdAndDeleteFlag(String userId, String bookId, Integer deleteFlag);

}

