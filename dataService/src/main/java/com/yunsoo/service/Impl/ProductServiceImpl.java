package com.yunsoo.service.Impl;


import com.yunsoo.dbmodel.ProductModel;
import com.yunsoo.service.contract.Product;
import com.yunsoo.service.exception.ServiceException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.service.ProductService;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getByKey(String productKey) {
        Assert.notNull(productKey, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel == null) {
            return null;
        }
        Product product = new Product();
        product.setProductKey(productModel.getProductKey());
        product.setStatusId(productModel.getStatusId());
        product.setManufacturingDateTime(productModel.getManufacturingDateTime());
        product.setCreatedDateTime(productModel.getCreatedDateTime());
        product.setProductBaseId(productModel.getProductBaseId());

        return product;
    }

    @Override
    public void batchCreate(Product productTemplate, List<String> productKeyList) {
        Assert.notNull(productTemplate, "productTemplate must not be null");
        Assert.notEmpty(productKeyList, "productKeyList must not be empty");

        int productBaseId = productTemplate.getProductBaseId();
        int statusId = productTemplate.getStatusId();
        DateTime manufacturingDateTime = productTemplate.getManufacturingDateTime();
        DateTime createdDateTime = productTemplate.getCreatedDateTime();

        Assert.isTrue(productBaseId > 0, "productBaseId is invalid");

        if (statusId < 0) statusId = 0;
        if (createdDateTime == null) createdDateTime = DateTime.now();

        List<ProductModel> products = new ArrayList<>();
        for (String key : productKeyList) {
            ProductModel p = new ProductModel();
            p.setProductKey(key);
            p.setProductBaseId(productBaseId);
            p.setStatusId(statusId);
            p.setCreatedDateTime(DateTime.now());
            products.add(p);
        }

        productDao.batchSave(products);

    }


    @Override
    public void update(Product product) {
        Assert.notNull(product, "product must not be null");

        String productKey = product.getProductKey();
        int statusId = product.getStatusId();
        DateTime manufacturingDateTime = product.getManufacturingDateTime();

        Assert.notNull(productKey, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel == null) throw new ServiceException(this.getClass(), "product not found");

        productModel.setStatusId(statusId);
        productModel.setManufacturingDateTime(manufacturingDateTime);
        productDao.save(productModel);
    }

    @Override
    public void patchUpdate(Product product) {
        Assert.notNull(product, "product must not be null");

        String productKey = product.getProductKey();
        int statusId = product.getStatusId();
        DateTime manufacturingDateTime = product.getManufacturingDateTime();

        Assert.notNull(productKey, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel == null) throw new ServiceException(this.getClass(), "product not found");

        if (statusId > 0) productModel.setStatusId(statusId);
        if (manufacturingDateTime != null) productModel.setManufacturingDateTime(manufacturingDateTime);

        productDao.save(productModel);
    }


}
