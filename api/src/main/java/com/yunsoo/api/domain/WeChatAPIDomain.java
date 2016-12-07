package com.yunsoo.api.domain;

import com.yunsoo.api.client.ThirdApiClient;
import com.yunsoo.api.dto.WeChatAccessToken;
import com.yunsoo.api.dto.WeChatOpenIdList;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Admin on 6/29/2016.
 */
@Component
public class WeChatAPIDomain {

    @Autowired
    private ThirdApiClient thirdApiClient;

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








}
