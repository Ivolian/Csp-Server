package com.withub.web.controller;

import com.withub.entity.User;
import com.withub.service.account.ShiroDbRealm;
import org.apache.shiro.SecurityUtils;

public class BaseController {

    protected User getCurrentUser() {
        ShiroDbRealm.ShiroUser shiroUser = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        User user = new User();
        user.setId(shiroUser.id);
        user.setName(shiroUser.name);
        return user;
    }

}
