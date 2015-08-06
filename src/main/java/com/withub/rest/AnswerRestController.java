package com.withub.rest;

import com.withub.entity.Answer;
import com.withub.service.content.AnswerService;
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
@RequestMapping(value = "/api/v1/answer")
public class AnswerRestController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(AnswerRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private AnswerService answerService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Answer> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");

        Page<Answer> answer = answerService.getAnswer(searchParams, pageNo, pageSize);
        return answer;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Answer get(@PathVariable("id") String id) {
        Answer answer = answerService.getAnswer(id);
        if (answer == null) {
            String message = "回答不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return answer;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody Answer answer, UriComponentsBuilder uriBuilder) {

        answerService.saveAnswer(answer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Answer answer) {

        answerService.saveAnswer(answer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {

        answerService.deleteAnswer(id);
    }

}
