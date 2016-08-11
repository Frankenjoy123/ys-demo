package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.ProductWarrantyDomain;
import com.yunsoo.api.rabbit.dto.ProductWarranty;
import com.yunsoo.common.data.object.ProductWarrantyObject;
import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/productwarranty")
public class ProductWarrantyController {

    @Autowired
    private ProductWarrantyDomain productWarrantyDomain;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductWarranty createProductWarranty(@RequestBody ProductWarranty productWarranty) {
        if (productWarranty == null) {
            throw new BadRequestException("productWarranty can not be null");
        }
        ProductWarrantyObject productWarrantyObject = productWarranty.toProductWarrantyObject();
        if (productWarrantyObject == null) {
            throw new BadRequestException("productWarrantyObject can not be null");
        }

        ProductWarrantyObject newObject = productWarrantyDomain.createProductWarranty(productWarrantyObject);
        return new ProductWarranty(newObject);
    }
}
