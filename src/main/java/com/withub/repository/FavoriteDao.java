package com.withub.repository;

import com.withub.entity.Comment;
import com.withub.entity.Favorite;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FavoriteDao extends PagingAndSortingRepository<Favorite, String>, JpaSpecificationExecutor<Favorite> {



}
