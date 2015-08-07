package com.withub.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.entity.Answer;
import com.withub.entity.Content;
import com.withub.entity.Question;
import com.withub.service.content.QuestionService;
import com.withub.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/question")
public class QuestionRestController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(QuestionRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Question> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");
        Page<Question> product = questionService.getQuestion(searchParams, pageNo, pageSize);
        return product;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list2(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<Question> questionPage = questionService.getQuestion(searchParams, pageNo, pageSize);
        List<Question> questionList = questionPage.getContent();

        JSONArray jsonArray = new JSONArray();
        for (Question question : questionList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content", question.getContent());
            jsonObject.put("username", question.getUser().getUsername());
            jsonObject.put("eventTime", question.getEventTime().getTime());
            jsonObject.put("id", question.getId());
            JSONArray answerJSONArray = new JSONArray();
            for (Answer answer : question.getAnswerList()) {
                JSONObject answerJSONObject = new JSONObject();
                answerJSONObject.put("content", answer.getContent());
                answerJSONObject.put("username", answer.getUser().getUsername());
                answerJSONObject.put("eventTime", answer.getEventTime());
                answerJSONObject.put("id", answer.getId());
                answerJSONArray.add(answerJSONObject);
                int index = question.getAnswerList().indexOf(answer);
                if (index == 4) {
                    break;
                }
            }

            jsonObject.put("answerList", answerJSONArray);
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", questionPage.isLastPage());
        response.put("totalPages", questionPage.getTotalPages());
        return response;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Question get(@PathVariable("id") String id) {
        Question question = questionService.getQuestion(id);
        if (question == null) {
            String message = "提问不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return question;
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "content", defaultValue = "") String content) {

        return questionService.create(userId, content);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Question question) {

        questionService.saveQuestion(question);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        questionService.deleteQuestion(id);
    }

}
