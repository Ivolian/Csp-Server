package com.withub.service.content;

import com.withub.csp.entity.Thumb;
import com.withub.csp.repository.NewsDao;
import com.withub.repository.CspUserDao;
import com.withub.csp.repository.ThumbDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Identities;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ThumbService {

    @Autowired
    private ThumbDao thumbDao;

    @Autowired
    private CspUserDao userDao;

    @Autowired
    private NewsDao newsDao;

    public Thumb getThumb(String id) {
        return thumbDao.findOne(id);
    }

    public void saveThumb(Thumb entity) {

        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }
        thumbDao.save(entity);
    }

    public void deleteThumb(String id) {

        Thumb thumb = getThumb(id);
        thumb.setDeleteFlag(1);
        thumbDao.save(thumb);
    }

    // 新增的
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

}
