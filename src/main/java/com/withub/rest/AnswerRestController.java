package com.withub.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.entity.Answer;
import com.withub.entity.Content;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            @RequestParam(value = "questionId", defaultValue = PAGE_SIZE) String questionId,

            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");

        Page<Answer> answer = answerService.getAnswer(searchParams, pageNo, pageSize,questionId);
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

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list2(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "questionId", defaultValue = PAGE_SIZE) String questionId){

        Map<String, Object> searchParams = new HashMap<String,Object>();
        Page<Answer> answerPage = answerService.getAnswer(searchParams, pageNo, pageSize, questionId);
        List<Answer> answerList = answerPage.getContent();

        JSONArray jsonArray = new JSONArray();
        for (Answer answer :answerList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", answer.getId());
            jsonObject.put("content", answer.getContent());
            jsonObject.put("username", answer.getUser().getUsername());
            jsonObject.put("eventTime", answer.getEventTime());
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", answerPage.isLastPage());
        response.put("totalPages", answerPage.getTotalPages());
        return response;
    }



    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "questionId", defaultValue = "") String questionId,
            @RequestParam(value = "content", defaultValue = "") String content) {

        return answerService.create(userId, questionId,content);
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
