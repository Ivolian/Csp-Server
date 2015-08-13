package com.withub.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.User;
import com.withub.service.content.CspUserService;
import com.withub.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/user")
public class CspUserRestController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CspUserRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private CspUserService cspUserService;

    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<User> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");
        Page<User> email = cspUserService.getEmail(searchParams, pageNo, pageSize);
        return email;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<User> list() {

        return cspUserService.getAllUser();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public User get(@PathVariable("id") String id) {
        User user = cspUserService.getUser(id);
        if (user == null) {
            String message = "用户不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return user;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody User user) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, user);

        // 保存内容
        cspUserService.saveUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, user);

        // 保存内容
        cspUserService.saveUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        cspUserService.deleteUser(id);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {

        return cspUserService.loginCheck(username, password);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject changePassword(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "oldPassword", defaultValue = "") String oldPassword,
            @RequestParam(value = "newPassword", defaultValue = "") String newPassword) {

        return cspUserService.changePassword(userId, oldPassword, newPassword);
    }

}
