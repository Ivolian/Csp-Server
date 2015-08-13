package com.withub.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.withub.entity.FavoriteBook;
import com.withub.entity.Book;
import com.withub.service.content.FavoriteBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/favoritebook")
public class FavoriteBookRestController {

    @Autowired
    private FavoriteBookService favoriteBookService;

    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean create(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "bookId", defaultValue = "") String bookId) {

        return favoriteBookService.saveFavoriteBook(userId, bookId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean delete(
            @RequestParam(value = "userId", defaultValue = "") String userId,
            @RequestParam(value = "bookId", defaultValue = "") String bookId) {

        favoriteBookService.deleteFavoriteBook(userId, bookId);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public JSONObject list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userId", defaultValue = "") String userId) {

        Map<String, Object> searchParams = new HashMap<>();
        Page<FavoriteBook> favoritePage = favoriteBookService.getFavoriteBook(searchParams, pageNo, pageSize, userId);
        List<FavoriteBook> favoriteBookList = favoritePage.getContent();
        List<Book> bookList = new ArrayList<>();
        for (FavoriteBook favoriteBook : favoriteBookList) {
            bookList.add(favoriteBook.getBook());
        }

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
            jsonObject.put("menuName",book.getMenu().getName());
            jsonArray.add(jsonObject);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", jsonArray);
        jsonObject.put("lastPage", favoritePage.isLastPage());
        jsonObject.put("totalPages", favoritePage.getTotalPages());
        return jsonObject;
    }

}
