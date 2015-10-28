package com.withub.csp.service;

import com.withub.csp.entity.Thumb;
import com.withub.csp.repository.NewsDao;
import com.withub.csp.repository.ThumbDao;
import com.withub.csp.repository.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import java.util.Map;


@Service
@Transactional
public class ThumbService extends BaseService{

    @Autowired
    private ThumbDao thumbDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NewsDao newsDao;

    //

    public void saveThumb(Thumb thumb) {

        if (StringUtils.isEmpty(thumb.getId())) {
           initEntity(thumb);
        }
        thumbDao.save(thumb);
    }

    public boolean create(String userId, String newsId) {

        Thumb thumb = thumbDao.findOneByUserIdAndNewsIdAndDeleteFlag(userId, newsId, 0);
        if (thumb != null) {
            return false;
        }

        thumb = new Thumb();
        thumb.setUser(userDao.findOne(userId));
        thumb.setNews(newsDao.findOne(newsId));
        saveThumb(thumb);
        return true;
    }

    public Page<Thumb> getThumb(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Sort.Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Thumb> spec = buildSpecification(searchParams);
        return thumbDao.findAll(spec, pageRequest);
    }

    // 基本无视的方法

    private Specification<Thumb> buildSpecification(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Thumb.class);
    }

}
