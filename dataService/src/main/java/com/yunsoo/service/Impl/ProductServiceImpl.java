package com.yunsoo.service.Impl;


import com.fasterxml.jackson.databind.deser.Deserializers;
import com.yunsoo.dao.BaseProductDao;
import com.yunsoo.dbmodel.BaseProductModel;
import com.yunsoo.dbmodel.ProductModel;
import com.yunsoo.service.BaseProductService;
import com.yunsoo.service.contract.BaseProduct;
import com.yunsoo.service.contract.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    BaseProductService baseProductServicce;

    @Override
    public Product getByKey(String key) {
        ProductModel productModel = productDao.getByKey(key);
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
    public void batchCreate() {

    }

    @Override
    public void active() {

    }
}
