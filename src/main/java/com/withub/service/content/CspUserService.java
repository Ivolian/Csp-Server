package com.withub.service.content;

import com.alibaba.fastjson.JSONObject;
import com.withub.entity.*;
import com.withub.repository.CspUserDao;
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
public class CspUserService {

    @Autowired
    private CspUserDao cspUserDao;

    public CspUser getEmail(String id) {
        return cspUserDao.findOne(id);
    }

    public void saveEmail(CspUser entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }
        cspUserDao.save(entity);
    }

    public void deleteEmail(String id) {
        CspUser cspUser = getEmail(id);
        cspUser.setDeleteFlag(1);
        cspUserDao.save(cspUser);
    }

    public Page<CspUser> getEmail(Map<String, Object> searchParams, int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.DESC, "defaultValue");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
        Specification<CspUser> spec = buildSpecificationEmail(searchParams);
        return cspUserDao.findAll(spec, pageRequest);
    }

    private Specification<CspUser> buildSpecificationEmail(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<CspUser> spec = DynamicSpecifications.bySearchFilter(filters.values(), CspUser.class);
        return spec;
    }

    public List<CspUser> getAllEmail() {
        Sort sort = new Sort(Direction.DESC, "defaultValue");
        Map<String, Object> searchParams = new HashMap<>();
        return cspUserDao.findAll(buildSpecificationEmail(searchParams), sort);
    }

    public JSONObject loginCheck(String username, String password) {

        CspUser cspUser = cspUserDao.findOneByUsernameAndPassword(username, password);
        JSONObject item = new JSONObject();
        item.put("result", cspUser != null);
        if (cspUser != null) {
            item.put("userId", cspUser.getId());
//            item.put("favoriteCount", cspUser.getFavoriteCount());
        }
        return item;
    }

}
