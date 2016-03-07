package com.yunsoo.api.domain;

import com.yunsoo.api.payment.AlipayParameters;
import com.yunsoo.api.payment.ParameterNames;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public MktDrawPrizeObject getMktDrawPrizeById(String id) {
        try {
            return dataAPIClient.get("marketing/drawPrize/record/{id}", MktDrawPrizeObject.class, id);
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

    public void updateMktDrawPrize(MktDrawPrizeObject mktDrawPrizeObject) {
        dataAPIClient.put("marketing/drawPrize", mktDrawPrizeObject, MktDrawPrizeObject.class);
    }


    public Map<String, String> getAlipayBatchTansferParameters() {

        AlipayParameters parameters = new AlipayParameters("batch_trans_notify", "2088121812016826", "e0opacdva54q9todmujp0vc5pwqlcj10");
        //order info
        String batchNo = DateTime.now().toString("yyyyMMddHHmmss") + getRandomString(10);
        String detail_data = "";
        Integer batchNum = 0;
        BigDecimal batchFee = new BigDecimal("0");
        List<MktDrawPrizeObject> mktDrawPrizeObjectList = dataAPIClient.get("marketing/alipay_batchtransfer", new ParameterizedTypeReference<List<MktDrawPrizeObject>>() {
        });
        if (mktDrawPrizeObjectList.size() > 0) {
            for (MktDrawPrizeObject object : mktDrawPrizeObjectList) {
                String drawRecordId = object.getDrawRecordId();
                String account = object.getPrizeAccount();
                String accountName = object.getPrizeAccountName();
                Integer amount = object.getAmount();
                detail_data += drawRecordId + "^" + account + "^" + accountName + "^" + amount.toString() + "^" + "alipay prize" + "|";
                batchNum++;
                batchFee = batchFee.add(new BigDecimal(amount.toString()));
            }
        }

        detail_data = detail_data.substring(0, detail_data.length() - 1);

        parameters.put(ParameterNames.ACCOUNT_NAME, "广州云溯科技有限公司");
        parameters.put(ParameterNames.BATCH_NO, batchNo);
        parameters.put(ParameterNames.BATCH_NUM, batchNum.toString());
        parameters.put(ParameterNames.BATCH_FEE, batchFee.toString());
        parameters.put(ParameterNames.EMAIL, "zhe@yunsu.co");
        parameters.put(ParameterNames.DETAIL_DATA, detail_data);
        parameters.put(ParameterNames.NOTIFY_URL, "http://183.128.236.12:6080/marketing/alipay/notify");
        parameters.put(ParameterNames.PAY_DATE, DateTime.now().toString("yyyyMMdd"));
        return parameters.toMap();
    }

    //generate random ID
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            buffer.append(base.charAt(number));
        }
        return buffer.toString();
    }


}
