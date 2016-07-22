package com.yunsoo.api.domain;

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
    private RestClient dataApiClient;


    public List<ProductSalesTerritoryObject> getProductSalesTerritoryByProductKey(String productKey) {
        return dataApiClient.get("productsalesterritory?product_key={productKey}", new ParameterizedTypeReference<List<ProductSalesTerritoryObject>>() {
        }, productKey);
    }

    public List<ProductSalesTerritoryObject> getProductSalesTerritoryByOrgAgencyId(String orgAgencyId) {
        return dataApiClient.get("productsalesterritory?org_agency_id={orgAgencyId}", new ParameterizedTypeReference<List<ProductSalesTerritoryObject>>() {
        }, orgAgencyId);
    }

    public ProductSalesTerritoryObject getProductSalesTerritoryById(String id) {
        return dataApiClient.get("productsalesterritory/{id}", ProductSalesTerritoryObject.class, id);
    }

    public ProductSalesTerritoryObject createProductSalesTerritory(ProductSalesTerritoryObject productSalesTerritoryObject) {
        return dataApiClient.post("productsalesterritory", productSalesTerritoryObject, ProductSalesTerritoryObject.class);
    }

    public void patchUpdate(ProductSalesTerritoryObject productSalesTerritoryObject) {
        dataApiClient.put("productsalesterritory/{id}", productSalesTerritoryObject, productSalesTerritoryObject.getId());
    }

    public void deleteProductSalesTerritory(String id) {
        dataApiClient.delete("productsalesterritory/{id}", id);
    }

}