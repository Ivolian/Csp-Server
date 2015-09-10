package com.withub.rest;

import com.withub.csp.service.CourtDataService;
import com.withub.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;


@RestController
@RequestMapping(value = "/api/v1/file/download")
public class FileDownloadController extends BaseController {

    @Autowired
    private CourtDataService courtDataService;

    @RequestMapping(method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String fileName = request.getParameter("fileName");
        String tempFileName = request.getParameter("tempFileName");
        String tempFilePath = courtDataService.getTempPath() + '/' + tempFileName;
        File file = new File(tempFilePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        response.setHeader("Content-Length", file.length() + "");
        if (fileName != null) {
            response.setHeader("Content-Disposition", "filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1"));
        }
        response.setContentType("application/octet-stream");

        FileCopyUtils.copy(fileInputStream, response.getOutputStream());

        fileInputStream.close();
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
