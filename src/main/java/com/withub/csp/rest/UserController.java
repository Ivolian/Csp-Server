package com.withub.csp.rest;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.User;
import com.withub.csp.service.UserService;
import com.withub.web.controller.BaseController;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springside.modules.web.MediaTypes;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    // 后台列表查询
    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "search_cnName", defaultValue = "") String cnName,
            @RequestParam(value = "search_username", defaultValue = "") String username,
            @RequestParam(value = "search_courtId", defaultValue = "") String courtId,
            @RequestParam(value = "search_department", defaultValue = "") String department,
            @RequestParam(value = "search_justOnline") Boolean justOnline

    ) {

        Map<String, Object> searchParams = new HashMap<String, Object>();
        JSONObject jsonObjectDepartment = JSONObject.parseObject(department);
        if (jsonObjectDepartment != null) {
            String departmentId = jsonObjectDepartment.getString("id");
            searchParams.put("EQ_department.id", departmentId);
        }
        searchParams.put("LIKE_cnName", cnName);
        searchParams.put("LIKE_username", username);
        searchParams.put("EQ_court.id", courtId);

        Date now = new Date();
        int interval = 60;
        Date time = DateUtils.addSeconds(now, -interval * 2);
        if (justOnline) {
            searchParams.put("GT_heartbeat", time);
        }

        JSONObject result = new JSONObject();
        Page<User> userPage = userService.getUser(searchParams, pageNo, pageSize);
        List<User> userList = userPage.getContent();
        JSONArray content = new JSONArray();
        for (User user : userList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user.getId());
            jsonObject.put("username", user.getUsername());
            jsonObject.put("cnName", user.getCnName());
            if (user.getCourt() != null) {
                jsonObject.put("courtName", user.getCourt().getName());
            }
            if (user.getDepartment() != null) {
                jsonObject.put("departmentName", user.getDepartment().getName());
            }
            jsonObject.put("position", user.getPosition());
            if (user.getRole() != null) {
                jsonObject.put("roleName", user.getRole().getName());
            }
            jsonObject.put("telephone", user.getTelephone());
            jsonObject.put("enable", user.getEnable());
            if (user.getHeartbeat() != null) {
                jsonObject.put("online", user.getHeartbeat().after(time) ? "在线" : "离线");
            }
            jsonObject.put("currentVersionName", user.getCurrentVersionName());
            content.add(jsonObject);
        }
        result.put("content", content);
        result.put("totalElements", userPage.getTotalElements());
        result.put("totalPages", userPage.getTotalPages());
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody User user) throws Exception {

        userService.saveUser(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody User user) {

        userService.updateUser(user);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public void resetPassword(String userId) throws Exception {
        userService.resetPassword(userId);
    }

    @RequestMapping(value = "/personalInfo", method = RequestMethod.GET)
    public JSONObject getPersonalInfo(String userId) throws Exception {

        return userService.getPersonalInfo(userId);
    }

    public static ConcurrentHashMap<String, Date> heartbeatMap = new ConcurrentHashMap<>(9000, 0.75f);

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    public void heartbeat(@RequestParam(value = "userId") String userId) {

        heartbeatMap.put(userId, new Date());
    }


    // 登录
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public JSONObject login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "currentVersionName", defaultValue = "") String currentVersionName
    ) {

        return userService.loginCheck(username, password, currentVersionName);
    }

    // 修改密码
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public JSONObject changePassword(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "oldPassword", defaultValue = "") String oldPassword,
            @RequestParam(value = "newPassword", defaultValue = "") String newPassword) {

        return userService.changePassword(userId, oldPassword, newPassword);
    }

    // 更新 pushTag
    @RequestMapping(value = "/updatePushTag", method = RequestMethod.GET)
    public void updatePushTag(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "pushTag", defaultValue = "") String pushTag
    ) {

        userService.updatePushTag(userId, pushTag);
    }


    // 启用或停用
    @RequestMapping(value = "/enableOrDisable", method = RequestMethod.GET)
    public void enableOrDisable(
            @RequestParam(value = "userId", defaultValue = "") String userId
    ) {

        userService.enableOrDisable(userId);
    }


    // 基本无视的方法

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User get(@PathVariable("id") String id) {

        return userService.getUser(id);
    }

    //

    @RequestMapping(value = "/pushUpdate", method = RequestMethod.GET)
    public void pushUpdate(
            @RequestParam(value = "userId") String userId) {

        String pushTag = userId.replace("-", "_");
        JPushClient jPushClient = new JPushClient("4a403a6df5b37fe29b3b68f1", "0c6d82e59bc4a8b85eaa05c8", 3);

        PushPayload pushPayload = PushPayload.newBuilder().setPlatform(Platform.android())
                .setAudience(Audience.tag(pushTag))
                .setNotification(Notification.alert("检测到新的版本"))
                .build();
        try {
            jPushClient.sendPush(pushPayload);
        } catch (Exception e) {
            //
        }
    }

    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    public void upload(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "avatar") CommonsMultipartFile avatar) throws Exception {

        userService.setUserAvatar(userId, avatar);
    }

}
