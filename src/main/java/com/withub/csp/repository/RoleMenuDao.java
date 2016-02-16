package com.withub.csp.repository;

import com.withub.csp.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RoleMenuDao extends JpaRepository<RoleMenu, String> {

    @Modifying
    @Query("delete from RoleMenu a where a.role.id = ?1")
    void deleteByRoleId(String roleId);

    @Query("select a from RoleMenu a where a.role.id = ?1")
    List<RoleMenu> findByRoleId(String roleId);
}
