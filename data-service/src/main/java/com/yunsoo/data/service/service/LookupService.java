package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.LookupItem;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
public interface LookupService {

    LookupItem getByCode(LookupType lookupType, String code);

    List<LookupItem> getAll(LookupType lookupType);

    List<LookupItem> getByActive(LookupType lookupType, Boolean active);

    LookupItem save(LookupType lookupType, LookupItem lookupItem);

}
