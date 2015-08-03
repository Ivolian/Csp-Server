package com.withub.repository;

import com.withub.entity.Position;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PositionDao extends PagingAndSortingRepository<Position, String>, JpaSpecificationExecutor<Position> {

    @Query("select max(u.id2) from Position u")
    public Integer getMaxId2();

}
