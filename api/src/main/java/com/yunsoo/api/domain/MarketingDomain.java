package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import com.yunsoo.common.data.object.MktDrawRuleObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public Page<MarketingObject> getMarketingByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("marketing" + query, new ParameterizedTypeReference<List<MarketingObject>>() {
        });
    }

    public MarketingObject createMarketing(MarketingObject marketingObject) {
        marketingObject.setId(null);
        return dataAPIClient.post("marketing", marketingObject, MarketingObject.class);
    }

    public void deleteMarketingById(String id) {
        dataAPIClient.delete("marketing/{id}", id);
        dataAPIClient.delete("marketing/drawRule/{id}", id);
    }

    public Page<MktDrawPrizeObject> getMktDrawPrizeByFilter(String marketingId, String accountType, String statusCode, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("marketing_id", marketingId)
                .append("account_type", accountType)
                .append("status_code", statusCode)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("marketing/drawPrize/marketing" + query, new ParameterizedTypeReference<List<MktDrawPrizeObject>>() {
        });

    }


}
