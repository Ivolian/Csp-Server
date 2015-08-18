package com.withub.csp.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.csp.entity.Book;
import com.withub.csp.service.BookService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/book")
public class BookController extends BaseController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private BookService bookService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Page<Book> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search");
        return bookService.getBook(searchParams, pageNo, pageSize);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject listForMoblie(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "menuId", defaultValue = "") String menuId,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<Book> bookPage = bookService.getBook(searchParams, pageNo, pageSize, menuId, keyword);
        List<Book> bookList = bookPage.getContent();

        JSONArray jsonArray = new JSONArray();
        for (Book book : bookList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", book.getId());
            jsonObject.put("orderNo", book.getOrderNo());
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


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Book get(@PathVariable("id") String id) {

        return bookService.getBook(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
    public void create(@RequestBody Book book, UriComponentsBuilder uriBuilder) {

        bookService.saveBook(book);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaTypes.JSON)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Book book) {

        bookService.saveBook(book);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        bookService.deleteBook(id);
    }

}
