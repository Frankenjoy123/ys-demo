package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductFileObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.ProductFileEntity;
import com.yunsoo.data.service.repository.ProductFileRepository;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 4/14/2015.
 */
@RestController
@RequestMapping("/productfile")
public class ProductFileController {

    @Autowired
    ProductFileRepository productFileRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody ProductFileObject productFileObject) {

        ProductFileEntity entity = new ProductFileEntity();
        BeanUtils.copyProperties(productFileObject, entity);

        ProductFileEntity newEntity = productFileRepository.save(entity);
        return newEntity.getId();
    }

    @RequestMapping(value = "/createby/{createby}/status/{status}/filetype/{filetype}/page/{page}", method = RequestMethod.GET)
      public List<ProductFileObject> get(@PathVariable(value = "createby") Long createby,
                                         @PathVariable(value = "status") Integer status,
                                         @PathVariable(value = "filetype") Integer filetype,
                                         @PathVariable(value = "page") Integer page) {

        Iterable<ProductFileEntity> entityList = null;
        if (status == 0)
            entityList = productFileRepository.findByCreateByAndFileTypeOrderByCreateDateDesc(createby, filetype, new PageRequest(page, 10));
        else
            entityList = productFileRepository.findByCreateByAndStatusAndFileTypeOrderByCreateDateDesc(createby, status, filetype, new PageRequest(page, 10));

        if (entityList == null) {
            throw new NotFoundException("ProductFile");
        }

        List<ProductFileObject> productFileObjects = new ArrayList<>();
        for (ProductFileEntity entity : entityList) {
            ProductFileObject productFileObject = new ProductFileObject();
            BeanUtils.copyProperties(entity, productFileObject);
            productFileObjects.add(productFileObject);
        }

        return productFileObjects;
    }

    @RequestMapping(value = "/countby/createby/{createby}/status/{status}/filetype/{filetype}", method = RequestMethod.GET)
    public Long getCount(@PathVariable(value = "createby") Long createby,
                                       @PathVariable(value = "status") Integer status,
                                       @PathVariable(value = "filetype") Integer filetype) {

        Long count = 0l;

        if (status == 0)
            count = productFileRepository.countByCreateByAndFileType(createby, filetype);
        else
            count = productFileRepository.countByCreateByAndStatusAndFileType(createby, status, filetype);

        return count;
    }
}
