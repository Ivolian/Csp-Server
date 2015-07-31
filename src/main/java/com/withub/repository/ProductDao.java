package com.withub.repository;

import com.withub.entity.Job;
import com.withub.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductDao extends PagingAndSortingRepository<Product, String>, JpaSpecificationExecutor<Product> {

}
