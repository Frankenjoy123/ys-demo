package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import com.yunsoo.common.data.object.MktDrawRecordObject;
import com.yunsoo.common.data.object.MktDrawRuleObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

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
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
        dataAPIClient.put("marketing/drawPrize", mktDrawPrizeObject, MktDrawPrizeObject.class);
    }

    public List<MktDrawRuleObject> getRuleList(String marketingId){
        return dataAPIClient.get("marketing/drawRule/{id}", new ParameterizedTypeReference<List<MktDrawRuleObject>>(){}, marketingId);

    }

    public int getMktRandomPrize(String marketId){
        MarketingObject obj = dataAPIClient.get("marketing/{id}", MarketingObject.class, marketId);
        List<MktDrawRuleObject> ruleList = getRuleList(marketId);
        List<MktDrawPrizeObject> exitingPrizeList = dataAPIClient.get("marketing/drawPrize/{id}", new ParameterizedTypeReference<List<MktDrawPrizeObject>>(){}, marketId);

        Map<Double, Integer> prizeArray = new HashMap<>();
        Double buget = obj.getBudget();

        Long totalQuantity = dataAPIClient.get("productkeybatch/sum/quantity?marketing_id=" + marketId, Long.class);

        for(MktDrawRuleObject rule : ruleList){
            int ruleQuantity = (int)(rule.getProbability() * totalQuantity);
            for(int i=0; i<ruleQuantity; i++){
                prizeArray.put(Math.floor(Math.random() * totalQuantity), rule.getAmount());
            }
        }

        double index = Math.floor(Math.random() * totalQuantity);
        if(prizeArray.containsKey(index) && obj.getBudget() >= prizeArray.get(index))
            return prizeArray.get(index);
        else
            return 0;

    }
}
