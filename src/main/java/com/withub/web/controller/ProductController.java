package com.withub.web.controller;

import com.withub.entity.Product;
import com.withub.rest.RestException;
import com.withub.service.content.ContentService;
import com.withub.service.content.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.MediaTypes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private ProductService productService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<Product> list(ServletRequest request) {

        List<Product> products = productService.getAllProduct();
        return products;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Product get(@PathVariable("id") String id) {
        Product product = productService.getProduct(id);
        if (product == null) {
            String message = "产品不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return product;
    }
}
