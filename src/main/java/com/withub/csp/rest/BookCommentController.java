package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.BookComment;
import com.withub.csp.entity.User;
import com.withub.csp.service.BookCommentService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/bookComment")
public class BookCommentController extends BaseController {


    @Autowired
    private BookCommentService bookCommentService;


    //

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public JSONObject create(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "bookId") String bookId,
            @RequestParam(value = "content") String content) {

        return bookCommentService.create(userId, bookId, content);
    }

//
//    // 后台列表查询
//    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
//    public Page<BookComment> list(
//            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
//            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
//            @RequestParam(value = "search_content", defaultValue = "") String content,
//            @RequestParam(value = "search_cnName", defaultValue = "") String cnName) {
//
//        Map<String, Object> searchParams = new HashMap<>();
//        searchParams.put("LIKE_content", content);
//        searchParams.put("LIKE_user.cnName", cnName);
//
//        return bookCommentService.getBookComment(searchParams, pageNo, pageSize);
//    }


    @RequestMapping(value = "/listForMobile",method = RequestMethod.GET)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "bookId", defaultValue = "") String bookId) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_book.id", bookId);

        Page<BookComment> bookCommentPage = bookCommentService.getBookComment(searchParams, pageNo, pageSize);
        List<BookComment> commentList = bookCommentPage.getContent();
        JSONArray jsonArray = new JSONArray();
        for (BookComment comment : commentList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("displayName", getDisplayName(comment));
            jsonObject.put("eventtime", comment.getEventTime());
            jsonObject.put("content", comment.getContent());
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", bookCommentPage.isLastPage());
        response.put("totalPages", bookCommentPage.getTotalPages());
        return response;
    }


    //

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        bookCommentService.deleteBookComment(id);
    }


    // ======================= 简单方法 =======================

    private String getDisplayName(BookComment comment) {

        User user = comment.getUser();
        if (user.getCourt() != null) {
            // 法院为空的情况，虽然不应该发生这种情况
            return comment.getUser().getCourt().getName() + " " + comment.getUser().getCnName();
        } else {
            return comment.getUser().getCnName();
        }
    }


}
