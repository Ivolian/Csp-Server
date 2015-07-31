package com.withub.rest;

import com.withub.service.FileService;
import com.withub.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springside.modules.utils.Identities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/file/upload")
public class FileUploadController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileService fileService;

    @RequestMapping(method = RequestMethod.POST)
    public Map upload(@RequestParam(value = "attachment") CommonsMultipartFile attachment) throws Exception {

        String fileName = attachment.getFileItem().getName();
        String tempFileName = Identities.randomLong() + "";

        String tempDirectory = fileService.getTempPath();

        attachment.getFileItem().write(new File(tempDirectory + "/" + tempFileName));

        Map data = new HashMap();
        data.put("fileName", fileName);
        data.put("tempFileName", tempFileName);
        return data;
    }

}
