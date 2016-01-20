package com.yunsoo.api.domain;

import com.yunsoo.api.dto.Lookup;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.LookupObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@Component
public class LookupDomain {

    @Autowired
    private LookupCacheDomain domain;

    public List<Lookup> getLookupListByType(LookupCodes.LookupType type, Boolean active){
        List<LookupObject> allList = domain.getAllLookupList();
        List<Lookup> returnList = new ArrayList<>();
        allList.forEach(item -> {
            if (type.equals(LookupCodes.LookupType.toLookupType(item.getTypeCode())) && (active == null || active.equals(item.getActive())))
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
