package com.yunsoo.api.key.service;

import com.yunsoo.api.client.KeyApiClient;
import com.yunsoo.api.key.dto.KeyBatch;
import com.yunsoo.api.key.dto.KeySerialNo;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yan on 12/29/2016.
 */
@Service
public class KeySerialNoService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;

    public List<KeySerialNo> getByFilter(String orgId){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();
        return keyApiClient.get("keySerialNo" + query, new ParameterizedTypeReference<List<KeySerialNo>>() {
        });
    }

    public void update(KeySerialNo serialNo){
        keyApiClient.patch("keySerialNo?org_id={org}", serialNo, serialNo.getOrgId());
    }
}
