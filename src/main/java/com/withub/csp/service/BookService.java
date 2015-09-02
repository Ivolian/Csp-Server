package com.withub.csp.service;

import com.withub.csp.entity.Book;
import com.withub.csp.repository.BookDao;
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
import java.util.Map;


@Component
@Transactional
public class BookService {

    @Autowired
    private BookDao bookDao;

    @Value("${exploded.path}")
    private String explodedPath;

    @Value("${temp.path}")
    private String tempPath;

    //

    public Book getBook(String id) {
        return bookDao.findOne(id);
    }

    public void saveBook(Book entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setEventTime(new Date());
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

        Integer maxOrderNo = bookDao.getMaxOrderNo();
        entity.setOrderNo(maxOrderNo == null ? 1 : maxOrderNo + 1);
        bookDao.save(entity);
    }

    public void deleteBook(String id) {
        Book book = getBook(id);
        book.setDeleteFlag(1);
        bookDao.save(book);
    }

    public Page<Book> getBook(Map<String, Object> searchParams, int pageNo, int pageSize, String menuId, String keyword) {

        searchParams.put("EQ_menu.id", menuId);
        searchParams.put("_LIKE_name", keyword);
        return getBook(searchParams, pageNo, pageSize);
    }

    public Page<Book> getBook(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Book> spec = buildSpecificationPosition(searchParams);
        return bookDao.findAll(spec, pageRequest);
    }

    private Specification<Book> buildSpecificationPosition(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Book.class);
    }

}
