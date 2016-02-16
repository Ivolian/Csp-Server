package com.withub.csp.service;

import com.withub.csp.entity.SystemMenu;
import com.withub.csp.repository.SystemMenuDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class SystemMenuService  extends BaseService{

    @Autowired
    private SystemMenuDao menuDao;

    public SystemMenu getRootMenu() {
        return menuDao.findByParent(null).get(0);
    }

    public SystemMenu getSystemMenu(String objectId) {
        return menuDao.findOne(objectId);
    }


    public void saveSystemMenu(SystemMenu menu) {

        if (StringUtils.isEmpty(menu.getId())) {
            initEntity(menu);
        }
        menuDao.save(menu);
    }

    public void deleteMenu(String objectId) {
        SystemMenu menu = getSystemMenu(objectId);
        menu.setDeleteFlag(1);
        menuDao.save(menu);
    }


}
