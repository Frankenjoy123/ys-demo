package com.yunsoo.api.biz;

import com.yunsoo.api.config.YunsooConfiguration;
import com.yunsoo.api.config.YunsooYamlConfig;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.api.dto.basic.ProductCategory;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 产品域
 * Created by Zhe on 2015/3/17.
 */
@Component
public class ProductDomain {
    @Autowired
    private RestClient dataAPIClient;
    @Autowired
    public YunsooYamlConfig yunsooYamlConfig;

    //Retrieve Product Key, ProductBase entry and Product-Category entry from Backend.
    public Product getProductByKey(String Key) {
        Product product = new Product();
        product.setProductKey(Key);

        ProductObject productObject = null;
        try {
            productObject = dataAPIClient.get("product/{Key}", ProductObject.class, Key);
        } catch (NotFoundException ex) {
            //log ...该产品码对应的产品不存在！
            return null;
        } catch (Exception ex) {
            return null;
        }

        product.setStatusId(productObject.getProductStatusId());
        if (productObject.getManufacturingDateTime() != null) {
            product.setManufacturingDateTime(productObject.getManufacturingDateTime().toString());
        }
        product.setCreatedDateTime(productObject.getCreatedDateTime().toString());

        //fill with ProductBase information.
        int productBaseId = productObject.getProductBaseId();
        ProductBaseObject productBaseObject = dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        product.setProductBaseId(productBaseId);
        product.setBarcode(productBaseObject.getBarcode());
        product.setDescription(productBaseObject.getDescription());
        product.setDetails(productBaseObject.getDetails());
        product.setName(productBaseObject.getName());
        product.setManufacturerId(productBaseObject.getManufacturerId());

        //fill with ProductCategory information.
        ProductCategory productCategory = dataAPIClient.get("productcategory/model?id={id}", ProductCategory.class, productBaseObject.getSubCategoryId());
        product.setProductCategory(productCategory);

        return product;
    }

    //获取基本产品信息 - ProductBase
    public ProductBase getProductBase(int productBaseId) {
        ProductBase productBase = dataAPIClient.get("productbase/{id}", ProductBase.class, productBaseId);
        productBase.setThumbnailURL(yunsooYamlConfig.getDataapi_productbase_picture_basepath() + "id" + productBase.getId() + ".jpg");
        return productBase;
    }
}
