package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Question;
import com.withub.csp.entity.User;
import com.withub.csp.service.QuestionService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/question")
public class QuestionController extends BaseController {


    @Autowired
    private QuestionService questionService;


    // ======================= Methods =======================

    // 后台列表查询
    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Question> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "search_content", defaultValue = "") String content,
            @RequestParam(value = "search_cnName", defaultValue = "") String cnName) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_content", content);
        searchParams.put("LIKE_user.cnName", cnName);

        return questionService.getQuestion(searchParams, pageNo, pageSize);
    }


    // 后台删除
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        questionService.deleteQuestion(id);
    }


    // 手机端查询列表
    @RequestMapping(value = "/listForMobile", method = RequestMethod.GET)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "type", defaultValue = "有问有答") String type) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_type", type);

        Page<Question> questionPage = questionService.getQuestion(searchParams, pageNo, pageSize);
        List<Question> questionList = questionPage.getContent();
        JSONArray jsonArray = new JSONArray();
        for (Question question : questionList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", question.getId());
            jsonObject.put("content", question.getContent());
            jsonObject.put("displayName", getDisplayName(question));
            jsonObject.put("eventTime", question.getEventTime().getTime());
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
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "content") String content) {

        return questionService.create(userId, type,content);
    }


    // ======================= 简单方法 =======================

    private String getDisplayName(Question question) {

        User user = question.getUser();
        if (user.getCourt() != null) {
            // 法院为空的情况，虽然不应该发生这种情况
            return question.getUser().getCourt().getName() + " " + question.getUser().getCnName();
        } else {
            return question.getUser().getCnName();
        }
    }

}
