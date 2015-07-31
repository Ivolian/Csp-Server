package com.withub.rest;

import com.withub.entity.Position;
import com.withub.service.content.PositionService;
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
@RequestMapping(value = "/api/v1/position")
public class PositionRestController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PositionRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private PositionService positionService;
    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Position> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");

        Page<Position> position = positionService.getPosition(searchParams, pageNo, pageSize);
        return position;
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Position> list() {

        return positionService.getAllPosition();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Position get(@PathVariable("id") String id) {
        Position position = positionService.getPosition(id);
        if (position == null) {
            String message = "内容不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return position;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public ResponseEntity<?> create(@RequestBody Position position, UriComponentsBuilder uriBuilder) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, position);

        // 保存内容
        positionService.savePosition(position);

        // 按照Restful风格约定，创建指向新内容的url, 也可以直接返回id或对象.
        String id = position.getId();
        URI uri = uriBuilder.path("/api/v1/position/" + id).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Position position) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, position);

        // 保存内容
        positionService.savePosition(position);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        positionService.deletePosition(id);
    }

}
