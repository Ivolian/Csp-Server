package com.withub.rest;

import com.withub.entity.Job;
import com.withub.service.content.ContentService;
import com.withub.service.content.JobService;
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
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/job")
public class JobRestController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(JobRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private JobService jobService;
    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Job> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");

        Page<Job> job = jobService.getJob(searchParams, pageNo, pageSize);
        return job;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Job get(@PathVariable("id") String id) {
        Job job = jobService.getJob(id);
        if (job == null) {
            String message = "内容不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return job;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public ResponseEntity<?> create(@RequestBody Job job, UriComponentsBuilder uriBuilder) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, job);

        // 保存内容
        jobService.saveJob(job);

        // 按照Restful风格约定，创建指向新内容的url, 也可以直接返回id或对象.
        String id = job.getId();
        URI uri = uriBuilder.path("/api/v1/job/" + id).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Job job) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, job);

        // 保存内容
        jobService.saveJob(job);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        jobService.deleteJob(id);
    }

    @RequestMapping(value = "/{id}/publish/{value}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void publish(@PathVariable("id") String id, @PathVariable("value") Integer value) {

        Job job = jobService.getJob(id);
        job.setPublish(value);
        jobService.saveJob(job);
    }

}
