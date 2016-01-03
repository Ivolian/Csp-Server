package com.withub.csp.service;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.AppDiff;
import com.withub.csp.repository.AppDiffDao;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Component
@Transactional
public class AppDiffService extends BaseService {


    @Value("${exploded.path}")
    private String explodedPath;

    @Value("${temp.path}")
    private String tempPath;

    @Autowired
    private AppDiffDao appDiffDao;

    //

    public AppDiff getAppDiff(String id) {
        return appDiffDao.findOne(id);
    }

    public Page<AppDiff> getAppDiff(Map<String, Object> searchParams, int pageNo, int pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNo, pageSize);
        Specification<AppDiff> spec = buildSpecification(searchParams);
        return appDiffDao.findAll(spec, pageRequest);
    }

    private PageRequest buildPageRequest(int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.DESC, "versionName");
        return new PageRequest(pageNo - 1, pageSize, sort);
    }

    private Specification<AppDiff> buildSpecification(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), AppDiff.class);
    }

    public void saveAppDiff(AppDiff entity) {

        if (StringUtils.isEmpty(entity.getId())) {
            initEntity(entity);
        }

        // save apk
        if (entity.getDiffAttachment() != null && StringUtils.isNotEmpty(entity.getDiffAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                entity.setDiff(distPath + "/" + entity.getDiffAttachment().getTempFileName() + "." + FilenameUtils.getExtension(entity.getDiffAttachment().getFileName()));
                entity.setDiffFilename(entity.getDiffAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + entity.getDiffAttachment().getTempFileName()), new File(explodedPath + entity.getDiff()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        appDiffDao.save(entity);
    }

    public void deleteAppDiff(String id) {
        AppDiff appDiff = getAppDiff(id);
        appDiff.setDeleteFlag(1);
        appDiffDao.save(appDiff);
    }

    private String getCurrentVersionName() {
        return appDiffDao.findVersionNameList().get(0);
    }

    public JSONObject checkUpdate(String clientVersionName) {
        String currentVersionName = getCurrentVersionName();
        if (currentVersionName.equals(clientVersionName)) {
            JSONObject result = new JSONObject();
            result.put("needUpdate", false);
            return result;
        }

        // 需要更新
        AppDiff appDiff = appDiffDao.findOneByClientVersionName(clientVersionName);
        JSONObject result = new JSONObject();
        result.put("needUpdate", true);
        result.put("diff", appDiff.getDiff());

        String apkPath = explodedPath + File.separator + "app.apk";
        File apk = new File(apkPath);
        String md5Sign = MD5Utils.getMd5ByFile(apk);
        result.put("md5Sign", md5Sign);

        return result;
    }

}
