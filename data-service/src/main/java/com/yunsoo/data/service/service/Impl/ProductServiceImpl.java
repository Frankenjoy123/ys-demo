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

        return product;
    }

//    @Override
//    public void batchCreate(Product productTemplate, List<String> productKeyList) {
//        Assert.notNull(productTemplate, "productTemplate must not be null");
//        Assert.notEmpty(productKeyList, "productKeyList must not be empty");
//
//        String productBaseId = productTemplate.getProductBaseId();
//        String statusId = productTemplate.getProductStatusCode();
//        DateTime manufacturingDateTime = productTemplate.getManufacturingDateTime();
//        DateTime createdDateTime = productTemplate.getCreatedDateTime();
//
//        if (createdDateTime == null) createdDateTime = DateTime.now();
//
//        List<ProductModel> products = new ArrayList<>();
//        for (String key : productKeyList) {
//            ProductModel p = new ProductModel();
//            p.setProductKey(key);
//            p.setProductBaseId(productBaseId);
//            p.setProductStatusCode(statusId);
//            p.setCreatedDateTime(createdDateTime);
//            if (manufacturingDateTime != null) {
//                p.setManufacturingDateTime(manufacturingDateTime);
//            }
//            products.add(p);
//        }
//
//        productDao.batchSave(products);

//    }


    @Override
    public void update(Product product) {
        Assert.notNull(product, "product must not be null");

        String productKey = product.getProductKey();
        String statusCode = product.getProductStatusCode();
        DateTime manufacturingDateTime = product.getManufacturingDateTime();

        Assert.notNull(productKey, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel == null) throw new ServiceException(this.getClass(), "Product not found");

        productModel.setProductStatusCode(statusCode);
        productModel.setManufacturingDateTime(manufacturingDateTime);
        productDao.save(productModel);
    }

    @Override
    public void patchUpdate(Product product) {
        Assert.notNull(product, "product must not be null");

        String productKey = product.getProductKey();
        String statusCode = product.getProductStatusCode();
        DateTime manufacturingDateTime = product.getManufacturingDateTime();

        Assert.notNull(productKey, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel == null) throw new ServiceException(this.getClass(), "Product not found");

        if (statusCode != null) productModel.setProductStatusCode(statusCode);
        if (manufacturingDateTime != null) productModel.setManufacturingDateTime(manufacturingDateTime);

        productDao.save(productModel);
    }


}
