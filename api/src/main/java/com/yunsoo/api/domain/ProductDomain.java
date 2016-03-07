package com.yunsoo.api.domain;

import com.yunsoo.api.dto.Product;
import com.yunsoo.api.dto.ProductCategory;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@Component
public class ProductDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductCategoryDomain productCategoryDomain;


    public ProductObject getProduct(String key) {
        try {
            return dataAPIClient.get("product/{key}", ProductObject.class, key);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    @Deprecated
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
        product.setComment(productBaseObject.getComments());
        product.setName(productBaseObject.getName());
        product.setOrgId(productBaseObject.getOrgId());

        //fill with ProductCategory information.
        ProductCategory productCategory = new ProductCategory(productCategoryDomain.getById(productBaseObject.getCategoryId()));
        product.setProductCategory(productCategory);

        return product;
    }

    public void activeProduct(String key) {
        ProductObject productObject = new ProductObject();
        productObject.setProductStatusCode(LookupCodes.ProductStatus.ACTIVATED);
        dataAPIClient.patch("product/{key}", productObject, key);
    }

    public void deleteProduct(String key) {
        ProductObject productObject = new ProductObject();
        productObject.setProductStatusCode(LookupCodes.ProductStatus.DELETED);
        dataAPIClient.patch("product/{key}", productObject, key);
    }

    public void patchUpdateProduct(String key, ProductObject productObject) {
        dataAPIClient.patch("product/{key}", productObject, key);
    }

    @Deprecated
    public boolean batchDeleteProducts(String[] productKeys, String orgId) throws AccessDeniedException {
        boolean batchResult = false;
        List<String> productBaseIds = new ArrayList<>();
        try {
            for (String productKey : productKeys) {
                ProductObject productObject = dataAPIClient.get("product/{key}", ProductObject.class, productKey);
                String productBaseId = productObject.getProductBaseId();
                if (!productBaseIds.contains(productBaseId)) {
                    productBaseIds.add(productBaseId);
                }
            }
            for (String productBaseId : productBaseIds) {
                ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
                if (!productBaseObject.getOrgId().equals(orgId)) {
                    throw new AccessDeniedException("You do not have access to delete product keys not belong to your organization.");
                }
            }
            batchResult = dataAPIClient.post("/product/batchdelete/file", productKeys, Boolean.class);
        } catch (NotFoundException ex) {
            log.error("could not found key!");
        }
        return batchResult;
    }
}
