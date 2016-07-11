package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public MktConsumerRightObject getConsumerRightById(String id) {
        return dataAPIClient.get("marketing/consumer/{id}", MktConsumerRightObject.class, id);
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

        if (!LookupCodes.MktType.ENVELOPE.equals(obj.getTypeCode())) {
            List<MktDrawRuleObject> ruleList = getRuleList(marketId);
            List<MktDrawRuleObject> newRuleList = new ArrayList<>();

            Long totalQuantity = dataAPIClient.get("productkeybatch/sum/quantity?marketing_id=" + marketId, Long.class);
            ;
            Integer sumQuantity = obj.getQuantity();
            if ((sumQuantity != null) || (sumQuantity > 0)) {
                totalQuantity = new Long(sumQuantity);
            }

            for (MktDrawRuleObject object : ruleList) {
                if ((object.getAvailableQuantity() != null) && (object.getAvailableQuantity() > 0)) {
                    newRuleList.add(object);
                } else if (object.getAvailableQuantity() == 0) {
                    totalQuantity = totalQuantity - object.getTotalQuantity();
                }
            }

            double prizeIndex = Math.floor(Math.random() * totalQuantity);

            Double indexBefore = new Double(0);
            Double indexAfter = new Double(0);

            for (int i = 0; i < newRuleList.size(); i++) {
                indexAfter += newRuleList.get(i).getTotalQuantity();
                if ((prizeIndex >= indexBefore) && (prizeIndex < indexAfter)) {
                    if (obj.getBalance() >= newRuleList.get(i).getAmount()) {
                        return newRuleList.get(i);
                    }
                } else {
                    indexBefore = indexAfter;
                }
            }
            return null;
        } else {
            List<MktDrawRuleObject> envelopeRuleList = getRuleList(marketId);
            if (envelopeRuleList == null || envelopeRuleList.size() != 2 || envelopeRuleList.get(0).getAvailableQuantity() < 1) {
                return null;
            }
            Long totalEnvelopeQuantity = dataAPIClient.get("productkeybatch/sum/quantity?marketing_id=" + marketId, Long.class);
            ;
            Integer sumEnvelopeQuantity = obj.getQuantity();
            if ((sumEnvelopeQuantity != null) || (sumEnvelopeQuantity > 0)) {
                totalEnvelopeQuantity = new Long(sumEnvelopeQuantity);
            }
            double prizeEnvelopeIndex = Math.floor(Math.random() * totalEnvelopeQuantity);
            if (prizeEnvelopeIndex <= new Double(envelopeRuleList.get(0).getTotalQuantity())) {
                Double amountMin = envelopeRuleList.get(0).getAmount();
                Double amountMax = envelopeRuleList.get(1).getAmount();
                if (amountMin > amountMax) {
                    Double temp = amountMin;
                    amountMin = amountMax;
                    amountMax = temp;
                }
                Double randomAmount = amountMin + Math.floor(Math.random() * (amountMax - amountMin));
                MktDrawRuleObject envelopeMktDrawRuleObject = envelopeRuleList.get(0);
                envelopeMktDrawRuleObject.setAmount(randomAmount);
                return envelopeMktDrawRuleObject;

            } else {
                return null;
            }
        }


        /*Map<Double, MktDrawRuleObject> prizeArray = new HashMap<>();
        Double index = new Double(0);

        for (MktDrawRuleObject rule : newRuleList) {

            int ruleQuantity = rule.getTotalQuantity();
            for(int i = 0; i < ruleQuantity; i++) {
                if(prizeArray.size() >= totalQuantity)
                    break;
                prizeArray.put(index, rule);
                index += 1;
            }

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

        if (prizeArray.containsKey(prizeIndex) && obj.getBalance() >= prizeArray.get(index).getAmount())
            return prizeArray.get(index);
        else
            return null;*/

    }
}
