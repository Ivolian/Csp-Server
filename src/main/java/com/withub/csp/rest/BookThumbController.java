package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.BookThumb;
import com.withub.csp.entity.User;
import com.withub.csp.service.BookThumbService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/bookThumb")
public class BookThumbController extends BaseController {

    @Autowired
    private BookThumbService bookThumbService;

    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "bookId", defaultValue = "") String bookId) {

        return bookThumbService.create(userId, bookId);
    }

    @RequestMapping(value = "/listForMobile",method = RequestMethod.GET)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "bookId", defaultValue = "") String bookId) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_book.id", bookId);

        Page<BookThumb> page = bookThumbService.getBookThumb(searchParams, pageNo, pageSize);
        List<BookThumb> thumbList = page.getContent();
        JSONArray jsonArray = new JSONArray();
        for (BookThumb bookThumb : thumbList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("displayName", getDisplayName(bookThumb));
            jsonObject.put("eventTime", bookThumb.getEventTime());
            if (bookThumb.getUser()!=null) {
                jsonObject.put("avatar", bookThumb.getUser().getAvatar());
                jsonObject.put("userId", bookThumb.getUser().getId());
            }
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", page.isLastPage());
        response.put("totalPages", page.getTotalPages());
        return response;
    }


    // ======================= 简单方法 =======================

    private String getDisplayName(BookThumb thumb) {

        User user = thumb.getUser();
        if (user.getCourt() != null) {
            // 法院为空的情况，虽然不应该发生这种情况
            return thumb.getUser().getCourt().getName() + " " + thumb.getUser().getCnName();
        } else {
            return thumb.getUser().getCnName();
        }
    }

}
