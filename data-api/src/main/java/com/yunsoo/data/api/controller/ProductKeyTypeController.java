package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.api.wrap.LookupServiceWrap;
import com.yunsoo.data.service.service.LookupType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/17
 * Descriptions:
 */
@RestController
@RequestMapping("/productkeytype")
public class ProductKeyTypeController {

    @Autowired
    private LookupServiceWrap lookupServiceWrap;


    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public LookupObject getByCode(@PathVariable(value = "code") String code) {
        LookupObject object = lookupServiceWrap.getByCode(LookupType.ProductKeyType, code);
        if (object == null) {
            throw new NotFoundException("ProductKeyType not found");
        }
        return object;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LookupObject> getAll(@RequestParam(value = "active", required = false) Boolean active) {
        return lookupServiceWrap.getAll(LookupType.ProductKeyType, active);
    }
}
