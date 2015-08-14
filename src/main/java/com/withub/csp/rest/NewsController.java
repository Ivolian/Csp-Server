package com.withub.csp.rest;

import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.News;
import com.withub.csp.service.NewsService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.Map;


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
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Page<News> newsPage = newsService.getNews(searchParams, pageNo, pageSize, null, null);
        return newsPage;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public News get(@PathVariable("id") String id) {

        return newsService.getNews(id);
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
    public void delete(@PathVariable("id") String id) {
        newsService.deleteNews(id);
    }


    // 手机端用，新闻查询
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "menuId") String menuId, @RequestParam(value = "keyword") String keyword) {

        return newsService.listForMobile(pageNo, pageSize, menuId, keyword);
    }

    // 获取新闻内容
    @RequestMapping(value = "/newsData", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public String getContentDate(
            @RequestParam(value = "newsId") String newsId) {

        return newsService.getNews(newsId).getNewsData().getData();
    }

}
