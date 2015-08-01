/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.withub.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.entity.Content;
import com.withub.entity.Job;
import com.withub.service.content.ContentService;
import com.withub.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.print.DocFlavor;
import javax.servlet.ServletRequest;
import javax.validation.Validator;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsRestController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(NewsRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private ContentService contentService;

    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Content> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortType", defaultValue = "auto") String sortType,
            @RequestParam(value = "regionId", defaultValue = "") String regionId,
            @RequestParam(value = "title", defaultValue = "") String title,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Page<Content> newsPage = contentService.getNews(searchParams, pageNo, pageSize, regionId, title);
        return newsPage;
    }

    // 多写了个查询，给手机端用，为了不传递 contentData 减少流量


    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list2(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "menuId", defaultValue = "") String menuId,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<Content> newsPage = contentService.getNews(searchParams, pageNo, pageSize, menuId, keyword);
        List<Content> contentList = newsPage.getContent();

        JSONArray jsonArray = new JSONArray();
        for (Content content : contentList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", content.getId());
            jsonObject.put("title", content.getTitle());
            jsonObject.put("picture", content.getPicture());
            jsonObject.put("commentCount", content.getCommentList().size());
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", newsPage.isLastPage());
        response.put("totalPages", newsPage.getTotalPages());
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Content get(@PathVariable("id") String id) {
        Content content = contentService.getContent(id);
        if (content == null) {
            String message = "内容不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return content;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody Content content, UriComponentsBuilder uriBuilder) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, content);

        // 保存内容
//        content.setUser(getCurrentUser());
        content.setContentColumnId("101");
        contentService.saveContent(content);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Content content) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, content);

        // 保存内容
        contentService.saveContent(content);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        contentService.deleteContent(id);
    }


    // 获取新闻内容
    @RequestMapping(value = "/contentData", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public String getContentDate(
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        return contentService.getContent(newsId).getContentData().getData();
    }

    // 可以删掉的，有有关发布的东西
    @RequestMapping(value = "/{id}/publish/{value}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void publish(@PathVariable("id") String id, @PathVariable("value") Integer value) {

        Content content = contentService.getContent(id);
        content.setPublish(value);
        contentService.saveContent(content);
    }

}
