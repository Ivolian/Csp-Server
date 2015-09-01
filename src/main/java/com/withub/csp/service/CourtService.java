package com.withub.csp.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Court;
import com.withub.csp.repository.CourtDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional


public class CourtService {

    @Autowired
    private CourtDao courtDao;

    public Court getCourt(String id) {
        return courtDao.findOne(id);
    }

    public void saveCourt(Court entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);
        }
        courtDao.save(entity);
    }

    public void deleteCourt(String id) {
        Court court = getCourt(id);
        court.setDeleteFlag(1);
        courtDao.save(court);
    }

    public Court getRootCourt() {
        return courtDao.parentIsNull();
    }

    public List<Court> getCourtByParentId(String parentId) {

//        Sort sort = new Sort(Direction.ASC, "orderNo");
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_parent.id", parentId);
        Specification<Court> spec = buildSpecificationRegion(searchParams);
        return courtDao.findAll(spec);
    }

    private Specification<Court> buildSpecificationRegion(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Court.class);
    }

    public List<Court> getAllCourt() {
        Sort sort = new Sort(Direction.ASC, "parent", "orderNo");
        Map<String, Object> searchParams = new HashMap<>();
        return courtDao.findAll(buildSpecificationRegion(searchParams), sort);
    }

}
