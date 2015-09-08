package com.yunsoo.api.controller;

import com.yunsoo.api.domain.LookupDomain;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.common.data.LookupCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/19
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productkeytype")
public class ProductKeyTypeController {

    @Autowired
    private LookupDomain lookDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Lookup> getAllActive() {
        return lookDomain.getLookupListByType(LookupCodes.LookupType.ProductKeyType, true);
    }

}
