package com.withub.service.content;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.User;
import com.withub.repository.CspUserDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private MenuService menuService;

    public User getUser(String id) {
        return cspUserDao.findOne(id);
    }

    public void saveUser(User entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }
        cspUserDao.save(entity);
    }

    public void deleteUser(String id) {
        User user = getUser(id);
        user.setDeleteFlag(1);
        cspUserDao.save(user);
    }

    public Page<User> getEmail(Map<String, Object> searchParams, int pageNo, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
        Specification<User> spec = buildSpecificationEmail(searchParams);
        return cspUserDao.findAll(spec, pageRequest);
    }

    private Specification<User> buildSpecificationEmail(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
        return spec;
    }

    public List<User> getAllUser() {
        Map<String, Object> searchParams = new HashMap();
        return cspUserDao.findAll(buildSpecificationEmail(searchParams));
    }

    public JSONObject loginCheck(String username, String password) {

        User user = cspUserDao.findOneByUsernameAndPasswordAndDeleteFlag(username, password, 0);
        boolean result = user != null;

        JSONObject item = new JSONObject();
        item.put("result", result);
        if (result) {
            item.put("userId", user.getId());
            item.put("rootMenuItem", menuService.getRootMenuItem());
//            item.put("favoriteCount", cspUser.getFavoriteCount());
        }
        return item;
    }

    public JSONObject changePassword(String userId, String oldPassword, String newPassword) {

        User user = cspUserDao.findOne(userId);
        boolean result = user != null;
        if (result) {
            result = (user.getDeleteFlag() == 0);
        }
        if (result) {
            result = user.getPassword().equals(oldPassword);
        }
        if (result) {
            user.setPassword(newPassword);
            saveUser(user);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

}
