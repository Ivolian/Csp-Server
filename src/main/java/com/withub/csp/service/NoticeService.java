package com.withub.csp.service;

import com.withub.csp.entity.Notice;
import com.withub.csp.repository.NoticeDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Transactional
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    //

    public Notice getNotice(String id) {
        return noticeDao.findOne(id);
    }

    public Page<Notice> getNotice(Map<String, Object> searchParams, int pageNo, int pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNo, pageSize);
        Specification<Notice> spec = buildSpecification(searchParams);
        return noticeDao.findAll(spec, pageRequest);
    }

    private PageRequest buildPageRequest(int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.DESC, "eventTime");
        return new PageRequest(pageNo - 1, pageSize, sort);
    }

    private Specification<Notice> buildSpecification(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Notice.class);
    }

    public void saveNotice(Notice entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);
        }
        noticeDao.save(entity);
    }

    public void deleteNotice(String id) {
        Notice notice = getNotice(id);
        notice.setDeleteFlag(1);
        noticeDao.save(notice);
    }

    //

    public List<Notice> getTopNotices(){
        Page<Notice> noticePage = getNotice(new HashMap<>(),1,5);
        List<Notice> noticeTopList = noticePage.getContent();
        return noticeTopList;
    }


}
