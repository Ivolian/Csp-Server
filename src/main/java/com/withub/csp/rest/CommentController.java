package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Comment;
import com.withub.csp.entity.User;
import com.withub.csp.service.CommentService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/comment")
public class CommentController extends BaseController {


    @Autowired
    private CommentService commentService;


    //

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public JSONObject create(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "newsId") String newsId,
            @RequestParam(value = "content") String content) {

        return commentService.create(userId, newsId, content);
    }


    // 后台列表查询
    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Comment> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "search_content", defaultValue = "") String content,
            @RequestParam(value = "search_cnName", defaultValue = "") String cnName) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_content", content);
        searchParams.put("LIKE_user.cnName", cnName);

        return commentService.getComment(searchParams, pageNo, pageSize);
    }


    @RequestMapping(value = "/listForMobile",method = RequestMethod.GET)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_news.id", newsId);

        Page<Comment> commentPage = commentService.getComment(searchParams, pageNo, pageSize);
        List<Comment> commentList = commentPage.getContent();
        JSONArray jsonArray = new JSONArray();
        for (Comment comment : commentList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("displayName", getDisplayName(comment));
            jsonObject.put("eventTime", comment.getEventTime());
            jsonObject.put("content", comment.getContent());
            if (comment.getUser()!=null) {
                jsonObject.put("avatar", comment.getUser().getAvatar());
                jsonObject.put("userId", comment.getUser().getId());
            }
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", commentPage.isLastPage());
        response.put("totalPages", commentPage.getTotalPages());
        return response;
    }


    //

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        commentService.deleteComment(id);
    }


    // ======================= 简单方法 =======================

    private String getDisplayName(Comment comment) {

        User user = comment.getUser();
        if (user.getCourt() != null) {
            // 法院为空的情况，虽然不应该发生这种情况
            return comment.getUser().getCourt().getName() + " " + comment.getUser().getCnName();
        } else {
            return comment.getUser().getCnName();
        }
    }


}
