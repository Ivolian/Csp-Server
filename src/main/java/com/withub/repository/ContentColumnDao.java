/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.withub.repository;

import com.withub.entity.ContentColumn;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContentColumnDao extends PagingAndSortingRepository<ContentColumn, String>, JpaSpecificationExecutor<ContentColumn> {

}
