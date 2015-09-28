package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@Component
public class LookupDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupDomain.class);

    @Autowired
    private LookupCacheDomain domain;

    public List<Lookup> getLookupListByType(LookupCodes.LookupType type, Boolean active){
        List<LookupObject> allList = domain.getAllLookupList();
        List<Lookup> returnList = new ArrayList<>();
        allList.forEach(item -> {
            if(type.equals(LookupCodes.LookupType.toLookupType(item.getTypeCode())) && (active == null || (active!=null && active.equals(item.getActive()))))
                returnList.add(new Lookup(item));
        });

        return returnList;
    }

    public List<LookupObject> getAllLookup(){
        return domain.getAllLookupList();
    }

    public List<Lookup> getLookupListByType(LookupCodes.LookupType type){
       return getLookupListByType(type, null);
    }


    public void save(Lookup lookup){
        domain.saveLookup(Lookup.toLookupObj(lookup));
    }
}
