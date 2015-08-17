package com.withub.csp.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Menu;
import com.withub.csp.repository.MenuDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional


public class MenuService {

    @Autowired
    private MenuDao menuDao;

    public Menu getMenu(String id) {
        return menuDao.findOne(id);
    }

    public void saveMenu(Menu entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);
        }
        menuDao.save(entity);
    }

    public void deleteMenu(String id) {
        Menu menu = getMenu(id);
        menu.setDeleteFlag(1);
        menuDao.save(menu);
    }

    public Menu getRootMenu() {
        return menuDao.parentIsNull();
    }

    public List<Menu> getMenuByParentId(String parentId) {

        Sort sort = new Sort(Direction.ASC, "orderNo");
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_parent.id", parentId);
        Specification<Menu> spec = buildSpecificationRegion(searchParams);
        return menuDao.findAll(spec, sort);
    }

    private Specification<Menu> buildSpecificationRegion(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Menu.class);
    }

    public List<Menu> getAllMenu() {
        Sort sort = new Sort(Direction.ASC, "parent", "orderNo");
        Map<String, Object> searchParams = new HashMap<>();
        return menuDao.findAll(buildSpecificationRegion(searchParams), sort);
    }

    // 获得所有的菜单
    public JSONObject getRootMenuItem() {

        return menuToItem(getRootMenu());
    }

    private JSONObject menuToItem(Menu menu) {

        JSONObject item = new JSONObject();
        item.put("id", menu.getId());
        item.put("name", menu.getName());
        item.put("type", menu.getType());
        item.put("orderNo", menu.getOrderNo());

        JSONArray items = new JSONArray();
        for (Menu child : getMenuByParentId(menu.getId())) {
            items.add(menuToItem(child));
        }
        item.put("items", items);

        return item;
    }

}
