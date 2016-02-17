package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.OrganizationDomain;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.dto.ProductCategory;
import com.yunsoo.api.rabbit.dto.WebScanResult;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-02-17
 * Descriptions:
 */
@RestController
@RequestMapping("webScan")
public class WebScanController {

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private OrganizationDomain organizationDomain;

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public WebScanResult getScan(@PathVariable(value = "key") String key) {
        String productBaseId = null;
        WebScanResult result = getBasicInfo(productBaseId);


        return result;
    }

    @RequestMapping(value = "productbase/{id}", method = RequestMethod.GET)
    public WebScanResult getProductBaseScan(@PathVariable(value = "id") String productBaseId) {
        return getBasicInfo(productBaseId);
    }

    private WebScanResult getBasicInfo(String productBaseId) {
        WebScanResult result = new WebScanResult();

        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject == null) {
            throw new NotFoundException("product not found");
        }
        ProductCategory productCategory = productBaseDomain.getProductCategoryById(productBaseObject.getCategoryId());
        String productBaseDetails = productBaseDomain.getProductBaseDetails(productBaseObject.getOrgId(), productBaseObject.getId(), productBaseObject.getVersion());
        OrganizationObject organizationObject = organizationDomain.getById(productBaseObject.getOrgId());

        WebScanResult.Product product = new WebScanResult.Product();
        product.setId(productBaseObject.getId());
        product.setName(productBaseObject.getName());
        product.setCategory(productCategory);
        product.setDescription(productBaseObject.getDescription());
        product.setShelfLife(productBaseObject.getShelfLife());
        product.setShelfLifeInterval(productBaseObject.getShelfLifeInterval());
        product.setDetails(productBaseDetails);
        result.setProduct(product);

        if (organizationObject != null) {
            WebScanResult.Organization organization = new WebScanResult.Organization();
            organization.setId(organizationObject.getId());
            organization.setName(organizationObject.getName());
            organization.setStatusCode(organizationObject.getStatusCode());
            organization.setDescription(organizationObject.getDescription());
            organization.setDetails(organizationObject.getDetails());
            result.setOrganization(organization);
        }

        return result;
    }


}
