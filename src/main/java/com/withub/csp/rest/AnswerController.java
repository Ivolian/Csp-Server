package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Answer;
import com.withub.csp.service.AnswerService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/answer")
public class AnswerController extends BaseController{

    private static final String PAGE_SIZE = "10";

    @Autowired
    private AnswerService answerService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Answer> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "questionId", defaultValue = "") String questionId,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");
        Page<Answer> answer = answerService.getAnswer(searchParams, pageNo, pageSize,questionId);
        return answer;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject listForMobile(
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
            jsonObject.put("username", answer.getUser().getCourt().getCourtName() + " " +answer.getUser().getCnName());
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

}
