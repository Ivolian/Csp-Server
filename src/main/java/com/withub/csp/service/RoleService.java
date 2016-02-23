package com.withub.csp.service;

import com.withub.csp.entity.Role;
import com.withub.csp.entity.RoleMenu;
import com.withub.csp.entity.SystemMenu;
import com.withub.csp.repository.RoleDao;
import com.withub.csp.repository.RoleMenuDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Transactional
public class RoleService extends BaseService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleMenuDao roleMenuDao;


    //

    public Role getRole(String id) {
        return roleDao.findOne(id);
    }

    public Page<Role> getRole(Map<String, Object> searchParams, int pageNo, int pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNo, pageSize);
        Specification<Role> spec = buildSpecification(searchParams);
        return roleDao.findAll(spec, pageRequest);
    }

    private PageRequest buildPageRequest(int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.DESC, "eventTime");
        return new PageRequest(pageNo - 1, pageSize, sort);
    }

    private Specification<Role> buildSpecification(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Role.class);
    }

    public void saveRole(Role entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            initEntity(entity);
        }
        roleDao.save(entity);
    }

    public void deleteRole(String id) {
        Role role = getRole(id);
        role.setDeleteFlag(1);
        roleDao.save(role);
    }

    // role menu

    public void saveRoleMenu(String roleId, String[] systemMenuIds) {

        // 清除该角色已配菜单
        roleMenuDao.deleteByRoleId(roleId);

        // 保存新分配的菜单
        Role role = getRole(roleId);
        for (String systemMenuId : systemMenuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setId(Identities.uuid());
            roleMenu.setRole(role);
            SystemMenu systemMenu = new SystemMenu();
            systemMenu.setId(systemMenuId);
            roleMenu.setSystemMenu(systemMenu);
            roleMenuDao.save(roleMenu);
        }
    }

    public List<RoleMenu> getRoleMenuList(String objectId) {
        return roleMenuDao.findByRoleId(objectId);
    }

    //

    public List<Role> getAllRole() {
        Sort sort = new Sort(Direction.ASC, "eventTime");
        Map<String, Object> searchParams = new HashMap<>();
        return roleDao.findAll(buildSpecification(searchParams), sort);
    }

}
