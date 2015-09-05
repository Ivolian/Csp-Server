package com.withub.csp.repository;

import com.withub.csp.entity.Book;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface BookDao extends PagingAndSortingRepository<Book, String>, JpaSpecificationExecutor<Book> {

    @Query("select max(u.orderNo) from Book u")
    public Integer getMaxOrderNo();

    public Book findOneByEbookFilenameAndDeleteFlag(String ebookFilename,Integer deleteFlag);


    @Query("select o from Book o where o.deleteFlag=0 and o.ebookFilename=:ebookFilename and o.id<>:bookId")
    public Book isBookExist(@Param("ebookFilename") String ebookFilename, @Param("bookId") String bookId);

}
