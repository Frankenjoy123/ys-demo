package com.yunsoo.data.api.wrap;

import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.data.service.service.LookupService;
import com.yunsoo.data.service.service.LookupType;
import com.yunsoo.data.service.service.contract.LookupItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/4/16
 * Descriptions:
 */
@Component
public class LookupServiceWrap {

    @Autowired
    private LookupService lookupService;

    public LookupObject getByCode(LookupType lookupType, String code) {
        LookupItem item = lookupService.getByCode(lookupType, code);
        if (item == null) {
            return null;
        }
        LookupObject object = new LookupObject();
        object.setCode(item.getCode());
        object.setName(item.getName());
        object.setDescription(item.getDescription());
        object.setActive(item.isActive());
        return object;
    }

    public List<LookupObject> getAll(LookupType lookupType, Boolean activeOnly) {
        List<LookupItem> productKeyTypes = activeOnly != null && activeOnly
                ? lookupService.getAllActive(lookupType)
                : lookupService.getAll(lookupType);
        return productKeyTypes.stream().map(p -> {
            LookupObject object = new LookupObject();
            object.setCode(p.getCode());
            object.setName(p.getName());
            object.setDescription(p.getDescription());
            object.setActive(p.isActive());
            return object;
        }).collect(Collectors.toList());
    }
}