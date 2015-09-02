package com.withub.csp.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Comment;
import com.withub.csp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/commentList")
public class CommentListController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        commentService.deleteComment(id);
    }


    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Comment> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        return commentService.getComment(searchParams, pageNo, pageSize, null);
    }

}
