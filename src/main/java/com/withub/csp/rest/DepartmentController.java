package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Court;
import com.withub.csp.entity.Department;
import com.withub.csp.service.CourtService;
import com.withub.csp.service.DepartmentService;
import com.withub.csp.service.UserService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/department")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourtService courtService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Department> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "search_name", defaultValue = "") String name,
            @RequestParam(value = "search_courtId", defaultValue = "1") String courtId
    ) {
        userService.checkPermission(courtId);

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_name", name);
        searchParams.put("EQ_court.id", courtId);

        return departmentService.getDepartment(searchParams, pageNo, pageSize);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Department get(@PathVariable("id") String id) {
        return departmentService.getDepartment(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody Department department) {
        userService.checkPermission(department.getCourt().getId());
        departmentService.saveDepartment(department);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Department department) {
        userService.checkPermission(department.getCourt().getId());
        departmentService.saveDepartment(department);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        departmentService.deleteDepartment(id);
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONArray group() {

        JSONArray groups = new JSONArray();
        for (Court court : courtService.getAllCourt()) {
            JSONObject group = new JSONObject();
            JSONArray items = new JSONArray();
            for (Department department : court.getDepartmentList()) {
                JSONObject item = new JSONObject();
                item.put("id", department.getId());
                item.put("name", department.getName());
                items.add(item);
            }
            group.put("name", court.getName());
            group.put("items", items);
            groups.add(group);
        }
        return groups;
    }

    @RequestMapping(value = "/listByCourtId", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONArray group(String courtId) {

        return departmentService.getDepartmentListByCourtId(courtId);
    }

}
