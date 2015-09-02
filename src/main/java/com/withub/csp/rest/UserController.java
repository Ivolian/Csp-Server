package com.withub.csp.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.User;
import com.withub.csp.service.UserService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController extends BaseController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<User> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");
        return userService.getUser(searchParams, pageNo, pageSize);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public User get(@PathVariable("id") String id) {

        return userService.getUser(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody User user) {

        userService.saveUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    public void update(@RequestBody User user) {

        userService.updateUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }


    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public void resetPassword(String userId) {

        User user = userService.getUser(userId);
        user.setPassword("111111");
        userService.saveUser(user);
    }


    // 登录
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {

        return userService.loginCheck(username, password);
    }

    // 修改密码
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject changePassword(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "oldPassword", defaultValue = "") String oldPassword,
            @RequestParam(value = "newPassword", defaultValue = "") String newPassword) {

        return userService.changePassword(userId, oldPassword, newPassword);
    }

}
