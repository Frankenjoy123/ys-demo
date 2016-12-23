package com.yunsoo.api.rabbit.third.service;

import com.yunsoo.api.rabbit.client.ThirdApiClient;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.third.dto.WeChatAccessToken;
import com.yunsoo.api.rabbit.third.dto.WeChatRedPackRequest;
import com.yunsoo.api.rabbit.third.dto.WeChatUser;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Random;

/**
 * Created by:   yan
 * Created on:   12/7/2016
 * Descriptions:
 */
@Service
public class WeChatService {

    @Autowired
    private ThirdApiClient thirdApiClient;

    @Autowired
    private ProductDomain productDomain;

    public WeChatAccessToken getUserAccessTokenByAppId(String appId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("app_id", appId)
                .build();

        return thirdApiClient.get("wechat/token" + query, WeChatAccessToken.class);
    }

    public Map<String, Object> getConfig(String appId, String url) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("app_id", appId).append("url", url)
                .build();
        return thirdApiClient.get("wechat/jssdk/config" + query, new ParameterizedTypeReference<Map<String, Object>>() {
        });
    }

    public boolean sendRedPack(WeChatRedPackRequest redPackRequest) {
        return thirdApiClient.post("wechat/redpack", redPackRequest, boolean.class);
    }

    public String createQRCode(String key){
        long millis = new DateTime().getMillis();
        Random random = new Random();
        Number scenarioId = millis * 1000 + random.nextInt(1000);
        productDomain.saveKeyToRadis(scenarioId, key);
        return thirdApiClient.post("wechat/qrcode/{id}", null, String.class, scenarioId);
    }

    public WeChatUser getWeChatUser(String openId){
        return thirdApiClient.get("wechat/user/{id}", WeChatUser.class, openId);
    }


}
