package com.withub.csp.service;

import com.withub.csp.entity.App;
import com.withub.csp.repository.*;
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
import java.util.Map;


@Component
@Transactional
public class AppService {

    @Value("${exploded.path}")
    private String explodedPath;

    @Value("${temp.path}")
    private String tempPath;

    @Autowired
    private AppDao appDao;

    //

    public App getApp(String id) {

        return appDao.findOne(id);
    }

    public Page<App> getApp(Map<String, Object> searchParams, int pageNo, int pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNo, pageSize);
        Specification<App> spec = buildSpecification(searchParams);
        return appDao.findAll(spec, pageRequest);
    }

    private PageRequest buildPageRequest(int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "eventTime");
        return new PageRequest(pageNo - 1, pageSize, sort);
    }

    private Specification<App> buildSpecification(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), App.class);
    }

    public void saveApp(App entity) {

        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);

            // 版本号递增
            Integer versionCode = appDao.getMaxVersionCode() == null ? 1 : (appDao.getMaxVersionCode() + 1);
            entity.setVersionCode(versionCode);
        }

        // save apk
        if (entity.getApkAttachment() != null && StringUtils.isNotEmpty(entity.getApkAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                entity.setApk(distPath + "/" + entity.getApkAttachment().getTempFileName() + "." + FilenameUtils.getExtension(entity.getApkAttachment().getFileName()));
                entity.setApkFilename(entity.getApkAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + entity.getApkAttachment().getTempFileName()), new File(explodedPath + entity.getApk()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        appDao.save(entity);
    }

    public void deleteApp(String id) {

        App app = getApp(id);
        app.setDeleteFlag(1);
        appDao.save(app);
    }

    // todo checkUpdate

}
