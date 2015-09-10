package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.common.MD5Utils;
import com.withub.csp.entity.User;
import com.withub.csp.entity.UserLogin;
import com.withub.csp.repository.UserDao;
import com.withub.csp.repository.UserLoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.util.Date;
import java.util.Map;


@Component
@Transactional
public class UserService extends BaseService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLoginDao userLoginDao;

    @Autowired
    private MenuService menuService;

    //

    public void saveUser(User user) {

        User oldUser = userDao.findOneByUsernameAndDeleteFlag(user.getUsername(), 0);
        if (oldUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        initEntity(user);

        String telephone = user.getTelephone();
        String password = telephone.substring(5, 11);
        user.setPassword(password);
        encryptAndSave(user);
    }

    public void resetPassword(String userId) throws Exception {

        User user = getUser(userId);
        String telephone = user.getTelephone();
        if (telephone == null || telephone.equals("")) {
            throw new Exception("手机号是空的");
        }

        String password = telephone.substring(5, 11);
        user.setPassword(password);
        encryptAndSave(user);
    }

    public void updateUser(User user) {

        User old = userDao.isUserExist(user.getUsername(), user.getId());
        if (old != null) {
            throw new RuntimeException("用户名已存在");
        }
        userDao.save(user);
    }

    public Page<User> getUser(Map<String, Object> searchParams, int pageNo, int pageSize) {

        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
        Specification<User> spec = buildSpecification(searchParams);
        return userDao.findAll(spec, pageRequest);
    }


    // 登录
    public JSONObject loginCheck(String username, String password) {

        JSONObject result = new JSONObject();

        User user = userDao.findOneByUsernameAndDeleteFlag(username, 0);
        if (user == null) {
            result.put("errorMsg", "用户不存在");
            result.put("result", false);
            return result;
        }

        if (!user.getPassword().equals(MD5Utils.encryptByMD5(password))) {
            result.put("result", false);
            result.put("errorMsg", "密码错误");
            return result;
        }

        // 登录成功
        result.put("result", true);
        result.put("userId", user.getId());
        result.put("rootMenuItem", menuService.getRootMenuItem());

        // 添加登录记录
        UserLogin userLogin = new UserLogin();
        userLogin.setId(Identities.uuid());
        userLogin.setUser(user);
        userLogin.setEventTime(new Date());
        userLoginDao.save(userLogin);

        return result;
    }


    // 修改密码
    public JSONObject changePassword(String userId, String oldPassword, String newPassword) {

        JSONObject result = new JSONObject();

        User user = userDao.findOneByIdAndDeleteFlag(userId, 0);
        if (user == null) {
            result.put("result", false);
            result.put("errorMsg", "用户不存在");
            return result;
        }

        if (!user.getPassword().equals(MD5Utils.encryptByMD5(oldPassword))) {
            result.put("result", false);
            result.put("errorMsg", "旧密码错误");
            return result;
        }

        user.setPassword(newPassword);
        encryptAndSave(user);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "true");
        return jsonObject;
    }


    // 基本无视的方法
    public User getUser(String id) {
        return userDao.findOne(id);
    }

    public void deleteUser(String id) {
        User user = getUser(id);
        user.setDeleteFlag(1);
        userDao.save(user);
    }

    private Specification<User> buildSpecification(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), User.class);
    }


    // 简单方法
    private void encryptAndSave(User user) {

        String ePassword = MD5Utils.encryptByMD5(user.getPassword());
        user.setPassword(ePassword);
        userDao.save(user);
    }

}
