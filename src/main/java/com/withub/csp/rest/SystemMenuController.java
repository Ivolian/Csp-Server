package com.withub.csp.rest;

import com.withub.csp.entity.SystemMenu;
import com.withub.csp.service.SystemMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/systemMenu")
public class SystemMenuController {

    @Autowired
    private SystemMenuService systemMenuService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SystemMenu get(@PathVariable("id") String id) {
        return systemMenuService.getSystemMenu(id);
    }

    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<SystemMenu> getTree(@PathVariable("id") String id) {
        List<SystemMenu> menuList = new ArrayList();
        if (StringUtils.equals("root", id)) {
            SystemMenu menu = systemMenuService.getRootMenu();
            menuList.add(menu);
        } else {
            menuList = systemMenuService.getSystemMenu(id).getChildList();
        }
        return menuList;
    }

    @RequestMapping(method = RequestMethod.POST)
    public SystemMenu create(@RequestBody SystemMenu menu) {
        systemMenuService.saveSystemMenu(menu);
        return menu;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody SystemMenu menu) {
        systemMenuService.saveSystemMenu(menu);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        systemMenuService.deleteMenu(id);
    }


    // 分配菜单
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List loadMenuTree() {

        SystemMenu menu = systemMenuService.getRootMenu();
        return convertMenuData(menu);
    }

    private List convertMenuData(SystemMenu menu) {
        List result = new ArrayList();
        if (!CollectionUtils.isEmpty(menu.getChildList())) {
            for (SystemMenu child : menu.getChildList()) {
                Map data = new HashMap();
                data.put("id", child.getId());
                data.put("name", child.getName());
                data.put("icon", child.getIcon());
                data.put("url", child.getUrl());
                data.put("orderNo", child.getOrderNo());
                data.put("items", convertMenuData(child));
                result.add(data);
            }
        }
        return result;
    }


}
