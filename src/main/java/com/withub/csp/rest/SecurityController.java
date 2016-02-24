package com.withub.csp.rest;

import com.withub.csp.entity.RoleMenu;
import com.withub.csp.entity.SystemMenu;
import com.withub.csp.entity.User;
import com.withub.csp.service.RoleService;
import com.withub.csp.service.SystemMenuService;
import com.withub.csp.service.UserService;
import com.withub.service.account.ShiroDbRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/security")
@RestController
public class SecurityController {


    @Autowired
    private RoleService roleService;

    @Autowired
    private SystemMenuService systemMenuService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/getApplicationInfo", method = RequestMethod.GET)
    public Map getApplicationInfo() {

        Map<String, Object> result = new HashMap<String, Object>();
        ShiroDbRealm.ShiroUser shiroUser = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (shiroUser != null) {
            result.put("username", shiroUser.loginName);
            result.put("roleTag", userService.getUser(shiroUser.id).getRole().getTag());

            SystemMenu root = systemMenuService.getRootMenu();
            User user = userService.getUser(shiroUser.id);
            List<RoleMenu> roleMenuList = roleService.getRoleMenuList(user.getRole().getId());
            List<String> menuIdList = new ArrayList<>();
            for (RoleMenu roleMenu : roleMenuList) {
                menuIdList.add(roleMenu.getMenuId());
            }
            result.put("menuList", convertMenuData(root, menuIdList));
        }

        return result;
    }

    private List convertMenuData(SystemMenu menu, List<String> menuIdList) {
        List result = new ArrayList();
        if (!CollectionUtils.isEmpty(menu.getChildList())) {
            for (SystemMenu child : menu.getChildList()) {
                if (menuIdList.contains(child.getId())) {
                    Map data = new HashMap();

                    data.put("title", child.getName());
                    data.put("state", child.getUrl());
                    data.put("iconCls", child.getIcon());
                    data.put("children", convertMenuData(child, menuIdList));
                    result.add(data);
                }
            }
        }
        return result;
    }


}
