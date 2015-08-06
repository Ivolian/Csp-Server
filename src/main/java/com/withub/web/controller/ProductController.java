package com.withub.web.controller;

import com.withub.entity.Question;
import com.withub.rest.RestException;
import com.withub.service.content.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springside.modules.web.MediaTypes;

import javax.servlet.ServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Question get(@PathVariable("id") String id) {
        Question question = questionService.getQuestion(id);
        if (question == null) {
            String message = "产品不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return question;
    }
}
