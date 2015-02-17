package com.yunsoo.service.Impl;


import com.yunsoo.dbmodel.ProductModel;
import com.yunsoo.service.BaseProductService;
import com.yunsoo.service.contract.BaseProduct;
import com.yunsoo.service.contract.Product;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    BaseProductService baseProductServicce;

    @Override
    public Product getByKey(String productKey) {
        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel == null) {
            return null;
        }
        Product product = new Product();
        product.setProductKey(productModel.getProductKey());
        product.setProductStatusId(productModel.getStatusId());
        product.setManufacturingDateTime(productModel.getManufacturingDateTime());
        product.setCreatedDateTime(productModel.getCreatedDateTime());
        product.setBaseProductId(productModel.getBaseProductId());

        product.setBaseProduct(baseProductServicce.getById(productModel.getBaseProductId()));

        return product;
    }

    @Override
    public void batchCreate(int baseProductId, List<String> productKeyList) {
        if (baseProductId > 0 && productKeyList != null) {
            List<ProductModel> products = new ArrayList<>();
            for (String key : productKeyList) {
                ProductModel p = new ProductModel();
                p.setProductKey(key);
                p.setStatusId(0); //init to 0
                p.setBaseProductId(baseProductId);
                p.setCreatedDateTime(DateTime.now());
                products.add(p);
            }

            productDao.batchSave(products);
        }
    }

    @Override
    public void active(String productKey) {
        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel != null) {
            productModel.setStatusId(1); //active
            productDao.save(productModel);
        }
    }
}
