package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Book;
import com.withub.csp.service.BookService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.MediaTypes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/book")
public class BookController extends BaseController {


    @Autowired
    private BookService bookService;

    //

    // 后台列表查询
    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Book> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "search_name", defaultValue = "") String name,
            @RequestParam(value = "search_author", defaultValue = "") String author,
            @RequestParam(value = "search_menuId", defaultValue = "") String menuId,
            @RequestParam(value = "search_beginDate", defaultValue = "") String beginDateString,
            @RequestParam(value = "search_endDate", defaultValue = "") String endDateString
    ) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("LIKE_name", name);
        searchParams.put("LIKE_author", author);
        searchParams.put("EQ_menu.id", menuId);
        if (!beginDateString.equals("")) {
            Date beginDate = simpleDateFormat.parse(beginDateString);
            searchParams.put("GTE_eventTime", beginDate);
        }
        if (!endDateString.equals("")) {
            Date endDate = simpleDateFormat.parse(endDateString);
            searchParams.put("LTE_eventTime", endDate);
        }

        return bookService.getBook(searchParams, pageNo, pageSize);
    }


    // 手机端查询
    @RequestMapping(value = "/listForMobile", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject listForMobile(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "menuId", defaultValue = "") String menuId,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {


        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("LIKE_name", keyword);
        searchParams.put("EQ_menu.id", menuId);


        Page<Book> bookPage = bookService.getBook(searchParams, pageNo, pageSize);
        List<Book> bookList = bookPage.getContent();
        JSONArray jsonArray = new JSONArray();
        for (Book book : bookList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", book.getId());
            jsonObject.put("name", book.getName());
            jsonObject.put("picture", book.getPicture());
            jsonObject.put("ebook", book.getEbook());
            jsonObject.put("ebookFilename", book.getEbookFilename());
            jsonObject.put("summary", book.getSummary());
            jsonObject.put("eventtime", book.getEventTime());
            jsonObject.put("orderNo", book.getOrderNo());
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("content", jsonArray);
        response.put("lastPage", bookPage.isLastPage());
        response.put("totalPages", bookPage.getTotalPages());
        return response;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Book get(@PathVariable("id") String id) {

        return bookService.getBook(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody Book book) {

        bookService.saveBook(book);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody Book book) {

        bookService.saveBook(book);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        bookService.deleteBook(id);
    }

    @RequestMapping(value = "/batchUpload", method = RequestMethod.POST)
    public void batchUpload(@RequestBody List<Book> bookList) {

        for (Book book : bookList) {
            bookService.saveBook(book);
        }
    }

}
