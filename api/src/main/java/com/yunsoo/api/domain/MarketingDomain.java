package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.MktDrawRuleObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by  : Haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
@Component
public class MarketingDomain {

    @Autowired
    private RestClient dataAPIClient;

    public MktDrawRuleObject createMktDrawRule(MktDrawRuleObject mktDrawRuleObject) {
        mktDrawRuleObject.setId(null);
        mktDrawRuleObject.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("marketing/drawRule", mktDrawRuleObject, MktDrawRuleObject.class);
    }

    public MarketingObject getMarketingById(String id) {
        try {
            return dataAPIClient.get("marketing/{id}", MarketingObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }


}
