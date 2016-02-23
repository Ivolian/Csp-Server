package com.withub.csp.rest;

import com.withub.csp.entity.District;
import com.withub.csp.service.DistrictService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/district")
public class DistrictController extends BaseController {


    @Autowired
    private DistrictService districtService;

    //

    // 后台列表查询
    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<District> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request
    ) throws Exception {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        return districtService.getDistrict(searchParams, pageNo, pageSize);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public District get(@PathVariable("id") String id) {
        return districtService.getDistrict(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody District district) {
        districtService.saveDistrict(district);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody District district) {
        districtService.saveDistrict(district);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        districtService.deleteDistrict(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<District> list() {
        return districtService.getAllDistrict();
    }


}
