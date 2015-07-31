package com.withub.service.content;

import com.withub.entity.Product;
import com.withub.repository.ProductDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ProductService {

    private ProductDao productDao;

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product getProduct(String id) {
        return productDao.findOne(id);
    }

    public void saveProduct(Product entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(Identities.uuid());
            entity.setDeleteFlag(0);
        }
        productDao.save(entity);
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        product.setDeleteFlag(1);
        productDao.save(product);
    }

    public Page<Product> getProduct(Map<String, Object> searchParams, int pageNo, int pageSize) {
        Sort sort = new Sort(Direction.ASC, "orderNo");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Specification<Product> spec = buildSpecificationProduct(searchParams);
        return productDao.findAll(spec, pageRequest);
    }

    public List<Product> getAllProduct() {
        Sort sort = new Sort(Direction.ASC, "orderNo");
        Map<String, Object> searchParams = new HashMap<>();
        return productDao.findAll(buildSpecificationProduct(searchParams), sort);
    }

    private Specification<Product> buildSpecificationProduct(Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteFlag", "0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Product> spec = DynamicSpecifications.bySearchFilter(filters.values(), Product.class);
        return spec;
    }

}
