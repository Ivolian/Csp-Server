package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.common.MD5Utils;
import com.withub.csp.entity.User;
import com.withub.csp.entity.UserLogin;
import com.withub.csp.repository.UserDao;
import com.withub.csp.repository.UserLoginDao;
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
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLoginDao userLoginDao;

    @Autowired
    private MenuService menuService;

    //

    public User getUser(String id) {
        return userDao.findOne(id);
    }

    public void saveUser(User entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setPassword("111111");
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);
        }
        entity.setPassword(MD5Utils.encryptByMD5(entity.getPassword()));
        userDao.save(entity);
    }

    public void updateUser(User user){
        userDao.save(user);
    }

    public void deleteUser(String id) {
        User user = getUser(id);
        user.setDeleteFlag(1);
        userDao.save(user);
    }

    public Page<User> getUser(Map<String, Object> searchParams, int pageNo, int pageSize) {

        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
        Specification<User> spec = buildSpecification(searchParams);
        return userDao.findAll(spec, pageRequest);
    }

    private Specification<User> buildSpecification(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), User.class);
    }

    // 登录
    public JSONObject loginCheck(String username, String password) {

        JSONObject item = new JSONObject();

        // 未找到用户
        User user = userDao.findOneByUsernameAndDeleteFlag(username, 0);
        boolean result = (user != null);
        if (!result) {
            item.put("result", false);
            return item;
        }

        // 密码错误
        result = user.getPassword().equals(MD5Utils.encryptByMD5(password));
        if (!result) {
            item.put("result", false);
            return item;
        }else {
            item.put("result", true);
            item.put("userId", user.getId());
            item.put("rootMenuItem", menuService.getRootMenuItem());

            // 添加登录记录
            UserLogin userLogin = new UserLogin();
            userLogin.setId(Identities.uuid());
            userLogin.setUser(user);
            userLogin.setEventTime(new Date());
            userLoginDao.save(userLogin);

            return item;
        }
    }

    // 修改密码
    public JSONObject changePassword(String userId, String oldPassword, String newPassword) {

        User user = userDao.findOne(userId);
        boolean result = (user != null);

        if (result) {
            result = (user.getDeleteFlag() == 0);
        }
        if (result) {
            result = user.getPassword().equals(MD5Utils.encryptByMD5(oldPassword));
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
