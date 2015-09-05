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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Service
@Transactional
public class BookService extends BaseService {

    @Value("${exploded.path}")
    private String explodedPath;

    @Value("${temp.path}")
    private String tempPath;

    //

    @Autowired
    private BookDao bookDao;

    //

    public void saveBook(Book book) {

        if (StringUtils.isEmpty(book.getId())) {

            Book old = bookDao.findOneByEbookFilenameAndDeleteFlag(book.getEbookAttachment().getFileName(), 0);
            if (old != null) {
                throw new RuntimeException(book.getEbookAttachment().getFileName() + "已存在");
            }

            initEntity(book);
            book.setOrderNo(getNextOrderNo());
        }else {
            Book old = bookDao.isBookExist(book.getEbookFilename(), book.getId());
            if (old != null) {
                throw new RuntimeException(book.getEbookFilename() + "已存在");
            }
        }

        // save picture
        if (book.getPictureAttachment() != null && StringUtils.isNotEmpty(book.getPictureAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                book.setPicture(distPath + "/" + book.getPictureAttachment().getTempFileName() + "." + FilenameUtils.getExtension(book.getPictureAttachment().getFileName()));
                book.setPictureFilename(book.getPictureAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + book.getPictureAttachment().getTempFileName()), new File(explodedPath + book.getPicture()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // save ebook
        if (book.getEbookAttachment() != null && StringUtils.isNotEmpty(book.getEbookAttachment().getFileName())) {
            try {
                String distPath = "/attachment/content/" + new SimpleDateFormat("yyyy/MM").format(new Date());
                File distFile = new File(explodedPath + distPath);
                if (!distFile.exists()) {
                    FileUtils.forceMkdir(distFile);
                }
                book.setEbook(distPath + "/" + book.getEbookAttachment().getTempFileName() + "." + FilenameUtils.getExtension(book.getEbookAttachment().getFileName()));
                book.setEbookFilename(book.getEbookAttachment().getFileName());
                FileUtils.copyFile(new File(tempPath + "/" + book.getEbookAttachment().getTempFileName()), new File(explodedPath + book.getEbook()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        bookDao.save(book);
    }


    public Page<Book> getBook(Map<String, Object> searchParams, int pageNo, int pageSize) {

        Sort sort = new Sort(Direction.DESC, "eventTime");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Book> spec = buildSpecificationPosition(searchParams);
        return bookDao.findAll(spec, pageRequest);
    }

    // 基础方法
    private Integer getNextOrderNo() {

        Integer maxOrderNo = bookDao.getMaxOrderNo();
        return maxOrderNo == null ? 1 : maxOrderNo + 1;
    }

    // 基本无视的方法
    private Specification<Book> buildSpecificationPosition(Map<String, Object> searchParams) {

        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return DynamicSpecifications.bySearchFilter(filters.values(), Book.class);
    }

    public Book getBook(String id) {
        return bookDao.findOne(id);
    }

    public void deleteBook(String id) {

        Book book = getBook(id);
        book.setDeleteFlag(1);
        bookDao.save(book);
    }

}
