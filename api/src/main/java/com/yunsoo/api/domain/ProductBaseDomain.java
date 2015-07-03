package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductCategory;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@Component
public class ProductBaseDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private ProductCategoryDomain productCategoryDomain;


    public ProductBase getProductBaseById(String productBaseId) {
        ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        if (productBaseObject == null) {
            return null;
        }
        List<ProductKeyType> productKeyTypes = lookupDomain.getProductKeyTypes();
        Map<Integer, ProductCategoryObject> productCategoryObjectMap = productCategoryDomain.getProductCategoryMap();
        return toProductBase(productBaseObject, productKeyTypes, productCategoryObjectMap);
    }

    public List<ProductBase> getAllProductBaseByOrgId(String orgId) {
        ProductBaseObject[] objects = dataAPIClient.get("productbase?org_id={id}", ProductBaseObject[].class, orgId);
        List<ProductKeyType> productKeyTypes = lookupDomain.getProductKeyTypes();
        Map<Integer, ProductCategoryObject> productCategoryObjectMap = productCategoryDomain.getProductCategoryMap();
        return Arrays.stream(objects)
                .map(p -> toProductBase(p, productKeyTypes, productCategoryObjectMap))
                .collect((Collectors.toList()));
    }

    private ProductBase toProductBase(ProductBaseObject productBaseObject,
                                      List<ProductKeyType> productKeyTypes,
                                      Map<Integer, ProductCategoryObject> productCategoryObjectMap) {
        ProductBase productBase = new ProductBase();
        productBase.setId(productBaseObject.getId());
        productBase.setName(productBaseObject.getName());
        productBase.setBarcode(productBaseObject.getBarcode());
        productBase.setStatusCode(productBaseObject.getStatus());
        productBase.setComments(productBaseObject.getComment());
        productBase.setOrgId(productBaseObject.getOrgId());
        productBase.setShelfLife(productBaseObject.getShelfLife());
        productBase.setShelfLifeInterval(productBaseObject.getShelfLifeInterval());
        productBase.setCreatedDateTime(productBaseObject.getCreatedDateTime());
        productBase.setModifiedDateTime(productBaseObject.getModifiedDateTime());
        productBase.setCategoryId(productBaseObject.getCategoryId());
        productBase.setCategory(new ProductCategory(productCategoryDomain.getById(productBaseObject.getCategoryId(), productCategoryObjectMap)));
        if (productBaseObject.getProductKeyTypeCodes() != null) {
            productBase.setProductKeyTypeCodes(productBaseObject.getProductKeyTypeCodes());
            productBase.setProductKeyTypes(LookupObject.fromCodeList(productKeyTypes, productBaseObject.getProductKeyTypeCodes()));
        }
        return productBase;
    }
}
