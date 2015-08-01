package com.withub.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.withub.entity.Comment;
import com.withub.service.content.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/comment")
public class CommentController {

    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private CommentService commentService;
    @Autowired
    private Validator validator;

//    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
//    public void create(@RequestBody Comment comment, HttpServletRequest request) {
//        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
//        BeanValidators.validateWithException(validator, comment);
//
//        String capText = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        String captchaText = request.getParameter("captchaText");
//        if (capText.equalsIgnoreCase(captchaText)) {
//            // 保存内容
//            commentService.saveComment(comment);
//        } else {
//            throw new RestException(HttpStatus.OK, "验证码错误");
//        }
//    }


    // 添加一条评论
    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "contentId", defaultValue = "") String contentId,
            @RequestParam(value = "words", defaultValue = "") String words,
            ServletRequest request) {

        return commentService.create(userId, contentId, words);
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
            Map<String, Object> item = new HashMap<>();
            item.put("username", comment.getUser().getUsername());
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
