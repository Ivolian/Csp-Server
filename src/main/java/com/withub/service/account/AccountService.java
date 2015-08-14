/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.withub.service.account;

import com.withub.entity.SSUser;
import com.withub.repository.SSUserDao;
import com.withub.service.ServiceException;
import com.withub.service.account.ShiroDbRealm.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Clock;
import org.springside.modules.utils.Encodes;

import java.util.List;

/**
 * 用户管理类.
 *
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class AccountService {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    private SSUserDao SSUserDao;
    private Clock clock = Clock.DEFAULT;

    public List<SSUser> getAllUser() {
        return (List<SSUser>) SSUserDao.findAll();
    }

    public SSUser getUser(Long id) {
        return SSUserDao.findOne(id);
    }

    public SSUser findUserByLoginName(String loginName) {
        return SSUserDao.findByLoginName(loginName);
    }

    public void registerUser(SSUser SSUser) {
        entryptPassword(SSUser);
        SSUser.setRoles("user");
        SSUser.setRegisterDate(clock.getCurrentDate());

        SSUserDao.save(SSUser);
    }

    public void updateUser(SSUser SSUser) {
        if (StringUtils.isNotBlank(SSUser.getPlainPassword())) {
            entryptPassword(SSUser);
        }
        SSUserDao.save(SSUser);
    }

    public void deleteUser(Long id) {
        if (isSupervisor(id)) {
            logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
            throw new ServiceException("不能删除超级管理员用户");
        }
        SSUserDao.delete(id);
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }

    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.loginName;
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(SSUser SSUser) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        SSUser.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(SSUser.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        SSUser.setPassword(Encodes.encodeHex(hashPassword));
    }

    @Autowired
    public void setSSUserDao(SSUserDao SSUserDao) {
        this.SSUserDao = SSUserDao;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
