package com.yunsoo.api.rabbit.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.rabbit.dto.Product;
import com.yunsoo.api.rabbit.dto.ProductBase;
import com.yunsoo.api.rabbit.dto.ProductBaseDetails;
import com.yunsoo.api.rabbit.dto.ProductCategory;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@Component
public class ProductDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private ProductCommentsDomain commentsDomain;

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
        ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        product.setProductBaseId(productBaseId);
        product.setBarcode(productBaseObject.getBarcode());
        product.setComment(productBaseObject.getComments());
        product.setName(productBaseObject.getName());
        product.setOrgId(productBaseObject.getOrgId());

        //fill with ProductCategory information.
        ProductCategory productCategory = getProductCategoryById(productBaseObject.getCategoryId());
        product.setProductCategory(productCategory);

        return product;
    }

    public ProductCategory getProductCategoryById(String id) {
        if (id == null) {
            return null;
        }
        return dataAPIClient.get("productcategory/{id}", ProductCategory.class, id);
    }

    //获取基本产品信息 - ProductBase
    public ProductBase getProductBaseById(String productBaseId) {
        ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        if (productBaseObject == null) {
            return null;
        }
        ProductBase productBase = convertFromProductBaseObject(productBaseObject);
        //set the average score
        productBase.setCommentsScore(dataAPIClient.get("productcomments/avgscore/{id}", Long.class, productBaseId));
        //set the hotline and purchase info
        ResourceInputStream stream =  dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/details.json", productBase.getOrgId(), productBaseId, productBase.getVersion());
        try {
            ProductBaseDetails detail = new ObjectMapper().readValue(stream, ProductBaseDetails.class);
            productBase.setDetail(detail);
        }
        catch (Exception ex){
            LOGGER.error("get detail data from s3 failed.", ex);
        }

        //todo set detailUrl property
        return productBase;
    }

    // get product details
    public ResourceInputStream getProductBaseDetailsById(String productBaseId) {
        try {
            ProductBase productBase = getProductBaseById(productBaseId);
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/details.json", productBase.getOrgId(), productBaseId, productBase.getVersion());
        } catch (NotFoundException ex) {
            return null;
        }
    }

    // get product images
    public ResourceInputStream getProductBaseImage(String productBaseId, String imageName) {
        try {
            ProductBase productBase = getProductBaseById(productBaseId);
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}", productBase.getOrgId(), productBaseId, productBase.getVersion(), imageName);
        } catch (NotFoundException ex) {
            return null;
        }
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

    public List<ProductBase> getAllProductBaseByOrgId(int orgId) {
        ProductBaseObject[] objects = dataAPIClient.get("productbase?orgId={id}", ProductBaseObject[].class, orgId);
        if (objects == null) {
            return null;
        }
       // List<Lookup> productKeyTypes = lookupDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType);
        return Arrays.stream(objects).map(p -> convertFromProductBaseObject(p)).collect((Collectors.toList()));
    }

    public ProductBase convertFromProductBaseObject(ProductBaseObject productBaseObject) {
        ProductBase productBase = new ProductBase();
        productBase.setId(productBaseObject.getId());
        productBase.setVersion(productBaseObject.getVersion());
        productBase.setName(productBaseObject.getName());
        productBase.setDescription(productBaseObject.getComments());
        productBase.setOrgId(productBaseObject.getOrgId());
        productBase.setShelfLife(productBaseObject.getShelfLife());
        productBase.setShelfLifeInterval(productBaseObject.getShelfLifeInterval());
        productBase.setCategory(getProductCategoryById(productBaseObject.getCategoryId()));

        return productBase;
    }
}
