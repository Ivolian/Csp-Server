/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.withub.service.content;

import com.withub.entity.Content;
import com.withub.entity.ContentColumn;
import com.withub.entity.ContentData;
import com.withub.repository.ContentColumnDao;
import com.withub.repository.ContentDao;
import com.withub.repository.ContentDataDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ContentService {

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private ContentDataDao contentDataDao;

    @Autowired
    private ContentColumnDao contentColumnDao;

    @Value("${exploded.path}")
    private String explodedPath;

    @Value("${temp.path}")
    private String tempPath;

    public Content getContent(String id) {

        Content content = contentDao.findOne(id);
        Map<String, SearchFilter> filters = new HashMap<>();
        filters.put("content", new SearchFilter("content.id", SearchFilter.Operator.EQ, id));
        Specification<ContentData> spec = DynamicSpecifications.bySearchFilter(filters.values(), ContentData.class);
        content.setContentData(contentDataDao.findOne(spec));
        return content;
    }

    public ContentColumn getContentColumn(String id) {
        return contentColumnDao.findOne(id);
    }

    public void saveContent(Content entity) {

        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);
            entity.setPublish(1);
            ContentData contentData = entity.getContentData();
            contentData.setId(Identities.uuid());
        }

        // save picture
        if (entity.getAttachment() != null && StringUtils.isNotEmpty(entity.getAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                entity.setPicture(distPath + "/" + entity.getAttachment().getTempFileName() + "." + FilenameUtils.getExtension(entity.getAttachment().getFileName()));
                entity.setPictureFilename(entity.getAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + entity.getAttachment().getTempFileName()), new File(explodedPath + entity.getPicture()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // save contentData
        ContentData contentData = entity.getContentData();
        contentData.setContent(entity);
        entity.setContentData(null);
        contentDao.save(entity);
        contentDataDao.save(contentData);
    }

    public void deleteContent(String id) {

        Content content = getContent(id);
        content.setDeleteFlag(1);
        contentDao.save(content);
    }

    public List<ContentColumn> getAllContentColumn() {
        return (List<ContentColumn>) contentColumnDao.findAll();
    }

    public Page<Content> getNews(Map<String, Object> searchParams, int pageNo, int pageSize,
                                 String menuId,String keyword) {

        searchParams.put("EQ_contentColumnId", "101");
        searchParams.put("EQ_menu.id", menuId);
        searchParams.put("_LIKE_title", keyword);
        return getContent(searchParams, pageNo, pageSize);
    }

    public Page<Content> getCulture(Map<String, Object> searchParams, int pageNo, int pageSize,
                                    String sortType) {

        searchParams.put("EQ_contentColumnId", "102");
        return getContent(searchParams, pageNo, pageSize);
    }

    public Page<Content> getContent(Map<String, Object> searchParams, int pageNo, int pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNo, pageSize);
        Specification<Content> spec = buildSpecification(searchParams);
        return contentDao.findAll(spec, pageRequest);
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.DESC, "postTime");
        return new PageRequest(pageNo - 1, pageSize, sort);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Content> buildSpecification(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Content> spec = DynamicSpecifications.bySearchFilter(filters.values(), Content.class);
        return spec;
    }


}
