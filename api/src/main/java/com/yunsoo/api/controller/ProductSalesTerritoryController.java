package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductSalesTerritoryDomain;
import com.yunsoo.api.dto.ProductSalesTerritory;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.ProductSalesTerritoryObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Haitao
 * Created on  : 2015/8/26
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productsalesterritory")
public class ProductSalesTerritoryController {

    @Autowired
    private ProductSalesTerritoryDomain productSalesTerritoryDomain;


    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductSalesTerritory getProductSalesTerritoryById(@PathVariable(value = "id") String id) {
        ProductSalesTerritoryObject object = productSalesTerritoryDomain.getProductSalesTerritoryById(id);
        if (object == null) {
            throw new NotFoundException("product sales territory not found by [id: " + id + "]");
        }
        return new ProductSalesTerritory(object);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductSalesTerritory> getProductSalesTerritoryByFilter(
            @RequestParam(value = "product_key", required = false) String productKey,
            @RequestParam(value = "org_agency_id", required = false) String orgAgencyId) {

        List<ProductSalesTerritoryObject> productSalesTerritoryObjects;
        if (!StringUtils.isEmpty(productKey)) {
            productSalesTerritoryObjects = productSalesTerritoryDomain.getProductSalesTerritoryByProductKey(productKey);
        } else if (!StringUtils.isEmpty(orgAgencyId)) {
            productSalesTerritoryObjects = productSalesTerritoryDomain.getProductSalesTerritoryByOrgAgencyId(orgAgencyId);
        } else {
            throw new BadRequestException("at least need one filter parameter of product_key or org_agency_id");
        }
        return productSalesTerritoryObjects.stream().map(ProductSalesTerritory::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductSalesTerritoryObject createProductComments(@RequestBody ProductSalesTerritory productSalesTerritory) {
        if (productSalesTerritory == null) {
            throw new BadRequestException("productSalesTerritory can not be null");
        }
        ProductSalesTerritoryObject productSalesTerritoryObject = new ProductSalesTerritoryObject();
        productSalesTerritoryObject.setProductKey(productSalesTerritory.getProductKey());
        productSalesTerritoryObject.setOrgAgencyId(productSalesTerritory.getOrgAgencyId());
        if (productSalesTerritory.getCreatedAccountId() == null) {
            String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
            productSalesTerritoryObject.setCreatedAccountId(currentAccountId);
        }
        if (productSalesTerritory.getCreatedDateTime() == null) {
            productSalesTerritoryObject.setCreatedDateTime(DateTime.now());
        }
        return productSalesTerritoryDomain.createProductSalesTerritory(productSalesTerritoryObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteProductSalesTerritoryById(@PathVariable(value = "id") String id) {
        ProductSalesTerritoryObject object = productSalesTerritoryDomain.getProductSalesTerritoryById(id);
        if (object == null) {
            throw new NotFoundException("product comment not found by [id: " + id + "]");
        }
        productSalesTerritoryDomain.deleteProductSalesTerritory(id);
    }


    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void updateProductSalesTerritory(@PathVariable(value = "id") String id,
                                            @RequestBody ProductSalesTerritory productSalesTerritory) {

        ProductSalesTerritoryObject productSalesTerritoryObject = productSalesTerritoryDomain.getProductSalesTerritoryById(id);
        if (productSalesTerritoryObject == null) {
            throw new BadRequestException("The product sales territory not found");
        }

        productSalesTerritoryObject.setProductKey(productSalesTerritory.getProductKey());
        productSalesTerritoryObject.setOrgAgencyId(productSalesTerritory.getOrgAgencyId());
        if (productSalesTerritory.getModifiedAccountId() == null) {
            String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
            productSalesTerritoryObject.setModifiedAccountId(currentAccountId);
        }
        if (productSalesTerritory.getModifiedDateTime() == null) {
            productSalesTerritoryObject.setModifiedDateTime(DateTime.now());
        }
        productSalesTerritoryDomain.patchUpdate(productSalesTerritoryObject);
    }

}
