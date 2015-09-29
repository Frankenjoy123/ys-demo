package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.rabbit.dto.Product;
import com.yunsoo.api.rabbit.dto.ProductCategory;
import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.data.object.UserProductFollowingObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@Component
@ElastiCacheConfig
public class ProductDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private UserDomain userDomain;

    @Autowired
    ProductBaseDomain productBaseDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDomain.class);

    //Retrieve Product key, ProductBase entry and Product-Category entry from Backend.
    public Product getProductByKey(String key) {
        Product product = new Product();
        product.setProductKey(key);

        ProductObject productObject = null;
        try {
            productObject = dataAPIClient.get("product/{key}", ProductObject.class, key);
        } catch (NotFoundException ex) {
            return null;
        }

        product.setStatusCode(productObject.getProductStatusCode());
        product.setManufacturingDateTime(productObject.getManufacturingDateTime());
        product.setCreatedDateTime(productObject.getCreatedDateTime());

        //fill with ProductBase information.
        String productBaseId = productObject.getProductBaseId();
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject != null) {
            product.setProductBaseId(productBaseId);
            product.setBarcode(productBaseObject.getBarcode());
            product.setComment(productBaseObject.getComments());
            product.setName(productBaseObject.getName());
            product.setOrgId(productBaseObject.getOrgId());

            //fill with ProductCategory information.
            ProductCategory productCategory = getProductCategoryById(productBaseObject.getCategoryId());
            product.setProductCategory(productCategory);
        }
        return product;
    }

    public ProductCategory getProductCategoryById(String id) {
        if (id == null) {
            return null;
        }
        return dataAPIClient.get("productcategory/{id}", ProductCategory.class, id);
    }


    public Long getCommentsScore(String productBaseId) {
        return dataAPIClient.get("productcomments/avgscore/{id}", Long.class, productBaseId);
    }

    public Page<User> getFollowingUsersByProductBaseId(String productBaseId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("product_base_id", productBaseId)
                .append(pageable)
                .build();

        Page<UserProductFollowingObject> userFollowingList = dataAPIClient.getPaged("UserProductFollowing" + query,
                new ParameterizedTypeReference<List<UserProductFollowingObject>>() {
                });

        List<User> userList = new ArrayList<>();
        userFollowingList.forEach(item -> {
            userList.add(new User(userDomain.getUserById(item.getUserId())));
        });

        return new Page<>(userList, userFollowingList.getPage(), userFollowingList.getTotal());
    }

    //获取基本产品信息 - ProductBase
    public void fillProductName(List<Product> productList) {
        //fill product name
        HashMap<String, String> productHashMap = new HashMap<>();
        for (Product product : productList) {
            if (!productHashMap.containsKey(product.getProductBaseId())) {
                ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, product.getProductBaseId());
                if (productBaseObject != null) {
                    productHashMap.put(product.getProductBaseId(), productBaseObject.getName());
                    product.setProductName(productBaseObject.getName());
                }
            } else {
                product.setProductName(productHashMap.get(product.getProductName()));
            }
        }
    }

}
