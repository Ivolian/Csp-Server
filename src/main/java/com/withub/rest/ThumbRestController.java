package com.withub.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.service.content.ThumbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import javax.validation.Validator;

@RestController
@RequestMapping(value = "/api/v1/thumb")
public class ThumbRestController {

    private static Logger logger = LoggerFactory.getLogger(ThumbRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private ThumbService thumbService;
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
//            thumbService.saveComment(comment);
//        } else {
//            throw new RestException(HttpStatus.OK, "验证码错误");
//        }
//    }


    // 添加一条评论
    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        return thumbService.create(userId, newsId);
    }

}
