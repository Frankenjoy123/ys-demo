package com.yunsoo.auth.service;

import com.yunsoo.auth.dto.weChatUser;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 10/28/2016.
 */
@Service
public class WechatService {

    private RestClient wechatClient;

    public WechatService(){
        wechatClient = new RestClient("https://api.weixin.qq.com/");
        List<HttpMessageConverter<?>> messageConverterList = wechatClient.getRestTemplate().getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : messageConverterList) {
            if(httpMessageConverter instanceof StringHttpMessageConverter){
                List<MediaType> mediaTypeList = new ArrayList<>();
                mediaTypeList.addAll(httpMessageConverter.getSupportedMediaTypes());
                mediaTypeList.add(new MediaType("text", "plain", Charset.forName("UTF-8")));
                ((StringHttpMessageConverter) httpMessageConverter).setSupportedMediaTypes(mediaTypeList);
                break;
            }
        }

    }

    public weChatUser getUserInfo(String accessToken, String openId){
        String user = wechatClient.get("sns/userinfo?access_token={accessToken}&openid={openId}&lang=zh_CN",
                String.class, accessToken, openId);
        try {
            user = new String(user.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            return com.yunsoo.common.util.ObjectUtils.toObject(user, weChatUser.class);
        } catch (IOException e) {
            return null;
        }

    }


}
