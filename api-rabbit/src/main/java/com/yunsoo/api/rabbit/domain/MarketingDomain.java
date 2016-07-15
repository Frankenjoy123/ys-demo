package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.MktDrawRule;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
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

    public MktDrawRuleObject getDrawRuleById(String id){
        return  dataAPIClient.get("marketing/Rule/{id}", MktDrawRuleObject.class, id);
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
    }

    public boolean createMobileOrder(String prizeId) {
        return  true;
        //return dataAPIClient.get("juhe/mobile/order?draw_prize_id=" + prizeId, Boolean.class);
    }

    public boolean createMobileDataFlow(String prizeId) {
        //return true;
        return dataAPIClient.get("juhe/mobile/data?draw_prize_id=" + prizeId, Boolean.class);
    }

    public boolean sendVerificationCode(String mobile, String templateName) {
        return dataAPIClient.get("juhe/verificationCode/{mobile}?temp_name={name}", Boolean.class, mobile, templateName);
    }

    public boolean validateVerificationCode(String mobile, String verificationCode) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("mobile", mobile)
                .append("verification_code", verificationCode)
                .build();

        return dataAPIClient.post("juhe/verifycode" + query, null, Boolean.class);
    }


    public String validateMobileType(String mobile) {
        String returnString = "";
        if (mobile == null || mobile.trim().length() != 11) {
            return "-1";
        }
        if (mobile.trim().substring(0, 3).equals("134") || mobile.trim().substring(0, 3).equals("135") ||
                mobile.trim().substring(0, 3).equals("136") || mobile.trim().substring(0, 3).equals("137")
                || mobile.trim().substring(0, 3).equals("138") || mobile.trim().substring(0, 3).equals("139") || mobile.trim().substring(0, 3).equals("150") ||
                mobile.trim().substring(0, 3).equals("151") || mobile.trim().substring(0, 3).equals("152")
                || mobile.trim().substring(0, 3).equals("157") || mobile.trim().substring(0, 3).equals("158") || mobile.trim().substring(0, 3).equals("159")
                || mobile.trim().substring(0, 3).equals("187") || mobile.trim().substring(0, 3).equals("188")) {
            returnString = LookupCodes.MobileType.CMCC;     // china mobile
        }
        if (mobile.trim().substring(0, 3).equals("130") || mobile.trim().substring(0, 3).equals("131") ||
                mobile.trim().substring(0, 3).equals("132") || mobile.trim().substring(0, 3).equals("156")
                || mobile.trim().substring(0, 3).equals("185") || mobile.trim().substring(0, 3).equals("186")) {
            returnString = LookupCodes.MobileType.CUCC;     //china unicom
        }
        if (mobile.trim().substring(0, 3).equals("133") || mobile.trim().substring(0, 3).equals("153") ||
                mobile.trim().substring(0, 3).equals("180") || mobile.trim().substring(0, 3).equals("189")) {
            returnString = LookupCodes.MobileType.CTCC;     //china telecom
        }
        if (returnString.trim().equals("")) {
            returnString = LookupCodes.MobileType.UNKNOWN;    // unknown type
        }
        return returnString;
    }

}
