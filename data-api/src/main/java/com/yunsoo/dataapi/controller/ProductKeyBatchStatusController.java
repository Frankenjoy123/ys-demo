package com.yunsoo.dataapi.controller;

import com.yunsoo.service.ProductKeyBatchStatusService;
import com.yunsoo.service.contract.ProductKeyBatchStatus;
import com.yunsoo.service.contract.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/11
 * Descriptions:
 */
@RestController
@RequestMapping("/productkeybatchstatus")
public class ProductKeyBatchStatusController {

    private final ProductKeyBatchStatusService productKeyBatchStatusService;

    @Autowired
    public ProductKeyBatchStatusController(ProductKeyBatchStatusService productKeyBatchStatusService) {
        this.productKeyBatchStatusService = productKeyBatchStatusService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyBatchStatus> getAllProductStatus(@RequestParam(value = "active", required = false) Boolean active) {
        return active != null && active
                ? productKeyBatchStatusService.getAllActive()
                : productKeyBatchStatusService.getAll();
    }

}
