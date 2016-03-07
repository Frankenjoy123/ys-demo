package com.yunsoo.data.service.service.Impl;


import com.yunsoo.data.service.dao.ProductDao;
import com.yunsoo.data.service.dbmodel.dynamodb.ProductModel;
import com.yunsoo.data.service.service.ProductService;
import com.yunsoo.data.service.service.contract.Product;
import com.yunsoo.data.service.service.exception.ServiceException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getByKey(String productKey) {
        Assert.notNull(productKey, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel == null
                || (productModel.getProductKeyDisabled() != null && productModel.getProductKeyDisabled())) {
            return null;
        }
        String productKeyTypeCode = productModel.getProductKeyTypeCode();
        String productKeyBatchId = productModel.getProductKeyBatchId();

        if (!productModel.isPrimary() && productModel.getPrimaryProductKey() != null) {
            //get the primary product key
            productModel = productDao.getByKey(productModel.getPrimaryProductKey());
        }
        if (productModel == null || productModel.getProductBaseId() == null) {
            return null;
        }

        Product product = new Product();
        product.setProductKey(productKey);
        product.setProductKeyTypeCode(productKeyTypeCode);
        product.setProductKeyBatchId(productKeyBatchId);
        product.setProductKeySet(productModel.getProductKeySet());
        product.setCreatedDateTime(productModel.getCreatedDateTime());

        product.setProductBaseId(productModel.getProductBaseId());
        product.setProductStatusCode(productModel.getProductStatusCode());
        product.setManufacturingDateTime(productModel.getManufacturingDateTime());
        product.setDetails(productModel.getDetails());

        return product;
    }

    @Override
    public void patchUpdate(Product product) {
        Assert.notNull(product, "product must not be null");
        Assert.notNull(product.getProductKey(), "productKey must not be null");

        String statusCode = product.getProductStatusCode();
        DateTime manufacturingDateTime = product.getManufacturingDateTime();
        String details = product.getDetails();

        ProductModel productModel = productDao.getByKey(product.getProductKey());
        if (productModel == null
                || (productModel.getProductKeyDisabled() != null && productModel.getProductKeyDisabled())) {
            throw ServiceException.notFound("product not found"); //product not found
        }
        if (!productModel.isPrimary()) {
            //get the primary product key
            productModel = productDao.getByKey(productModel.getPrimaryProductKey());
        }
        if (productModel == null) {
            throw ServiceException.notFound("product not found"); //product not found
        }

        if (statusCode != null) productModel.setProductStatusCode(statusCode);
        if (manufacturingDateTime != null) productModel.setManufacturingDateTime(manufacturingDateTime);
        if (details != null) productModel.setDetails(details);

        productDao.save(productModel);
    }

}
