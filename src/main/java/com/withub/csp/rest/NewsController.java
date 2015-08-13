package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.News;
import com.withub.csp.service.NewsService;
import com.withub.rest.RestException;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsController extends BaseController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private NewsService newsService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<News> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortType", defaultValue = "auto") String sortType,
            @RequestParam(value = "regionId", defaultValue = "") String regionId,
            @RequestParam(value = "title", defaultValue = "") String title,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Page<News> newsPage = newsService.getNews(searchParams, pageNo, pageSize, regionId, title);
        return newsPage;
    }

    // 多写了个查询，给手机端用，为了不传递 contentData 减少流量
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list2(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "menuId", defaultValue = "") String menuId,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<News> newsPage = newsService.getNews(searchParams, pageNo, pageSize, menuId, keyword);
        List<News> newsList = newsPage.getContent();

        JSONArray jsonArray = new JSONArray();
        for (News news : newsList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", news.getId());
            jsonObject.put("title", news.getTitle());
            jsonObject.put("picture", news.getPicture());
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public News get(@PathVariable("id") String id) {
        News news = newsService.getNews(id);
        if (news == null) {
            String message = "内容不存在(id:" + id + ")";
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return news;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody News news) {

        newsService.saveNews(news);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    public void update(@RequestBody News news) {

        newsService.saveNews(news);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        newsService.deleteNews(id);
    }


    // 获取新闻内容
    @RequestMapping(value = "/contentData", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public String getContentDate(
            @RequestParam(value = "newsId", defaultValue = "") String newsId) {

        return newsService.getNews(newsId).getNewsData().getData();
    }


}
