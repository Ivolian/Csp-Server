package com.withub.service.content;

import com.withub.entity.Position;
import com.withub.repository.PositionDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class PositionService {

    @Autowired
    private PositionDao positionDao;

    @Value("${exploded.path}")
    private String explodedPath;

    @Value("${temp.path}")
    private String tempPath;

    public Position getPosition(String id) {
        return positionDao.findOne(id);
    }

    public void savePosition(Position entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }

        // save picture
        if (entity.getPictureAttachment() != null && StringUtils.isNotEmpty(entity.getPictureAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                entity.setPicture(distPath + "/" + entity.getPictureAttachment().getTempFileName() + "." + FilenameUtils.getExtension(entity.getPictureAttachment().getFileName()));
                entity.setPictureFilename(entity.getPictureAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + entity.getPictureAttachment().getTempFileName()), new File(explodedPath + entity.getPicture()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // save ebook
        if (entity.getEbookAttachment() != null && StringUtils.isNotEmpty(entity.getEbookAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                entity.setEbook(distPath + "/" + entity.getEbookAttachment().getTempFileName() + "." + FilenameUtils.getExtension(entity.getEbookAttachment().getFileName()));
                entity.setEbookFilename(entity.getEbookAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + entity.getEbookAttachment().getTempFileName()), new File(explodedPath + entity.getEbook()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Integer maxId2 = positionDao.getMaxId2();
        entity.setId2(maxId2 == null ? 1 : maxId2 + 1);
        positionDao.save(entity);
    }

    public void deletePosition(String id) {
        Position position = getPosition(id);
        position.setDeleteFlag(1);
        positionDao.save(position);
    }

    public Page<Position> getPosition(Map<String, Object> searchParams, int pageNo, int pageSize,String menuId,String keyword) {

        searchParams.put("EQ_menu.id", menuId);
        searchParams.put("_LIKE_name", keyword);

        Sort sort = new Sort(Direction.ASC, "name");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Position> spec = buildSpecificationPosition(searchParams);
        return positionDao.findAll(spec, pageRequest);
    }

    private Specification<Position> buildSpecificationPosition(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Position> spec = DynamicSpecifications.bySearchFilter(filters.values(), Position.class);
        return spec;
    }

    public List<Position> getAllPosition() {
        Sort sort = new Sort(Direction.ASC, "name");
        Map<String, Object> searchParams = new HashMap<>();
        return positionDao.findAll(buildSpecificationPosition(searchParams), sort);
    }
}
