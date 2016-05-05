package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import com.yunsoo.common.data.object.MktDrawRecordObject;
import com.yunsoo.common.data.object.MktDrawRuleObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  : Haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */

@Component
public class MarketingDomain {

    @Autowired
    private RestClient dataAPIClient;

    public MarketingObject getMarketingById(String id) {
        if (id == null) {
            return null;
        }
        try {
            return dataAPIClient.get("marketing/{id}", MarketingObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public MktDrawRecordObject getMktDrawRecordByProductKey(String key) {
        return dataAPIClient.get("marketing/draw/{key}", MktDrawRecordObject.class, key);
    }

    public MktDrawPrizeObject getMktDrawPrizeByProductKey(String key) {
        return dataAPIClient.get("marketing/drawprize/{key}", MktDrawPrizeObject.class, key);
    }

    public MktDrawRecordObject createMktDrawRecord(MktDrawRecordObject mktDrawRecordObject) {
        mktDrawRecordObject.setId(null);
        mktDrawRecordObject.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("marketing/draw", mktDrawRecordObject, MktDrawRecordObject.class);
    }

    public MktDrawPrizeObject createMktDrawPrize(MktDrawPrizeObject mktDrawPrizeObject) {
        mktDrawPrizeObject.setCreatedDateTime(DateTime.now());
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.CREATED);
        return dataAPIClient.post("marketing/drawPrize", mktDrawPrizeObject, MktDrawPrizeObject.class);
    }

    public void updateMktDrawPrize(MktDrawPrizeObject mktDrawPrizeObject) {
        dataAPIClient.put("marketing/drawPrize", mktDrawPrizeObject, MktDrawPrizeObject.class);
    }


    public List<MktDrawRuleObject> getRuleList(String marketingId) {
        return dataAPIClient.get("marketing/drawRule/{id}", new ParameterizedTypeReference<List<MktDrawRuleObject>>() {
        }, marketingId);

    }

    public MktDrawRuleObject getMktRandomPrize(String marketId) {
        MarketingObject obj = dataAPIClient.get("marketing/{id}", MarketingObject.class, marketId);
        if (obj.getBalance() <= 0)
            return null;

        if ((obj.getStartDateTime() != null) && (obj.getEndDateTime() != null)) {
            DateTime currentDateTime = DateTime.now();
            if ((currentDateTime.compareTo(obj.getStartDateTime()) < 0) || (obj.getEndDateTime().compareTo(currentDateTime) < 0)) {
                return null;
            }
        }
        List<MktDrawRuleObject> ruleList = getRuleList(marketId);
        List<MktDrawRuleObject> newRuleList = new ArrayList<>();

        for (MktDrawRuleObject object : ruleList) {
            if ((object.getAvailableQuantity() != null) && (object.getAvailableQuantity() > 0)) {
                newRuleList.add(object);
            }
        }
        Long totalQuantity = dataAPIClient.get("productkeybatch/sum/quantity?marketing_id=" + marketId, Long.class);
        ;
        Integer sumQuantity = obj.getQuantity();
        if ((sumQuantity != null) || (sumQuantity > 0)) {
            totalQuantity = new Long(sumQuantity);
        }

        Map<Double, MktDrawRuleObject> prizeArray = new HashMap<>();

        for (MktDrawRuleObject rule : ruleList) {
            int ruleQuantity = (int) (rule.getProbability() * totalQuantity);
            for (int i = 0; i < ruleQuantity; i++) {
                double index = Math.floor(Math.random() * totalQuantity);
                while (prizeArray.containsKey(index)) {
                    index += 1;
                    if (prizeArray.size() >= totalQuantity)
                        break;
                    if (index > totalQuantity)
                        index = index - totalQuantity;
                }

                prizeArray.put(index, rule);
            }
        }

        double index = Math.floor(Math.random() * totalQuantity);
        if (prizeArray.containsKey(index) && obj.getBalance() >= prizeArray.get(index).getAmount())
            return prizeArray.get(index);
        else
            return null;

    }
}
