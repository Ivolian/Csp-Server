package com.withub.rest;

import com.withub.entity.Region;
import com.withub.service.FileService;
import com.withub.service.content.RegionService;
import com.withub.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.utils.Identities;
import org.springside.modules.web.MediaTypes;

import javax.validation.Validator;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
