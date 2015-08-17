package com.withub.csp.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.service.FavoriteNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;


@RestController
@RequestMapping(value = "/api/v1/favorite")
public class FavoriteNewsController {

    @Autowired
    private FavoriteNewsService favoriteNewsService;

    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        return favoriteNewsService.saveFavoriteNews(userId, newsId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean delete(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        favoriteNewsService.deleteFavoriteNews(userId, newsId);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userId", defaultValue = "") String userId) {

        return favoriteNewsService.listForMobile(pageNo, pageSize, userId);
    }

}
