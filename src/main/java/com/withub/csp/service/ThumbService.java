package com.withub.csp.service;

import com.withub.csp.entity.Thumb;
import com.withub.csp.repository.NewsDao;
import com.withub.csp.repository.ThumbDao;
import com.withub.csp.repository.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

}
