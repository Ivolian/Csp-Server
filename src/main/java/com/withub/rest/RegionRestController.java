package com.withub.rest;

import com.withub.entity.Position;
import com.withub.entity.Region;
import com.withub.service.content.RegionService;
import com.withub.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.web.MediaTypes;

import javax.validation.Validator;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/region")
public class RegionRestController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RegionRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private RegionService regionService;
    @Autowired
    private Validator validator;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Region get(@PathVariable("id") String id) {
        Region region = regionService.getRegion(id);
        if (region == null) {
            String message = "区域不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return region;
    }

    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Region> getTree(@PathVariable("id") String id) {
        List<Region> regionList = new ArrayList();
        if (StringUtils.equals("Root", id)) {
            Region region = regionService.getRootRegion();
            regionList.add(region);
//            regionList = regionService.getRegionByParentId(region.getId());
        } else {
            regionList = regionService.getRegionByParentId(id);
        }
        return regionList;
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Map> getGroup() {

        List<Region> regionList = regionService.getRegionByParentId(regionService.getRootRegion().getId());
        List<Map> result = new ArrayList<>();
        for (Region region : regionList) {
            List<Region> subRegionList = regionService.getRegionByParentId(region.getId());
            Map<String, Object> item = new HashMap<>();
            List<Map> subItems = new ArrayList<>();
            item.put("name", region.getName());
            item.put("items", subItems);
            if (!CollectionUtils.isEmpty(subRegionList)) {
                for (Region subRegion : subRegionList) {
                    Map subItem = new HashMap();
                    subItem.put("id", subRegion.getId());
                    subItem.put("name", subRegion.getName());
                    subItems.add(subItem);
                }
            }
            result.add(item);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public ResponseEntity<Region> create(@RequestBody Region region, UriComponentsBuilder uriBuilder) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, region);

        // 保存内容
        regionService.saveRegion(region);

        // 按照Restful风格约定，创建指向新内容的url, 也可以直接返回id或对象.
        String id = region.getId();
        URI uri = uriBuilder.path("/api/v1/region/" + id).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        return new ResponseEntity(region, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Region region) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, region);

        // 保存内容
        regionService.saveRegion(region);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        regionService.deleteRegion(id);
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Region> list() {

        return regionService.getAllRegion();
    }


    // 我新加的，获得所有的菜单
    @RequestMapping(value = "/all2", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> getAll() {

        return regionToItem(regionService.getRootRegion(), 0);
    }

    private Map<String, Object> regionToItem(Region region, int depth) {

        Map<String, Object> item = new HashMap<>();
        item.put("id", region.getId());
        item.put("name", region.getName());
        item.put("code", region.getCode());
        item.put("orderNo", region.getOrderNo());
        item.put("depth", depth);

        List<Map> items = new ArrayList<>();
        for (Region child : regionService.getRegionByParentId(region.getId())) {
            items.add(regionToItem(child, depth + 1));
        }
        item.put("items", items);

        return item;
    }

}
