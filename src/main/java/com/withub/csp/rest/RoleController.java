package com.withub.csp.rest;

import com.withub.csp.entity.Role;
import com.withub.csp.service.RoleService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Role> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        return roleService.getRole(searchParams, pageNo, pageSize);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Role get(@PathVariable("id") String id) {
        return roleService.getRole(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody Role role) {
        roleService.saveRole(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody Role role) {
        roleService.saveRole(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        roleService.deleteRole(id);
    }

}
