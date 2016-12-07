package com.yunsoo.api.rabbit.third.service;

import com.yunsoo.api.rabbit.client.ThirdApiClient;
import com.yunsoo.api.rabbit.third.dto.WeChatAccessToken;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yan on 12/7/2016.
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


}
