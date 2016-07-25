package com.yunsoo.api.domain;

import com.yunsoo.api.dto.AccessToken;
import com.yunsoo.api.dto.JsApi_Ticket;
import com.yunsoo.api.dto.WXOpenIdList;
import com.yunsoo.common.data.object.UserAccessTokenObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Admin on 6/29/2016.
 */
//@Component
public class WeixinAPIDomain {

    //@Autowired
    private RestClient dataAPIClient;

    private RestClient wxapiClient;

    public WeixinAPIDomain(){
        wxapiClient = new RestClient();
    }


    public UserAccessTokenObject getUserAccessTokenObject(String orgId, String appId, String secret) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();

        UserAccessTokenObject userAccessTokenObject = dataAPIClient.get("userAccessToken" + query, UserAccessTokenObject.class);
        if (userAccessTokenObject == null || userAccessTokenObject.getExpiredDatetime() == null || userAccessTokenObject.getExpiredDatetime().isBeforeNow()) {
            AccessToken accessToken = wxapiClient.get("token?grant_type=client_credential&appid=" + appId + "&secret=" + secret, AccessToken.class);
            if (accessToken != null && accessToken.getAccessToken() != null) {
                JsApi_Ticket jsApi_ticket = wxapiClient.get("ticket/getticket?access_token=" + accessToken.getAccessToken() + "&type=jsapi", JsApi_Ticket.class);
                if (jsApi_ticket != null && jsApi_ticket.getTicket() != null) {
                    UserAccessTokenObject newUserATObj = new UserAccessTokenObject();
                    newUserATObj.setId(null);
                    newUserATObj.setCreatedDateTime(DateTime.now());
                    newUserATObj.setAccessToken(accessToken.getAccessToken());
                    newUserATObj.setJsapiTicket(jsApi_ticket.getTicket());
                    newUserATObj.setOrgId(orgId);
                    newUserATObj.setExpiredDatetime(DateTime.now().plusSeconds(jsApi_ticket.getExpiresIn().intValue() - 200));

                    return dataAPIClient.post("userAccessToken", newUserATObj, UserAccessTokenObject.class);
                }
            }
        } else {
            return userAccessTokenObject;
        }

        return null;
    }

    public WXOpenIdList getOpenIds(String orgId, String appId, String secret){

        UserAccessTokenObject tokenObject = getUserAccessTokenObject(orgId, appId, secret);
        if(tokenObject!=null) {
            String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={token}&next_openid={id}";
            WXOpenIdList list = wxapiClient.get(url, WXOpenIdList.class, tokenObject.getAccessToken(), "");
            return list;
        }

        return null;
    }

    public void sendRedPack(String appId, String mchId, String privateKey, byte[] wxCert, byte[] wxKey){


        String url="https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
    }







}
