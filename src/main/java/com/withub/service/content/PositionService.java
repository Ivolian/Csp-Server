package com.withub.service.content;

import com.withub.entity.Position;
import com.withub.repository.PositionDao;
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

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class PositionService {
    private PositionDao positionDao;

    @Autowired
    public void setPositionDao(PositionDao positionDao) {
        this.positionDao = positionDao;
    }

    public Position getPosition(String id) {
        return positionDao.findOne(id);
    }

    public void savePosition(Position entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }
        positionDao.save(entity);
    }

    public void deletePosition(String id) {
        Position position= getPosition(id);
        position.setDeleteFlag(1);
        positionDao.save(position);
    }

    public Page<Position> getPosition(Map<String, Object> searchParams, int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.ASC, "name");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Position> spec = buildSpecificationPosition(searchParams);
        return positionDao.findAll(spec, pageRequest);
    }

    private Specification<Position> buildSpecificationPosition(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Position> spec = DynamicSpecifications.bySearchFilter(filters.values(), Position.class);
        return spec;
    }

    public List<Position> getAllPosition() {
        Sort sort = new Sort(Direction.ASC, "name");
        Map<String, Object> searchParams = new HashMap<>();
        return positionDao.findAll(buildSpecificationPosition(searchParams), sort);
    }
}
