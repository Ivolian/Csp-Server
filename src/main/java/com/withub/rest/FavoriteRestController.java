package com.withub.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.entity.Content;
import com.withub.entity.Favorite;
import com.withub.service.content.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/favorite")
public class FavoriteRestController {

    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        return favoriteService.saveFavorite(userId, newsId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean delete(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        favoriteService.deleteFavorite(userId, newsId);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userId", defaultValue = "") String userId) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<Favorite> favoritePage = favoriteService.getFavorite(searchParams, pageNo, pageSize, userId);
        List<Favorite> favoriteList = favoritePage.getContent();
        List<Content> newsList = new ArrayList<>();
        for (Favorite favorite : favoriteList) {
            newsList.add(favorite.getNews());
        }

        JSONArray jsonArray = new JSONArray();
        for (Content news : newsList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", news.getId());
            jsonObject.put("title", news.getTitle());
            jsonObject.put("picture", news.getPicture());
            jsonObject.put("commentCount", news.getCommentList().size());
            jsonArray.add(jsonObject);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", jsonArray);
        jsonObject.put("lastPage", favoritePage.isLastPage());
        jsonObject.put("totalPages", favoritePage.getTotalPages());
        return jsonObject;
    }

}
