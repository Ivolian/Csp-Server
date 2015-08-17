package com.withub.csp.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.App;
import com.withub.csp.service.AppService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/app")
public class AppController extends BaseController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private AppService appService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<App> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        return appService.getApp(searchParams, pageNo, pageSize);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public App get(@PathVariable("id") String id) {

        return appService.getApp(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody App app) {

        appService.saveApp(app);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    public void update(@RequestBody App app) {

        appService.saveApp(app);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        appService.deleteApp(id);
    }


    @RequestMapping(value = "/checkUpdate", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject getContentDate(
            @RequestParam(value = "versionName") String versionName) {

        return appService.checkUpdate(versionName);
    }

}
