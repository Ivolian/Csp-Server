package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Answer;
import com.withub.csp.entity.User;
import com.withub.csp.service.AnswerService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/answer")
public class AnswerController extends BaseController {


    @Autowired
    private AnswerService answerService;


    // ======================= Methods =======================

    // 后台列表查询
    @RequestMapping(method = RequestMethod.GET)
    public Page<Answer> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "search_content", defaultValue = "") String content,
            @RequestParam(value = "search_cnName", defaultValue = "") String cnName) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_content", content);
        searchParams.put("LIKE_user.cnName", cnName);

        return answerService.getAnswer(searchParams, pageNo, pageSize);
    }


    // 后台删除
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        answerService.deleteAnswer(id);
    }


    // 手机端查询列表
    @RequestMapping(value = "/listForMobile", method = RequestMethod.GET)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "questionId", defaultValue = PAGE_SIZE) String questionId) {

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("EQ_question.id", questionId);

        Page<Answer> answerPage = answerService.getAnswer(searchParams, pageNo, pageSize);
        List<Answer> answerList = answerPage.getContent();

        JSONArray jsonArray = new JSONArray();
        for (Answer answer : answerList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content", answer.getContent());
            jsonObject.put("displayName", getDisplayName(answer));
            jsonObject.put("eventTime", answer.getEventTime());
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", answerPage.isLastPage());
        response.put("totalPages", answerPage.getTotalPages());
        return response;
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public JSONObject create(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "questionId") String questionId,
            @RequestParam(value = "content") String content) {

        return answerService.create(userId, questionId, content);
    }


    // ======================= 简单方法 =======================

    private String getDisplayName(Answer answer) {

        User user = answer.getUser();
        if (user.getCourt() != null) {
            // 法院为空的情况，虽然不应该发生这种情况
            return answer.getUser().getCourt().getName() + " " + answer.getUser().getCnName();
        } else {
            return answer.getUser().getCnName();
        }
    }

}
