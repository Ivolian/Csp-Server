package com.withub.csp.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Comment;
import com.withub.csp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/comment")
public class CommentController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "newsId", defaultValue = "") String newsId,
            @RequestParam(value = "content", defaultValue = "") String content) {

        return commentService.create(userId, newsId, content);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<Comment> commentPage = commentService.getComment(searchParams, pageNo, pageSize, newsId);
        List<Comment> commentList = commentPage.getContent();

        List<Map> items = new ArrayList<>();
        for (Comment comment : commentList) {
            Map<String, Object> item = new HashMap<String, Object>();
            if (comment.getUser().getCourt() != null) {
                item.put("courtName", comment.getUser().getCourt().getCourtName());
            }
            item.put("username", comment.getUser().getCnName());
            item.put("eventtime", comment.getEventTime());
            item.put("content", comment.getContent());
            items.add(item);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", items);
        jsonObject.put("lastPage", commentPage.isLastPage());
        jsonObject.put("totalPages", commentPage.getTotalPages());
        return jsonObject;
    }

}
