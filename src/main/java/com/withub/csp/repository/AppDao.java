package com.withub.csp.repository;

import com.withub.csp.entity.App;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface AppDao extends PagingAndSortingRepository<App, String>, JpaSpecificationExecutor<App> {

    @Query("select max(u.versionCode) from App u")
    public Integer getMaxVersionCode();

}
