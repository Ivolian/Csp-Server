package com.withub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// Spring Bean的标识.
@Component
public class FileService {

    @Value("${temp.path}")
    private String tempPath;

    public String getTempPath() {
        return tempPath;
    }
}
