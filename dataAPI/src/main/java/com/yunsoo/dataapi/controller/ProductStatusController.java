package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.ResultWrapper;
import com.yunsoo.dataapi.factory.ResultFactory;
import com.yunsoo.service.ProductStatusService;
import com.yunsoo.service.contract.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/2/28.
 */
@RestController
@RequestMapping("/productstatus")
public class ProductStatusController {

    private final ProductStatusService productStatusService;

    @Autowired
    public ProductStatusController(ProductStatusService productStatusService) {
        this.productStatusService = productStatusService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductStatusById(@PathVariable(value = "id") Integer id) {
        ProductStatus productStatus = productStatusService.getById(id);
        return new ResponseEntity(productStatus, HttpStatus.OK);
    }

    @RequestMapping(value = "/all/{activeonly}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllProductStatusByActive(@PathVariable(value = "activeonly") Boolean activeonly) {
        List<ProductStatus> productStatusList = productStatusService.getAllProductKeyStatus(activeonly);
        return new ResponseEntity(productStatusList, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> create(@RequestBody ProductStatus productStatus) {
        int id = productStatusService.save(productStatus);
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody ProductStatus productStatus) {
        HttpStatus httpStatus = productStatusService.update(productStatus) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity(httpStatus);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultWrapper> delete(@PathVariable(value = "id") int id) {
        boolean result = productStatusService.delete(id);
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
    }
}
