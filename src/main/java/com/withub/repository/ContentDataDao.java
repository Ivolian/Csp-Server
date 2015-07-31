/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.withub.repository;

import com.withub.entity.ContentData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ContentDataDao extends CrudRepository<ContentData, String>, JpaSpecificationExecutor<ContentData> {


}
