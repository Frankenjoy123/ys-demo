package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.key.service.KeyService;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-08-24
 * Descriptions:
 */
@RestController
@RequestMapping("/key")
public class KeyController {

    @Autowired
    private KeyService keyService;


    @RequestMapping(value = "external/{partitionId}/{externalKey}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getExternalKey(@PathVariable(value = "partitionId") String partitionId,
                                 @PathVariable(value = "externalKey") String externalKey) {
        String key = keyService.translateExternalKey(partitionId, externalKey);
        if (key == null) {
            throw new NotFoundException("key not found");
        }
        return key;
    }

}
