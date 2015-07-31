package com.withub.rest;

import com.withub.entity.Menu;
import com.withub.service.content.MenuService;
import com.withub.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.web.MediaTypes;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/menu")
public class MenuRestController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(MenuRestController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private Validator validator;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Menu get(@PathVariable("id") String id) {
        Menu menu = menuService.getMenu(id);
        if (menu == null) {
            String message = "菜单不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return menu;
    }

    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Menu> getTree(@PathVariable("id") String id) {
        List<Menu> menuList = new ArrayList();
        if (StringUtils.equals("Root", id)) {
            Menu menu = menuService.getRootMenu();
            menuList.add(menu);
        } else {
            menuList = menuService.getMenuByParentId(id);
        }
        return menuList;
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Map> getGroup() {

        List<Menu> menuList = menuService.getMenuByParentId(menuService.getRootMenu().getId());
        List<Map> result = new ArrayList<>();
        for (Menu menu : menuList) {
            List<Menu> subMenuList = menuService.getMenuByParentId(menu.getId());
            Map<String, Object> item = new HashMap<>();
            List<Map> subItems = new ArrayList<>();
            item.put("name", menu.getName());
            item.put("items", subItems);
            if (!CollectionUtils.isEmpty(subMenuList)) {
                for (Menu subMenu : subMenuList) {
                    Map subItem = new HashMap();
                    subItem.put("id", subMenu.getId());
                    subItem.put("name", subMenu.getName());
                    subItems.add(subItem);
                }
            }
            result.add(item);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody Menu menu) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, menu);

        // 保存内容
        menuService.saveMenu(menu);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Menu menu) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, menu);

        // 保存内容
        menuService.saveMenu(menu);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        menuService.deleteMenu(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Menu> list() {

        return menuService.getAllMenu();
    }

}
