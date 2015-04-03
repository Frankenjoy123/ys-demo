package com.yunsoo.dataapi.controller;

import com.yunsoo.service.ProductStatusService;
import com.yunsoo.service.contract.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/2/28
 * Descriptions:
 */
@RestController
@RequestMapping("/productstatus")
public class ProductStatusController {

    @Autowired
    private ProductStatusService productStatusService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductStatus getById(@PathVariable(value = "id") Integer id) {
        return productStatusService.getById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductStatus> getAll(@RequestParam(value = "active", required = false) Boolean active) {
        return productStatusService.getAll(active);
    }

//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    public ResponseEntity<ResultWrapper> create(@RequestBody ProductStatus productStatus) {
//        int id = productStatusService.save(productStatus);
//        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/update", method = RequestMethod.PUT)
//    public ResponseEntity update(@RequestBody ProductStatus productStatus) {
//        HttpStatus httpStatus = productStatusService.update(productStatus) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
//        return new ResponseEntity(httpStatus);
//    }
//
//    @RequestMapping(value = "/deletePermanantly/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<ResultWrapper> deletePermanantly(@PathVariable(value = "id") int id) {
//        boolean result = productStatusService.deletePermanantly(id);
//        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
//    }
}
