package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.api.util.ResponseEntityUtils;
import com.yunsoo.key.dto.KeyBatch;
import com.yunsoo.key.dto.Keys;
import com.yunsoo.key.service.KeyBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
@RestController
@RequestMapping("/keyBatch")
public class KeyBatchController {

    @Autowired
    private KeyBatchService keyBatchService;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public KeyBatch getById(@PathVariable(value = "id") String id) {
        KeyBatch keyBatch = keyBatchService.getById(id);
        if (keyBatch == null) {
            throw new NotFoundException("keyBatch not found by id: " + id);
        }
        return keyBatch;
    }

    @RequestMapping(value = "{id}/keys", method = RequestMethod.GET)
    public Keys getProductKeys(@PathVariable(value = "id") String id) {
        Keys keys = keyBatchService.getKeysById(id);
        if (keys == null) {
            throw new NotFoundException("keys not found by batchId: " + id);
        }
        return keys;
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getProductKeyBatchDetails(@PathVariable(value = "id") String id) throws IOException {
        ResourceInputStream details = keyBatchService.getKeyBatchDetails(id);
        if (details == null) {
            throw new NotFoundException("keyBatch details not found by id: " + id);
        }
        return ResponseEntityUtils.convert(details);
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.PUT)
    public void putProductKeyBatchDetails(@PathVariable(value = "id") String id, HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        long contentLength = request.getContentLengthLong();
        ServletInputStream inputStream = request.getInputStream();
        keyBatchService.saveKeyBatchDetails(id, new ResourceInputStream(inputStream, contentLength, contentType));
    }

}
