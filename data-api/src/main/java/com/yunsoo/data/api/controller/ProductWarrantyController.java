package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductWarrantyObject;
import com.yunsoo.data.service.entity.ProductWarrantyEntity;
import com.yunsoo.data.service.repository.ProductWarrantyRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productwarranty")
public class ProductWarrantyController {

    @Autowired
    private ProductWarrantyRepository productWarrantyRepository;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductWarrantyObject create(@RequestBody ProductWarrantyObject productWarrantyObject) {
        ProductWarrantyEntity entity = toProductWarrantyEntity(productWarrantyObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        ProductWarrantyEntity newEntity = productWarrantyRepository.save(entity);

        return toProductWarrantyObject(newEntity);
    }

    private ProductWarrantyObject toProductWarrantyObject(ProductWarrantyEntity entity) {
        if (entity == null) {
            return null;
        }

        ProductWarrantyObject object = new ProductWarrantyObject();
        object.setId(entity.getId());
        object.setOrgId(entity.getOrgId());
        object.setStatusCode(entity.getStatusCode());
        object.setAssignPersonId(entity.getAssignPersonId());
        object.setAssignPersonName(entity.getAssignPersonName());
        object.setComments(entity.getComments());
        object.setProductBaseId(entity.getProductBaseId());
        object.setCustomerAddress(entity.getCustomerAddress());
        object.setCustomerCity(entity.getCustomerCity());
        object.setCustomerDescription(entity.getCustomerDescription());
        object.setCustomerMobile(entity.getCustomerMobile());
        object.setCustomerProvince(entity.getCustomerProvince());
        object.setCustomerName(entity.getCustomerName());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());

        return object;
    }

    private ProductWarrantyEntity toProductWarrantyEntity(ProductWarrantyObject object) {
        if (object == null) {
            return null;
        }

        ProductWarrantyEntity entity = new ProductWarrantyEntity();
        entity.setId(object.getId());
        entity.setOrgId(object.getOrgId());
        entity.setStatusCode(object.getStatusCode());
        entity.setAssignPersonId(object.getAssignPersonId());
        entity.setAssignPersonName(object.getAssignPersonName());
        entity.setComments(object.getComments());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setCustomerAddress(object.getCustomerAddress());
        entity.setCustomerCity(object.getCustomerCity());
        entity.setCustomerDescription(object.getCustomerDescription());
        entity.setCustomerMobile(object.getCustomerMobile());
        entity.setCustomerProvince(object.getCustomerProvince());
        entity.setCustomerName(object.getCustomerName());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());

        return entity;
    }
}
