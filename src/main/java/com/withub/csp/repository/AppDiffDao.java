package com.withub.csp.repository;

import com.withub.csp.entity.AppDiff;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface AppDiffDao extends PagingAndSortingRepository<AppDiff, String>, JpaSpecificationExecutor<AppDiff> {

    @Query ("select o.versionName from AppDiff o order by o.versionName desc")
    public List<String> findVersionNameList();

    public AppDiff findOneByVersionNameAndClientVersionNameAndDeleteFlag(String versionName,String clientVersionName,int deleteFlag);

}
