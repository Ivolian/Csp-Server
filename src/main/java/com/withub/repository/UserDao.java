/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.withub.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.withub.entity.SSUser;

public interface UserDao extends PagingAndSortingRepository<SSUser, Long> {
	SSUser findByLoginName(String loginName);
}
