package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductTraceCommentsObject;
import com.yunsoo.data.service.entity.ProductTraceCommentsEntity;
import com.yunsoo.data.service.repository.ProductTraceCommentsRepository;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yan on 10/20/2016.
 */
@RestController
@RequestMapping("/producttracecomments")
public class ProductTraceCommentsController {

    @Autowired
    private ProductTraceCommentsRepository repository;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTraceCommentsObject save(@RequestBody ProductTraceCommentsObject comments){
        ProductTraceCommentsEntity entity = new ProductTraceCommentsEntity(comments);
        entity.setCreatedDateTime(DateTime.now());
        repository.save(entity);
        return toProductTraceCommentsObject(entity);
    }



    private ProductTraceCommentsObject toProductTraceCommentsObject(ProductTraceCommentsEntity entity){
        ProductTraceCommentsObject object = new ProductTraceCommentsObject();
        BeanUtils.copyProperties(entity, object);
        return object;
    }
}
