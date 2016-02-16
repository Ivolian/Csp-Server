package com.withub.csp.rest;

import com.withub.csp.entity.SystemMenu;
import com.withub.csp.service.SystemMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import java.util.ArrayList;
import java.util.List;


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


}
