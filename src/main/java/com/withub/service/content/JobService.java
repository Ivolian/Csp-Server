package com.withub.service.content;

import com.withub.entity.*;
import com.withub.repository.JobDao;
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
import org.springframework.util.FileCopyUtils;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class JobService {
    private JobDao jobDao;

    @Autowired
    public void setJobDao(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public Job getJob(String id) {
        return jobDao.findOne(id);
    }

    public void saveJob(Job entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setPublish(1);
            entity.setDeleteFlag(0);
        }
        jobDao.save(entity);
    }

    public void deleteJob(String id) {
        Job job = getJob(id);
        job.setDeleteFlag(1);
        jobDao.save(job);
    }

    public Page<Job> getJob(Map<String, Object> searchParams, int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.ASC, "orderNo");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Job> spec = buildSpecificationJob(searchParams);
        if (jobDao.findAll(spec, pageRequest).getContent().size() != 0) {
            return jobDao.findAll(spec, pageRequest);
        } else {
            spec = buildSpecificationJobPlace(searchParams);
            return jobDao.findAll(spec, pageRequest);
        }
    }

    /*用于条件查询省或市*/
    private Specification<Job> buildSpecificationJob(Map<String, Object> searchParams) {
        if (searchParams.isEmpty()) {
        } else {
            searchParams.put("_LIKE_region.parent.name", searchParams.get("_LIKE_region"));
            searchParams.remove("_LIKE_region");
        }
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Job> spec = DynamicSpecifications.bySearchFilter(filters.values(), Job.class);
        return spec;
    }

    /*用于条件查询市或区*/
    private Specification<Job> buildSpecificationJobPlace(Map<String, Object> searchParams) {

        searchParams.put("_LIKE_region.name", searchParams.get("_LIKE_region.parent.name"));
        searchParams.remove("_LIKE_region.parent.name");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Job> spec = DynamicSpecifications.bySearchFilter(filters.values(), Job.class);
        return spec;
    }

    public List<Job> getAllJob() {
        Sort sort = new Sort(Direction.ASC, "orderNo");
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_publish", "1");
        return jobDao.findAll(buildSpecificationJob(searchParams), sort);
    }
}
