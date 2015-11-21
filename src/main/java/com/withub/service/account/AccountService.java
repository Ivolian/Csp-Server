/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.withub.service.account;

import com.withub.csp.entity.User;
import com.withub.csp.repository.UserDao;
import com.withub.service.ServiceException;
import com.withub.service.account.ShiroDbRealm.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Clock;

/**
 * 用户管理类.
 *
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class AccountService {

    public static final String HASH_ALGORITHM = "MD5";
    public static final int HASH_INTERATIONS = 1;
    private static final int SALT_SIZE = 8;

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    private UserDao userDao;
    private Clock clock = Clock.DEFAULT;

    public User getUser(String id) {
        return userDao.findOne(id);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public void deleteUser(String id) {
        if (isSupervisor(id)) {
            logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
            throw new ServiceException("不能删除超级管理员用户");
        }
        userDao.delete(id);
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(String id) {
        return true; // todo id == 1;
    }

    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.loginName;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
