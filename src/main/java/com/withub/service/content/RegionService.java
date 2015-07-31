package com.withub.service.content;

import com.withub.entity.Region;
import com.withub.repository.RegionDao;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class RegionService {
    private RegionDao regionDao;

    @Autowired
    public void setRegionDao(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    public Region getRegion(String id) {
        return regionDao.findOne(id);
    }

    public void saveRegion(Region entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
        }
        entity.setDeleteFlag(0);
        regionDao.save(entity);
    }

    public void deleteRegion(String id) {
        Region region = getRegion(id);
        region.setDeleteFlag(1);
        regionDao.save(region);
    }

    public Region getRootRegion() {
        return regionDao.parentIsNull();
    }

    public List<Region> getRegionByParentId(String parentId) {
        Sort sort = new Sort(Direction.ASC, "orderNo");
        Map<String, Object> searchParams = new HashMap();
        searchParams.put("EQ_parent.id", parentId);
        Specification<Region> spec = buildSpecificationRegion(searchParams);
        return regionDao.findAll(spec, sort);
    }

    private Specification<Region> buildSpecificationRegion(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Region> spec = DynamicSpecifications.bySearchFilter(filters.values(), Region.class);
        return spec;
    }

    public List<Region> getAllRegion() {
        Sort sort = new Sort(Direction.ASC,"parent", "orderNo");
        Map<String, Object> searchParams = new HashMap<>();
        return regionDao.findAll(buildSpecificationRegion(searchParams), sort);
    }

}
