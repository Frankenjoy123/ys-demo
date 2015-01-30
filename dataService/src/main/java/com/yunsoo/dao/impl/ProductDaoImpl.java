package com.yunsoo.dao.impl;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.dbmodel.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.yunsoo.dao.ProductDao;

@Repository("productDao")
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public ProductModel getByKey(String key) {
        ProductModel product = dynamoDBMapper.load(ProductModel.class, key);

            return product;
    }

    @Override
    public void save(ProductModel product) {
        System.out.println(dynamoDBMapper);
        dynamoDBMapper.save(product);
    }

    @Override
    public void update(ProductModel product) {
        dynamoDBMapper.save(product);
    }

//    @Override
//    public void deleteByKey(String key) {
//        if (key != null && key.length() > 0) {
//            ProductModel product = new ProductModel();
//            product.setProductKey(key);
//            dynamoDBMapper.delete(key);
//        }
//    }
}
