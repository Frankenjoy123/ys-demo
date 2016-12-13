package com.yunsoo.api.domain;

import com.yunsoo.api.client.ThirdApiClient;
import com.yunsoo.api.dto.WeChatAccessToken;
import com.yunsoo.api.dto.WeChatOpenIdList;
import com.yunsoo.api.dto.WeChatOrderRequest;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 6/29/2016.
 */
@Component
public class WeChatAPIDomain {

    @Autowired
    private ThirdApiClient thirdApiClient;

    @Autowired
    private MarketingDomain marketingDomain;



    public WeChatAccessToken getUserAccessTokenByAppId(String appId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("app_id", appId)
                .build();

        return  thirdApiClient.get("wechat/token" + query, WeChatAccessToken.class);
    }

    public WeChatOpenIdList getOpenIds(String appId, String secret){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("app_id", appId).append("app_secret", secret)
                .build();
        return  thirdApiClient.get("wechat/openid_list" + query, WeChatOpenIdList.class);
    }

    public Map<String, Object> getConfig(String appId, String url){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("app_id", appId).append("url", url)
                .build();
        return  thirdApiClient.get("wechat/jssdk/config" + query, Map.class);
    }

    public Map<String, Object> getPayConfig(String appId, String openId, String marketingId, String nonceString, long timestamp){

        MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
        WeChatOrderRequest request = new WeChatOrderRequest();
        request.setId(marketingId);
        request.setNonceString(nonceString);
        request.setOpenId(openId);
        request.setPrice(new BigDecimal(marketingObject.getBudget()));
        request.setProdName(marketingObject.getName());
        String preOrderId = thirdApiClient.post("wechat/unified", request, String.class);

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("pre_pay_id", preOrderId)
                .append("nonce_str", nonceString)
                .append("timestamp", timestamp)
                .build();
        return thirdApiClient.get("jssdk/pay_config" + query, Map.class);
    }








}
