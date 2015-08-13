package com.withub.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Book;
import com.withub.service.content.PositionService;
import com.withub.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/book")
public class PositionRestController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PositionRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private PositionService positionService;
    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Book> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");

        Page<Book> position = positionService.getPosition(searchParams, pageNo, pageSize,"","");
        return position;
    }

    // 多写了个查询，给手机端用
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list2(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "menuId", defaultValue = "") String menuId,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
        ) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<Book> bookPage = positionService.getPosition(searchParams, pageNo, pageSize, menuId,keyword);
        List<Book> bookList = bookPage.getContent();

        JSONArray jsonArray = new JSONArray();
        for (Book book : bookList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", book.getId());
            jsonObject.put("id2", book.getOrderNo());
            jsonObject.put("name", book.getName());
            jsonObject.put("picture", book.getPicture());
            jsonObject.put("ebook", book.getEbook());
            jsonObject.put("ebookFilename", book.getEbookFilename());
            jsonObject.put("summary", book.getSummary());
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", bookPage.isLastPage());
        response.put("totalPages", bookPage.getTotalPages());
        return response;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Book> list() {

        return positionService.getAllPosition();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Book get(@PathVariable("id") String id) {
        Book book = positionService.getPosition(id);
        if (book == null) {
            String message = "书籍不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return book;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody Book book, UriComponentsBuilder uriBuilder) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, book);

        // 保存内容
        positionService.savePosition(book);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    // 按Restful风格约定，返回204状态码, 无内容. 也可以返回200状态码.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Book book) {
        // 调用JSR303 Bean Validator进行校验, 异常将由RestExceptionHandler统一处理.
        BeanValidators.validateWithException(validator, book);

        // 保存内容
        positionService.savePosition(book);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        positionService.deletePosition(id);
    }

}
