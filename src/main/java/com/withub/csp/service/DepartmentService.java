package com.withub.csp.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Court;
import com.withub.csp.entity.Department;
import com.withub.csp.repository.CourtDao;
import com.withub.csp.repository.DepartmentDao;
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

import java.util.*;

@Component
@Transactional


public class DepartmentService {

    @Autowired
    private CourtDao courtDao;

    @Autowired
    private DepartmentDao departmentDao;

    public Department getDepartment(String id) {
        return departmentDao.findOne(id);
    }

    public void saveDepartment(Department entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);
        }
        departmentDao.save(entity);
    }

    public Page<Department> getDepartment(Map<String, Object> searchParams, int pageNo, int pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNo, pageSize);
        Specification<Department> spec = buildSpecification(searchParams);
        return departmentDao.findAll(spec, pageRequest);
    }


    private PageRequest buildPageRequest(int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.ASC, "court", "eventTime");
        return new PageRequest(pageNo - 1, pageSize, sort);
    }


    public void deleteDepartment(String id) {
        Department department = getDepartment(id);
        department.setDeleteFlag(1);
        departmentDao.save(department);
    }

    private Specification<Department> buildSpecification(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Department.class);
    }

    public JSONArray getDepartmentListByCourtId(String courtId) {

        Court court = courtDao.findOne(courtId);
        JSONArray jsonArray = new JSONArray();
        for (Department department : court.getDepartmentList()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", department.getId());
            jsonObject.put("name", department.getName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

}
