package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Answer;
import com.withub.csp.entity.Book;
import com.withub.csp.entity.FavoriteBook;
import com.withub.csp.entity.Question;
import com.withub.csp.service.FavoriteBookService;
import com.withub.csp.service.QuestionService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/question")
public class QuestionController extends BaseController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Question> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");
        return questionService.getQuestion(searchParams, pageNo, pageSize);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<Question> questionPage = questionService.getQuestion(searchParams, pageNo, pageSize);
        List<Question> questionList = questionPage.getContent();

        JSONArray jsonArray = new JSONArray();
        for (Question question : questionList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", question.getId());
            jsonObject.put("content", question.getContent());
            jsonObject.put("username", question.getUser().getCourt().getCourtName()+ " " + question.getUser().getCnName());
            jsonObject.put("eventTime", question.getEventTime().getTime());
//            JSONArray answerJSONArray = new JSONArray();
//            for (Answer answer : question.getAnswerList()) {
//                JSONObject answerJSONObject = new JSONObject();
//                answerJSONObject.put("id", answer.getId());
//                answerJSONObject.put("content", answer.getContent());
//                answerJSONObject.put("username", answer.getUser().getUsername());
//                answerJSONObject.put("eventTime", answer.getEventTime());
//                answerJSONArray.add(answerJSONObject);
//                int index = question.getAnswerList().indexOf(answer);
//                // 最多展示 5 条记录
//                if (index == 4) {
//                    break;
//                }
//            }
//
//            jsonObject.put("answerList", answerJSONArray);
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", questionPage.isLastPage());
        response.put("totalPages", questionPage.getTotalPages());
        return response;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "content", defaultValue = "") String content) {

        return questionService.create(userId, content);
    }


}