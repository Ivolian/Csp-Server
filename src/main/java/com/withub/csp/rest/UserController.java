package com.withub.csp.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.User;
import com.withub.csp.service.UserService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController extends BaseController {


    @Autowired
    private UserService userService;


    //

    // 后台列表查询
    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<User> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "search_cnName", defaultValue = "") String cnName,
            @RequestParam(value = "search_username", defaultValue = "") String username,
            @RequestParam(value = "search_courtId", defaultValue = "") String courtId) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_cnName", cnName);
        searchParams.put("LIKE_username", username);
        searchParams.put("EQ_court.id", courtId);

        return userService.getUser(searchParams, pageNo, pageSize);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody User user) throws Exception {

        userService.saveUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody User user) {

        userService.updateUser(user);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public void resetPassword(String userId)throws Exception{

        userService.resetPassword(userId);
    }


    // 登录
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public JSONObject login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {

        return userService.loginCheck(username, password);
    }

    // 修改密码
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public JSONObject changePassword(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "oldPassword", defaultValue = "") String oldPassword,
            @RequestParam(value = "newPassword", defaultValue = "") String newPassword) {

        return userService.changePassword(userId, oldPassword, newPassword);
    }


    // 基本无视的方法

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User get(@PathVariable("id") String id) {

        return userService.getUser(id);
    }


}
