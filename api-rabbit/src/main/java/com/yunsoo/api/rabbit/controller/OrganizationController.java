package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.auth.service.AuthOrganizationService;
import com.yunsoo.api.rabbit.domain.OrgBrandDomain;
import com.yunsoo.api.rabbit.domain.OrganizationDomain;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.dto.Organization;
import com.yunsoo.api.rabbit.dto.ProductBase;
import com.yunsoo.api.rabbit.key.dto.Product;
import com.yunsoo.api.rabbit.key.service.ProductService;
import com.yunsoo.api.rabbit.util.PageUtils;
import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Zhe on 2015/4/2.
 * Allow anonymous call.
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {

    @Autowired
    private AuthOrganizationService authOrganizationService;

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private OrgBrandDomain orgBrandDomain;

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Organization getOrganizationById(@PathVariable(value = "id") String id) {
        com.yunsoo.api.rabbit.auth.dto.Organization org = authOrganizationService.getById(id);
        if (org == null) {
            throw new NotFoundException("organization not found by [id: " + id + "]");
        }
        return new Organization(org);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Organization> getByFilter() {
        return organizationDomain.getOrganizationList();
    }

    @RequestMapping(value = "{id}/productbase", method = RequestMethod.GET)
    public List<ProductBase> getProductsByOrgId(@PathVariable(value = "id") String orgId, Pageable pageable, HttpServletResponse response) {
        Page<ProductBaseObject> productBasePage = productBaseDomain.getProductBaseByOrgId(orgId, pageable);

        return PageUtils.response(response, productBasePage.map(ProductBase::new), pageable != null);
    }

    @RequestMapping(value = "/{key}/brand/contactMobile", method = RequestMethod.GET)
    public String getBrandPhoneById(@PathVariable(value = "key") String key) {

        Product product = productService.getProductByKey(key);
        if (product == null) {
            throw new NotFoundException("product not found");
        }
        ProductBaseObject productBaseObject = getProductBaseById(product.getProductBaseId());
        OrgBrandObject brandObject = orgBrandDomain.getOrgBrandById(productBaseObject.getOrgId());

        if (brandObject == null) {
            throw new NotFoundException("brand organization not found by [id: " + productBaseObject.getOrgId() + "]");
        }

        return brandObject.getContactMobile();
    }

    @RequestMapping(value = "{id}/logo", method = RequestMethod.GET)
    public ResponseEntity<?> getOrganizationLogo(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "image_name", required = false) String imageName) {
        ResourceInputStream resourceInputStream = organizationDomain.getLogoImage(id, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("logo not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));
    }


    private ProductBaseObject getProductBaseById(String productBaseId) {
        if (!ObjectIdGenerator.validate(productBaseId)) {
            throw new NotFoundException("product not found");
        }
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject == null) {
            throw new NotFoundException("product not found");
        }
        return productBaseObject;
    }

}
