package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */

@Component
public class MarketingDomain {

    @Autowired
    private RestClient dataApiClient;

    public MarketingObject getMarketingById(String id) {
        if (id == null) {
            return null;
        }
        try {
            return dataApiClient.get("marketing/{id}", MarketingObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public List<MktDrawRecordObject> getMktDrawRecordList(String marketingId, String ysid) {
        return dataApiClient.get("marketing/draw?ys_id={ysId}&marketing_id={marketingId}", new ParameterizedTypeReference<List<MktDrawRecordObject>>() {
        }, ysid, marketingId);
    }

    public MktDrawRecordObject getMktDrawRecordByProductKey(String key) {
        return dataApiClient.get("marketing/draw/{key}", MktDrawRecordObject.class, key);
    }

    public MktDrawPrizeObject getMktDrawPrizeByProductKey(String key) {
        return dataApiClient.get("marketing/drawprize/{key}", MktDrawPrizeObject.class, key);
    }

    public MktDrawPrizeObject getMktDrawPrizeByPrizeId(String prizeId) {
        return dataApiClient.get("marketing/drawPrize/record/{id}", MktDrawPrizeObject.class, prizeId);
    }

    public MktPrizeContactObject getMktPrizeContactById(String id) {
        return dataApiClient.get("marketing/drawPrize/contact/{id}", MktPrizeContactObject.class, id);
    }

    public MktPrizeContactObject getMktPrizeContactByPrizeId(String prizeId) {
        return dataApiClient.get("marketing/drawPrize/contact/prize/{id}", MktPrizeContactObject.class, prizeId);
    }


    public MktConsumerRightObject getConsumerRightById(String id) {
        return dataApiClient.get("marketing/consumer/{id}", MktConsumerRightObject.class, id);
    }

    public MktConsumerRightObject getConsumerRightByProductKey(String productKey) {
        return dataApiClient.get("marketing/consumer/key/{key}", MktConsumerRightObject.class, productKey);
    }


    public MktDrawRuleObject getDrawRuleById(String id){
        return dataApiClient.get("marketing/Rule/{id}", MktDrawRuleObject.class, id);
    }


    public MktDrawRecordObject createMktDrawRecord(MktDrawRecordObject mktDrawRecordObject) {
        mktDrawRecordObject.setId(null);
        mktDrawRecordObject.setCreatedDateTime(DateTime.now());
        return dataApiClient.post("marketing/draw", mktDrawRecordObject, MktDrawRecordObject.class);
    }

    public MktDrawPrizeObject createMktDrawPrize(MktDrawPrizeObject mktDrawPrizeObject) {
        mktDrawPrizeObject.setCreatedDateTime(DateTime.now());
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.CREATED);
        return dataApiClient.post("marketing/drawPrize", mktDrawPrizeObject, MktDrawPrizeObject.class);
    }

    public MktPrizeContactObject createMktPrizeContact(MktPrizeContactObject mktPrizeContactObject) {
        mktPrizeContactObject.setCreatedDateTime(DateTime.now());
        mktPrizeContactObject.setModifiedDateTime(null);
        return dataApiClient.post("marketing/drawPrize/{id}/contact", mktPrizeContactObject, MktPrizeContactObject.class, mktPrizeContactObject.getMktPrizeId());
    }


    public void updateMktDrawPrize(MktDrawPrizeObject mktDrawPrizeObject) {
        dataApiClient.patch("marketing/drawPrize", mktDrawPrizeObject, MktDrawPrizeObject.class);
    }

    public void updateMktPrizeContact(MktPrizeContactObject mktPrizeContactObject) {
        dataApiClient.patch("marketing/drawPrize/{id}/contact", mktPrizeContactObject, mktPrizeContactObject.getMktPrizeId());
    }

    public void updateMktDrawRecord(MktDrawRecordObject object) {
        dataApiClient.patch("marketing/draw/{id}", object, object.getId());
    }


    public List<MktDrawRuleObject> getRuleList(String marketingId) {
        return dataApiClient.get("marketing/drawRule/{id}", new ParameterizedTypeReference<List<MktDrawRuleObject>>() {
        }, marketingId);

    }

    public List<MktDrawPrizeObject> getTop10PrizeList(String marketingId, String ysId) {
        if(StringUtils.hasText(ysId)) {
            List<MktDrawRecordObject> objectList = getMktDrawRecordList(marketingId, ysId);
            List<String> ids = new ArrayList<>();
            objectList.forEach(item->{
                ids.add(item.getId());
            });

            String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                    .append("record_ids", ids)
                    .append("status_code_in", Arrays.asList(LookupCodes.MktDrawPrizeStatus.SUBMIT, LookupCodes.MktDrawPrizeStatus.PAID, LookupCodes.MktDrawPrizeStatus.CREATED))
                    .build();

            return dataApiClient.get("marketing/drawPrize/{id}/top" + query, new ParameterizedTypeReference<List<MktDrawPrizeObject>>() {
            }, marketingId);
        }
        else
            return dataApiClient.get("marketing/drawPrize/{id}/top?status_code_in=" + LookupCodes.MktDrawPrizeStatus.PAID, new ParameterizedTypeReference<List<MktDrawPrizeObject>>() {
            }, marketingId);
    }

    public MktConsumerRightRedeemCodeObject getMktConsumerRightRedeemCodeByProductKey(String productKey) {
        return dataApiClient.get("marketing/consumer/redeemcode/{key}", MktConsumerRightRedeemCodeObject.class, productKey);
    }

    public MktConsumerRightRedeemCodeObject getRandomMktConsumerRightRedeemCodeByProductKey(String productKey, String drawRuleId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("draw_rule_id", drawRuleId)
                .build();

        return dataApiClient.get("marketing/consumer/redeemcode/generate/{key}" + query, MktConsumerRightRedeemCodeObject.class, productKey);
    }

    public List<String> getPrizedRuleListByUser(String marketingId, String userId, String ysId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("marketing_id", marketingId)
                .append("user_id", userId)
                .append("ys_id", ysId)
                .build();

        return dataApiClient.get("marketing/drawprize/rulelist" + query, new ParameterizedTypeReference<List<String>>() {
        });
    }

    public MktDrawRuleKeyObject getMktDrawRuleByProductKeyAndMarketingId(String productKey, String marketingId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("product_key", productKey)
                .append("marketing_id", marketingId)
                .build();

        return dataApiClient.get("marketing/drawRule/key" + query, MktDrawRuleKeyObject.class);
    }


    public MktDrawRuleObject getMktRandomPrize(String marketId, String scanRecordId, String productKey) {
        MarketingObject obj = dataApiClient.get("marketing/{id}", MarketingObject.class, marketId);
        if (obj.getBalance() <= 0)
            return null;

        if (LookupCodes.MktType.DRAW.equals(obj.getTypeCode()) || LookupCodes.MktType.SHAKE.equals(obj.getTypeCode()) || LookupCodes.MktType.REDPACKETS.equals(obj.getTypeCode())) {
            List<MktDrawRuleObject> ruleList = getRuleList(marketId);
            List<MktDrawRuleObject> newRuleList = new ArrayList<>();

            Long totalQuantity = dataApiClient.get("productkeybatch/sum/quantity?marketing_id=" + marketId, Long.class);
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
        }
        if (LookupCodes.MktType.ENVELOPE.equals(obj.getTypeCode())) {
            List<MktDrawRuleObject> envelopeRuleList = getRuleList(marketId);
            if (envelopeRuleList == null || envelopeRuleList.size() != 2 || envelopeRuleList.get(0).getAvailableQuantity() < 1) {
                return null;
            }
            Long totalEnvelopeQuantity = dataApiClient.get("productkeybatch/sum/quantity?marketing_id=" + marketId, Long.class);
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
        if (LookupCodes.MktType.DRAW01.equals(obj.getTypeCode())) {
            List<MktDrawRuleObject> originalRuleList = getRuleList(marketId);

            List<MktDrawRuleObject> ruleList = new ArrayList<>();
            List<MktDrawRuleObject> newRuleList = new ArrayList<>();
            List<String> prizedRuleList = new ArrayList<>();

            for (MktDrawRuleObject tempObject : originalRuleList) {
                if ((tempObject.getAvailableQuantity() != null) && (tempObject.getAvailableQuantity() > 0)) {
                    ruleList.add(tempObject);
                }
            }

            // query if the product key match the draw rule for key
            MktDrawRuleKeyObject mktDrawRuleKeyObject = getMktDrawRuleByProductKeyAndMarketingId(productKey, marketId);
            if ((mktDrawRuleKeyObject != null) && (StringUtils.hasText(mktDrawRuleKeyObject.getRuleId()))) {
                String ruleId = mktDrawRuleKeyObject.getRuleId();

                for (MktDrawRuleObject temp : ruleList) {
                    if ((temp.getId() != null) && (temp.getId().equals(ruleId))) {
                        return temp;
                    }
                }
                return null;
            }


            String userDeviceType = LookupCodes.UserDeviceType.NONANDROID;

            // check if the prize only for android user device type
            if (scanRecordId != null) {
                UserScanRecordObject userScanRecordObject = dataApiClient.get("userScanRecord/{id}", UserScanRecordObject.class, scanRecordId);
                if (userScanRecordObject != null) {
                    String userAgent = userScanRecordObject.getUserAgent();
                    if (userAgent != null) {
                        if (userAgent.contains(LookupCodes.UserDeviceType.ANDROID)) {
                            userDeviceType = LookupCodes.UserDeviceType.ANDROID;
                        }
                    }
                    prizedRuleList = getPrizedRuleListByUser(marketId, userScanRecordObject.getUserId(), userScanRecordObject.getYsid());
                }
            }

            Long totalWeight = new Long(0);

            // check if the prized already get by current user
            for (MktDrawRuleObject object : ruleList) {
                Boolean isRuleIdPrized = false;
                if (prizedRuleList.size() > 0) {
                    if (prizedRuleList.contains(object.getId())) {
                        isRuleIdPrized = true;
                    }
                }
                if (object.getAppliedEnv() == null) {
                    if (!object.getIsEqual() || !isRuleIdPrized) {
                        newRuleList.add(object);
                        if (object.getWeight() != null) {
                            totalWeight += object.getWeight();
                        }
                    }
                } else if (object.getAppliedEnv().equals(LookupCodes.UserDeviceType.ANDROID)) {
                    if (userDeviceType.equals(LookupCodes.UserDeviceType.ANDROID)) {
                        if (!object.getIsEqual() || !isRuleIdPrized) {
                            newRuleList.add(object);
                            if (object.getWeight() != null) {
                                totalWeight += object.getWeight();
                            }
                        }
                    }
                }
            }


            double prizeIndex = Math.floor(Math.random() * totalWeight);

            Double indexBefore = new Double(0);
            Double indexAfter = new Double(0);

            for (int i = 0; i < newRuleList.size(); i++) {
                indexAfter += newRuleList.get(i).getWeight();
                if ((prizeIndex >= indexBefore) && (prizeIndex < indexAfter)) {
                    return newRuleList.get(i);
                } else {
                    indexBefore = indexAfter;
                }
            }
            return null;
        }
        return null;
    }

    public boolean createMobileOrder(String prizeId) {
        //return  true;
        return dataApiClient.get("juhe/mobile/order?draw_prize_id=" + prizeId, Boolean.class);
    }

    public boolean createMobileDataFlow(String prizeId) {
        //return true;
        return dataApiClient.get("juhe/mobile/data?draw_prize_id=" + prizeId, Boolean.class);
    }

    public boolean sendVerificationCode(String mobile, String templateName) {
        return dataApiClient.get("juhe/verificationCode/{mobile}?temp_name={name}", Boolean.class, mobile, templateName);
    }

    public boolean validateVerificationCode(String mobile, String verificationCode) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("mobile", mobile)
                .append("verification_code", verificationCode)
                .build();

        return dataApiClient.post("juhe/verifycode" + query, null, Boolean.class);
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
