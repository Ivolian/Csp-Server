package com.withub.web.controller;


import com.withub.csp.entity.Answer;
import com.withub.csp.entity.Menu;
import com.withub.rest.RestException;
import com.withub.service.content.AnswerService;
import com.withub.service.content.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springside.modules.web.MediaTypes;

import java.util.List;

@Controller
@RequestMapping(value = "/job")
public class JobController {

    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private AnswerService answerService;

    @Autowired
    private MenuService menuService;



    @RequestMapping(value = "/region", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Menu> regionList() {

        List<Menu> menuList = menuService.getMenuByParentId(menuService.getRootMenu().getId());

        return menuList;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Answer get(@PathVariable("id") String id) {
        Answer answer = answerService.getAnswer(id);
        if (answer == null) {
            String message = "内容不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return answer;
    }
}
