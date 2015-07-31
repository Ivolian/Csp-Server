package com.withub.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.entity.Comment;
import com.withub.entity.Content;
import com.withub.entity.Favorite;
import com.withub.service.content.CommentService;
import com.withub.service.content.FavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import javax.validation.Validator;
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
            @RequestParam(value = "contentId", defaultValue = "") String contentId,
            ServletRequest request) {

        favoriteService.saveFavorite(contentId, userId);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userId", defaultValue = "") String userId,

            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        Page<Favorite> favoritePage = favoriteService.getFavorite(searchParams, pageNo, pageSize, userId);
        List<Favorite> favoriteList = favoritePage.getContent();
        List<Content> contentList = new ArrayList<>();
        for (Favorite favorite : favoriteList) {
            contentList.add(favorite.getContent());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", contentList);
        jsonObject.put("lastPage", favoritePage.isLastPage());
        jsonObject.put("totalPages", favoritePage.getTotalPages());
        return jsonObject;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean delete(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "contentId", defaultValue = "") String contentId,
            ServletRequest request) {

        favoriteService.deleteFavorite(userId, contentId);
        return true;
    }


}
