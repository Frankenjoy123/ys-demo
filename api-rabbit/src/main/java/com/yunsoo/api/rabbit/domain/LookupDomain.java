package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.Lookup;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@Component
public class LookupDomain {

    @Autowired
    private RestClient dataAPIClient;

    public List<Lookup> getAllLookupList(Boolean active){
        return dataAPIClient.get("lookup" + formatActive(active), new ParameterizedTypeReference<List<Lookup>>() {
        });
    }

    private String formatActive(Boolean active) {
        return new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
    }


    public List<Lookup> getLookupListByType(LookupCodes.LookupType type, Boolean active){
        List<Lookup> allList = getAllLookupList(active);
        List<Lookup> returnList = new ArrayList<>();
        allList.forEach(item -> {
            if(LookupCodes.LookupType.valueOf(item.getTypeCode()).equals(type))
                returnList.add(item);
        });

        return returnList;
    }

    public List<Lookup> getLookupListByType(LookupCodes.LookupType type){
        return getLookupListByType(type, null);
    }

}
