package com.withub.service;

import com.withub.entity.Job;
import com.withub.repository.JobDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Identities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Spring Bean的标识.
@Component
public class FileService {

    @Value("${temp.path}")
    private String tempPath;

    public String getTempPath() {
        return tempPath;
    }
}
