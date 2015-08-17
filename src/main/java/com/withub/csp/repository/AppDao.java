package com.withub.csp.repository;

import com.withub.csp.entity.App;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface AppDao extends PagingAndSortingRepository<App, String>, JpaSpecificationExecutor<App> {

    @Query("select o from App o where o.deleteFlag=0 order by o.eventTime desc")
    public List<App> findAppList();

}
