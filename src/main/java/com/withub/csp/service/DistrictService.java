package com.withub.csp.service;

import com.withub.csp.entity.District;
import com.withub.csp.repository.DistrictDao;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Transactional
public class DistrictService extends BaseService {

    @Autowired
    private DistrictDao districtDao;

    //

    public District getDistrict(String id) {
        return districtDao.findOne(id);
    }

    public Page<District> getDistrict(Map<String, Object> searchParams, int pageNo, int pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNo, pageSize);
        Specification<District> spec = buildSpecification(searchParams);
        return districtDao.findAll(spec, pageRequest);
    }

    private PageRequest buildPageRequest(int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.ASC, "eventTime");
        return new PageRequest(pageNo - 1, pageSize, sort);
    }

    private Specification<District> buildSpecification(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), District.class);
    }

    public void saveDistrict(District entity) {

        if (StringUtils.isEmpty(entity.getId())){
            initEntity(entity);
        }
        districtDao.save(entity);
    }

    public void deleteDistrict(String id) {
        District district = getDistrict(id);
        district.setDeleteFlag(1);
        districtDao.save(district);
    }

    public List<District> getAllDistrict() {
        Sort sort = new Sort(Direction.ASC, "eventTime");
        Map<String, Object> searchParams = new HashMap<>();
        return districtDao.findAll(buildSpecification(searchParams), sort);
    }



}
