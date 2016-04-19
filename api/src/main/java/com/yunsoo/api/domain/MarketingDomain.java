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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${yunsoo.alipay.pid}")
    private String pid;

    @Value("${yunsoo.alipay.key}")
    private String key;

    @Value("${yunsoo.alipay.account_name}")
    private String alipayAccountName;

    @Value("${yunsoo.alipay.email}")
    private String alipayEmail;

    @Value("${yunsoo.alipay.notify_url}")
    private String alipayNotifyUrl;


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

    public MktDrawRuleObject getMktDrawRuleById(String id) {
        try {
            return dataAPIClient.get("marketing/Rule/{id}", MktDrawRuleObject.class, id);
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

    public List<MktDrawRuleObject> getRuleList(String marketingId) {
        return dataAPIClient.get("marketing/drawRule/{id}", new ParameterizedTypeReference<List<MktDrawRuleObject>>() {
        }, marketingId);

    }



    public Page<MarketingObject> getMarketingList(String orgId, List<String> orgIds, String status, String searchText, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("org_ids", orgIds).append("status", status)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("marketing" + query, new ParameterizedTypeReference<List<MarketingObject>>() {
        });
    }



    public MarketingObject createMarketing(MarketingObject marketingObject) {
        marketingObject.setId(null);
        return dataAPIClient.post("marketing", marketingObject, MarketingObject.class);
    }

    public Long countMarketingsByOrgId(String orgId) {
        Long totalQuantity = dataAPIClient.get("marketing/totalcount?org_id=" + orgId, Long.class);
        return totalQuantity;
    }

    public Long countProductKeysByMarketingId(String marketingId, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("marketing_id", marketingId)
                .append("start_time", startTime)
                .append("end_time", endTime)
                .build();

        Long totalQuantity = dataAPIClient.get("productkeybatch/sum/quantity" + query, Long.class);
        return totalQuantity;
    }

    public Long countDrawRecordsByMarketingId(String marketingId, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("marketing_id", marketingId)
                .append("start_time", startTime)
                .append("end_time", endTime)
                .build();

        Long totalQuantity = dataAPIClient.get("marketing/drawRecord/sum" + query, Long.class);
        return totalQuantity;
    }

    public Long countMktDrawPrizesByOrgId(String orgId) {
        Long totalQuantity = dataAPIClient.get("marketing/drawPrize/totalcount?org_id=" + orgId, Long.class);
        return totalQuantity;
    }


    public Long countDrawPrizeByDrawRuleId(String drawRuleId, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime) {

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("draw_rule_id", drawRuleId)
                .append("start_time", startTime)
                .append("end_time", endTime)
                .build();

        Long totalQuantity = dataAPIClient.get("marketing/drawPrize/sum" + query, Long.class);

        return totalQuantity;
    }

    public int countMarketing(List<String> orgIds, String status){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                 .append("org_ids", orgIds).append("status", status)
                .build();
        return dataAPIClient.get("marketing/count" + query, Integer.class);
    }

    public List<MarketingObject> statisticsMarketing(List<String> orgIds, List<String> status, Pageable pageable){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_ids", orgIds).append("status", status).append(pageable)
                .build();
        return dataAPIClient.get("marketing/statistics" + query, new ParameterizedTypeReference<List<MarketingObject>>() {
        });
    }

    public void updateMarketing(MarketingObject marketingObject){
        dataAPIClient.put("marketing/{id}", marketingObject, marketingObject.getId());
    }

    public void deleteMarketingById(String id) {
        dataAPIClient.delete("marketing/{id}", id);
        dataAPIClient.delete("marketing/drawRule/{id}", id);
    }

    public Page<MktDrawPrizeObject> getMktDrawPrizeByFilter(String marketingId, String accountType, String statusCode, org.joda.time.LocalDate startTime, org.joda.time.LocalDate endTime, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("marketing_id", marketingId)
                .append("account_type", accountType)
                .append("status_code", statusCode)
                .append("start_time", startTime)
                .append("end_time", endTime)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("marketing/drawPrize/marketing" + query, new ParameterizedTypeReference<List<MktDrawPrizeObject>>() {
        });

    }

    public void updateMktDrawRule(MktDrawRuleObject mktDrawRuleObject) {
        dataAPIClient.put("marketing/drawRule/{id}", mktDrawRuleObject, mktDrawRuleObject.getId());
    }

    public void updateMktDrawPrize(MktDrawPrizeObject mktDrawPrizeObject) {
        dataAPIClient.put("marketing/drawPrize", mktDrawPrizeObject, MktDrawPrizeObject.class);
    }


    public Map<String, String> getAlipayBatchTansferParameters() {

        AlipayParameters parameters = new AlipayParameters("batch_trans_notify", pid, key);
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

        parameters.put(ParameterNames.ACCOUNT_NAME, alipayAccountName);
        parameters.put(ParameterNames.BATCH_NO, batchNo);
        parameters.put(ParameterNames.BATCH_NUM, batchNum.toString());
        parameters.put(ParameterNames.BATCH_FEE, batchFee.toString());
        parameters.put(ParameterNames.EMAIL, alipayEmail);
        parameters.put(ParameterNames.DETAIL_DATA, detail_data);
        parameters.put(ParameterNames.NOTIFY_URL, alipayNotifyUrl);
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
