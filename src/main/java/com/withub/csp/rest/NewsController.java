package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.News;
import com.withub.csp.service.NewsService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsController extends BaseController {


    @Autowired
    private NewsService newsService;


    // 后台列表查询
    @RequestMapping(method = RequestMethod.GET)
    public Page<News> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "search_title", defaultValue = "") String title,
            @RequestParam(value = "search_menuId", defaultValue = "") String menuId) {

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("LIKE_title", title);
        searchParams.put("EQ_menu.id", menuId);

        return newsService.getNews(searchParams, pageNo, pageSize);
    }

    // 后台新增
    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody News news) {

        newsService.saveNews(news);
    }

    // 后台编辑
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody News news) {

        newsService.saveNews(news);
    }


    // 手机端列表查询
    @RequestMapping(value = "/listForMobile", method = RequestMethod.GET)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo") int pageNo,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "menuId") String menuId,
            @RequestParam(value = "keyword") String keyword) {

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("LIKE_title", keyword);
        searchParams.put("EQ_menu.id", menuId);

        Page<News> newsPage = newsService.getNews(searchParams, pageNo, pageSize);
        List<News> newsList = newsPage.getContent();
        JSONArray jsonArray = new JSONArray();
        for (News news : newsList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", news.getId());
            jsonObject.put("title", news.getTitle());
            jsonObject.put("picture", news.getPicture());
            jsonObject.put("postTime", news.getPostTime());
            jsonObject.put("commentCount", news.getCommentList().size());
            jsonObject.put("thumbCount", news.getThumbList().size());
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", newsPage.isLastPage());
        response.put("totalPages", newsPage.getTotalPages());
        return response;
    }


    // 获取新闻内容
    @RequestMapping(value = "/newsData", method = RequestMethod.GET)
    public String getNewData(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "newsId") String newsId) {

        newsService.addNewsRead(userId, newsId);
        return newsService.getNews(newsId).getNewsData().getData();
    }


    // 基本无视的方法
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public News get(@PathVariable("id") String id) {

        return newsService.getNews(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {

        newsService.deleteNews(id);
    }

}
