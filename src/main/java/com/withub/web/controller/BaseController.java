package com.withub.web.controller;

import com.withub.entity.SSUser;
import com.withub.service.account.ShiroDbRealm;
import org.apache.shiro.SecurityUtils;

public class BaseController {

    protected SSUser getCurrentUser() {
        ShiroDbRealm.ShiroUser shiroUser = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        SSUser SSUser = new SSUser();
        SSUser.setId(shiroUser.id);
        SSUser.setName(shiroUser.name);
        return SSUser;
    }

}
