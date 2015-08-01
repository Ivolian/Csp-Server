package com.withub.web.controller;

import com.withub.entity.Content;
import com.withub.rest.RestException;
import com.withub.service.content.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

    private static Logger logger = LoggerFactory.getLogger(NewsController.class);

    private static final String PAGE_SIZE = "5";

    @Autowired
    private ContentService contentService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Content> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortType", defaultValue = "auto") String sortType,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        searchParams.put("EQ_publish", "1");
        Page<Content> news = contentService.getNews(searchParams, pageNo, pageSize,"","");
        return news;
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
}
