package com.withub.web.controller;


import com.withub.entity.Job;
import com.withub.entity.Region;
import com.withub.rest.RestException;
import com.withub.service.content.JobService;
import com.withub.service.content.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/job")
public class JobController {

    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private JobService jobService;

    @Autowired
    private RegionService regionService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Job> list() {

        List<Job> job = jobService.getAllJob();
        return job;
    }

    @RequestMapping(value = "/region", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Region> regionList() {

        List<Region> regionList = regionService.getRegionByParentId(regionService.getRootRegion().getId());
        return regionList;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Job get(@PathVariable("id") String id) {
        Job job = jobService.getJob(id);
        if (job == null) {
            String message = "内容不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return job;
    }
}
