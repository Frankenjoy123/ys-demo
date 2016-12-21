package com.yunsoo.api.rabbit.third.service;

import com.yunsoo.api.rabbit.client.ThirdApiClient;
import com.yunsoo.api.rabbit.third.dto.WeChatAccessToken;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by:   yan
 * Created on:   12/7/2016
 * Descriptions:
 */
@Service
public class WeChatService {

    @Autowired
    private ThirdApiClient thirdApiClient;

    public WeChatAccessToken getUserAccessTokenByAppId(String appId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("app_id", appId)
                .build();

        return  thirdApiClient.get("wechat/token" + query, WeChatAccessToken.class);
    }

    public Map<String, Object> getConfig(String appId, String url){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("app_id", appId).append("url", url)
                .build();
        return thirdApiClient.get("wechat/jssdk/config" + query, new ParameterizedTypeReference<Map<String, Object>>() {
        });
    }


}
