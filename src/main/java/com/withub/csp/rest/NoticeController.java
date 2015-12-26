package com.withub.csp.rest;

import com.withub.csp.entity.Notice;
import com.withub.csp.service.NoticeService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping(value = "/topList",method = RequestMethod.GET)
    public List<Notice> getTopNotice() {
        return noticeService.getTopNotices();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Notice> list(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize,
            ServletRequest request
    ) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        return noticeService.getNotice(searchParams, pageNo, pageSize);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Notice get(@PathVariable("id") String id) {
        return noticeService.getNotice(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody Notice notice) {
        noticeService.saveNotice(notice);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody Notice notice) {
        noticeService.saveNotice(notice);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        noticeService.deleteNotice(id);
    }

}
