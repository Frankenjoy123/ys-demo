package com.yunsoo.auth.service;

import com.yunsoo.auth.dto.WeChatUser;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by yan on 12/22/2016.
 */
@Service
public class WeChatService {

    private RestClient thirdClient;

    @Autowired
    public WeChatService(@Value("${yunsoo.client.third_api.base_url}")String url){

        thirdClient = new RestClient(url);
    }

    public WeChatUser getUserInfo(String accessToken, String openId){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("access_token", accessToken)
                .append("openid", openId)
                .build();

        return thirdClient.get("wechat/user" + query, WeChatUser.class);
    }
}
