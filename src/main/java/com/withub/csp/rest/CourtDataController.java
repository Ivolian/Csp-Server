package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.User;
import com.withub.csp.repository.CommentDao;
import com.withub.csp.repository.NewsReadDao;
import com.withub.csp.repository.ThumbDao;
import com.withub.csp.repository.UserLoginDao;
import com.withub.csp.service.CourtDataService;
import com.withub.csp.service.UserService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/courtData")
public class CourtDataController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ThumbDao thumbDao;

    @Autowired
    private UserLoginDao userLoginDao;

    @Autowired
    private NewsReadDao newsReadDao;

    @Autowired
    private CourtDataService courtDataService;


    @RequestMapping(method = RequestMethod.GET)
    public JSONObject list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "search_courtId", defaultValue = "") String courtId,
            @RequestParam(value = "search_cnName", defaultValue = "") String cnName,
            @RequestParam(value = "search_beginDate", defaultValue = "") String beginDateString,
            @RequestParam(value = "search_endDate", defaultValue = "") String endDateString,
            @RequestParam(value = "search_onlyNotLogin", defaultValue = "false") boolean onlyNotLogin
    ) throws Exception {






        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginDate = simpleDateFormat.parse(beginDateString);
        Date endDate = simpleDateFormat.parse(endDateString);

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("EQ_court.id", courtId);
        searchParams.put("LIKE_cnName", cnName);
        Page<User> page = userService.getUser(searchParams, pageNo, pageSize);
        List<User> userList = page.getContent();

        JSONObject response = new JSONObject();

        JSONArray content = new JSONArray();
        for (User user : userList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", user.getUsername());
            jsonObject.put("cnName", user.getCnName());
            jsonObject.put("courtName", user.getCourt().getName());
            jsonObject.put("commentCount", commentDao.getCommentCountByUserIdAndDateRange(user.getId(), beginDate, endDate));
            jsonObject.put("thumbCount", thumbDao.getThumbCountByUserIdAndDateRange(user.getId(), beginDate, endDate));
            int loginTimes = userLoginDao.getLoginTimesByUserIdAndDateRange(user.getId(), beginDate, endDate);
            jsonObject.put("loginTimes", loginTimes);
            jsonObject.put("readTimes", newsReadDao.getReadTimesByUserIdAndDateRange(user.getId(), beginDate, endDate));

            if (onlyNotLogin) {
                if (loginTimes == 0) {
                    content.add(jsonObject);
                }
            } else {
                content.add(jsonObject);
            }
        }

        response.put("content", content);
        response.put("firstPage", page.isFirstPage());
        response.put("lastPage", page.isLastPage());
        response.put("number", page.getNumber());
        response.put("numberOfElements", page.getNumberOfElements());
        response.put("size", page.getSize());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());

        return response;
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public JSONObject export(
            @RequestParam(value = "fileName") String fileName,
            @RequestParam(value = "courtId") String courtId,
            @RequestParam(value = "departmentId") String departmentId,
            @RequestParam(value = "beginTime") String beginTime,
            @RequestParam(value = "endTime") String endTime) throws Exception {

        JSONObject response = courtDataService.exportExcel(fileName, courtId,departmentId, beginTime,endTime);
        return response;
    }

}

