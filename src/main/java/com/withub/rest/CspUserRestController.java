package com.withub.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.entity.CspUser;
import com.withub.service.content.CspUserService;
import com.withub.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import javax.validation.Validator;
import java.net.URI;
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
    public Page<CspUser> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");

        Page<CspUser> email = cspUserService.getEmail(searchParams, pageNo, pageSize);
        return email;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<CspUser> list() {

        return cspUserService.getAllEmail();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public CspUser get(@PathVariable("id") String id) {
        CspUser cspUser = cspUserService.getEmail(id);
        if (cspUser == null) {
            String message = "内容不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return cspUser;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public ResponseEntity<?> create(@RequestBody CspUser cspUser, UriComponentsBuilder uriBuilder) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, cspUser);

        // 保存内容
        cspUserService.saveEmail(cspUser);

        // 按照Restful风格约定，创建指向新内容的url, 也可以直接返回id或对象.
        String id = cspUser.getId();
        URI uri = uriBuilder.path("/api/v1/email/" + id).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody CspUser cspUser) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, cspUser);

        // 保存内容
        cspUserService.saveEmail(cspUser);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        cspUserService.deleteEmail(id);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {

        return cspUserService.loginCheck(username, password);
    }

}
