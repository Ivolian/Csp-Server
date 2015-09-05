package com.withub.csp.rest;

import com.withub.csp.entity.Court;
import com.withub.csp.service.CourtService;
import com.withub.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/court")
public class CourtController extends BaseController {

    @Autowired
    private CourtService courtService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Court get(@PathVariable("id") String id) {

        return courtService.getCourt(id);
    }

    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Court> getTree(@PathVariable("id") String id) {
        List<Court> courtList = new ArrayList();
        if (StringUtils.equals("root", id)) {
            Court court = courtService.getRootCourt();
            courtList.add(court);
        } else {
            courtList = courtService.getCourtByParentId(id);
        }
        return courtList;
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Map> getGroup() {

        List<Court> courtList = courtService.getCourtByParentId(courtService.getRootCourt().getId());
        List<Map> result = new ArrayList<>();
        for (Court court : courtList) {
            List<Court> subCourtList = courtService.getCourtByParentId(court.getId());
            Map<String, Object> item = new HashMap<>();
            List<Map> subItems = new ArrayList<>();
            item.put("name", court.getName());
            item.put("items", subItems);
            if (!CollectionUtils.isEmpty(subCourtList)) {
                for (Court subCourt : subCourtList) {
                    Map subItem = new HashMap();
                    subItem.put("id", subCourt.getId());
                    subItem.put("name", subCourt.getName());
                    subItems.add(subItem);
                }
            }
            result.add(item);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public Court create(@RequestBody Court court) {

        courtService.saveCourt(court);
        return court;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Court court) {

        courtService.saveCourt(court);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {

        courtService.deleteCourt(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Court> list() {

        return courtService.getAllCourt();
    }

}
