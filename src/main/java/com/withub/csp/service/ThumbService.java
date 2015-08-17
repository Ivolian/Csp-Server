package com.withub.csp.service;

import com.withub.csp.entity.Thumb;
import com.withub.csp.repository.NewsDao;
import com.withub.csp.repository.UserDao;
import com.withub.csp.repository.ThumbDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Identities;

import java.util.Date;

@Component
@Transactional
public class ThumbService {

    @Autowired
    private ThumbDao thumbDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NewsDao newsDao;

    //

    public void saveThumb(Thumb entity) {

        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
            entity.setDeleteFlag(0);
        }
        thumbDao.save(entity);
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

}
