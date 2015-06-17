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
        return toLookupObject(item);
    }

    public List<LookupObject> getAll(LookupType lookupType, Boolean active) {
        List<LookupItem> productKeyTypes = active == null
                ? lookupService.getAll(lookupType)
                : lookupService.getByActive(lookupType, active);
        return productKeyTypes.stream().map(this::toLookupObject).collect(Collectors.toList());
    }

    public LookupObject save(LookupType lookupType, LookupObject object) {
        LookupItem item = toLookupItem(object);
        LookupItem savedItem = lookupService.save(lookupType, item);
        return toLookupObject(savedItem);
    }

    private LookupObject toLookupObject(LookupItem item) {
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

    private LookupItem toLookupItem(LookupObject object) {
        if (object == null) {
            return null;
        }
        LookupItem item = new LookupItem();
        item.setCode(object.getCode());
        item.setName(object.getName());
        item.setDescription(object.getDescription());
        item.setActive(object.isActive());
        return item;
    }
}
