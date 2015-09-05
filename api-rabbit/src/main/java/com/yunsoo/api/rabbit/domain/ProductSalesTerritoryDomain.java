package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.ProductSalesTerritoryObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2015/8/26
 * Descriptions:
 */

@Component
public class ProductSalesTerritoryDomain {

    @Autowired
    private RestClient dataAPIClient;


    public List<ProductSalesTerritoryObject> getProductSalesTerritoryByProductKey(String productKey) {
        return dataAPIClient.get("productsalesterritory?product_key={productKey}", new ParameterizedTypeReference<List<ProductSalesTerritoryObject>>() {
        }, productKey);
    }

    public List<ProductSalesTerritoryObject> getProductSalesTerritoryByOrgAgencyId(String orgAgencyId) {
        return dataAPIClient.get("productsalesterritory?org_agency_id={orgAgencyId}", new ParameterizedTypeReference<List<ProductSalesTerritoryObject>>() {
        }, orgAgencyId);
    }

    public ProductSalesTerritoryObject getProductSalesTerritoryById(String id) {
        return dataAPIClient.get("productsalesterritory/{id}", ProductSalesTerritoryObject.class, id);
    }

    public ProductSalesTerritoryObject createProductSalesTerritory(ProductSalesTerritoryObject productSalesTerritoryObject) {
        return dataAPIClient.post("productsalesterritory", productSalesTerritoryObject, ProductSalesTerritoryObject.class);
    }

    public void patchUpdate(ProductSalesTerritoryObject productSalesTerritoryObject) {
        dataAPIClient.put("productsalesterritory/{id}", productSalesTerritoryObject, productSalesTerritoryObject.getId());
    }

    public void deleteProductSalesTerritory(String id) {
        dataAPIClient.delete("productsalesterritory/{id}", id);
    }

}
