package com.withub.csp.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.AppDiff;
import com.withub.csp.service.AppDiffService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/appDiff")
public class AppDiffController extends BaseController {


    @Autowired
    private AppDiffService appDiffService;


    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<AppDiff> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request
    ) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        return appDiffService.getAppDiff(searchParams, pageNo, pageSize);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AppDiff get(@PathVariable("id") String id) {
        return appDiffService.getAppDiff(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody AppDiff appDiff) {
        appDiffService.saveAppDiff(appDiff);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody AppDiff appDiff) {
        appDiffService.saveAppDiff(appDiff);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        appDiffService.deleteAppDiff(id);
    }


    @RequestMapping(value = "/checkUpdate", method = RequestMethod.GET)
    public JSONObject checkUpdate(
            @RequestParam(value = "clientVersionName") String clientVersionName) {

        return appDiffService.checkUpdate(clientVersionName);
    }

}
