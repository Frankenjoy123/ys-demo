package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.api.dto.basic.ProductCategory;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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


    //Retrieve Product key, ProductBase entry and Product-Category entry from Backend.
    public Product getProductByKey(String key) {
        Product product = new Product();
        product.setProductKey(key);

        ProductObject productObject = null;
        try {
            productObject = dataAPIClient.get("product/{key}", ProductObject.class, key);
        } catch (NotFoundException ex) {
            //log ...该产品码对应的产品不存在！
            return null;
        }

        product.setStatusCode(productObject.getProductStatusCode());
        product.setManufacturingDateTime(productObject.getManufacturingDateTime());
        product.setCreatedDateTime(productObject.getCreatedDateTime().toString());

        //fill with ProductBase information.
        String productBaseId = productObject.getProductBaseId();
        ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        product.setProductBaseId(productBaseId);
        product.setBarcode(productBaseObject.getBarcode());
//        product.setDescription(productBaseObject.getDescription());
        product.setComment(productBaseObject.getComment());
        product.setName(productBaseObject.getName());
        product.setOrgId(productBaseObject.getOrgId());

        //fill with ProductCategory information.
        ProductCategory productCategory = getProductCategoryById(productBaseObject.getCategoryId());
        product.setProductCategory(productCategory);

        return product;
    }

    public void activeProduct(String key) {

    }

    public ProductCategory getProductCategoryById(Integer id) {
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
        ProductBase productBase = fromProductBaseObject(productBaseObject, lookupDomain.getAllProductKeyTypes());
//        productBase.setThumbnailURL(yunsooYamlConfig.getDataapi_productbase_picture_basepath() + "id" + productBase.getId() + ".jpg");
        return productBase;
    }

    public List<ProductBase> getAllProductBaseByOrgId(String orgId) {
        ProductBaseObject[] objects = dataAPIClient.get("productbase?orgId={id}", ProductBaseObject[].class, orgId);
        if (objects == null) {
            return null;
        }
        List<ProductKeyType> productKeyTypes = lookupDomain.getAllProductKeyTypes();
        return Arrays.stream(objects).map(p -> fromProductBaseObject(p, productKeyTypes)).collect((Collectors.toList()));
    }

    public ProductBase fromProductBaseObject(ProductBaseObject productBaseObject, List<ProductKeyType> productKeyTypes) {
        ProductBase productBase = new ProductBase();
        productBase.setId(productBaseObject.getId());
        productBase.setName(productBaseObject.getName());
        productBase.setBarcode(productBaseObject.getBarcode());
        productBase.setStatus(productBaseObject.getStatus());
        productBase.setComment(productBaseObject.getComment());
        productBase.setOrgId(productBaseObject.getOrgId());
        productBase.setShelfLife(productBaseObject.getShelfLife());
        productBase.setShelfLifeInterval(productBaseObject.getShelfLifeInterval());
        productBase.setCreatedDateTime(productBaseObject.getCreatedDateTime());
        productBase.setModifiedDateTime(productBaseObject.getModifiedDateTime());

        productBase.setCategory(getProductCategoryById(productBaseObject.getCategoryId()));
        if (productBaseObject.getProductKeyTypeCodes() != null) {
            productBase.setProductKeyTypeCodes(productBaseObject.getProductKeyTypeCodes());
            productBase.setProductKeyTypes(LookupObject.fromCodeList(productKeyTypes, productBaseObject.getProductKeyTypeCodes()));
        }
        return productBase;
    }
}
