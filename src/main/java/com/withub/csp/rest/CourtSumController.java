package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.service.CourtSumService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/courtSum")
public class CourtSumController extends BaseController {


    @Autowired
    CourtSumService courtSumService;


    @RequestMapping(method = RequestMethod.GET)
    public JSONObject list(
            @RequestParam(value = "search_beginDate", defaultValue = "") String beginDateString,
            @RequestParam(value = "search_endDate", defaultValue = "") String endDateString
            ) throws Exception {

        JSONArray jsonArray = courtSumService.list(beginDateString, endDateString);

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", true);
        response.put("totalPages", 1);
        return response;
    }


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public JSONObject export(
            @RequestParam(value = "fileName") String fileName,
            @RequestParam(value = "beginTime") String beginTime,
            @RequestParam(value = "endTime") String endTime) throws Exception {

        JSONObject response = courtSumService.exportExcel(fileName, beginTime,endTime);
        return response;
    }

}

