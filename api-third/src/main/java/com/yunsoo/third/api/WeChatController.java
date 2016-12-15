package com.yunsoo.third.api;

import com.yunsoo.common.exception.ErrorResultException;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.third.dao.entity.ThirdWeChatAccessTokenEntity;
import com.yunsoo.third.dao.util.IpUtils;
import com.yunsoo.third.dto.wechat.*;
import com.yunsoo.third.service.WeChatService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by yan on 11/2/2016.
 */
@RestController
@RequestMapping("/wechat")
public class WeChatController {
    @Autowired
    private WeChatService weChatService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public WeChatUser getWeChatWebUser(@RequestParam("access_token") String accessToken, @RequestParam("openid") String openId) {
        return weChatService.getUserInfo(accessToken, openId);
    }

    @RequestMapping(value = "web_token", method = RequestMethod.GET)
    public WeChatWebAccessToken getWeChatWebAccessToken(@RequestParam("code") String code, HttpServletRequest request) {
        if (!StringUtils.hasText(code))
            throw new BadRequestException("no wechat code in the request, url: " + request.getQueryString());

        WeChatWebAccessToken webToken = weChatService.getWebAccessToken(code);
        if (webToken == null)
            throw new BadRequestException("get web access token error");

        return webToken;
    }

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public WeChatAccessToken getWeChatToken(@RequestParam(value = "app_id", required = false) String appId) {
        if(StringUtils.hasText(appId))
            return new WeChatAccessToken(weChatService.getAccessTokenFromDB(appId, null));
        return new WeChatAccessToken(weChatService.getAccessTokenFromDB(null, null));
    }

    @RequestMapping(value = "openid_list", method = RequestMethod.GET)
    public WeChatOpenIdList getOpenIdList(@RequestParam("app_id") String appId, @RequestParam("app_secret") String appSecret){
        WeChatAccessToken token = getWeChatToken(appId);

        return weChatService.getOpenIdList(token);
    }

    @RequestMapping(value = "jssdk/config", method = RequestMethod.GET)
    public WeChatConfig getWeChatConfig(@RequestParam("url") String url,@RequestParam(value = "app_id", required = false) String appId,
                                        @RequestParam(value = "nonce_str", required = false) String nonceString) {
        return weChatService.getWechatConfig(null, url, nonceString);
    }

    @RequestMapping(value = "jssdk/pay_config", method = RequestMethod.GET)
    public Map<String, Object> getWeChatPayConfig(@RequestParam("pre_pay_id") String id,
                                                  @RequestParam("nonce_str") String nonceString,
                                                  @RequestParam("timestamp") long timestamp) throws UnsupportedEncodingException {
        Map<String, Object> values = weChatService.getWeChatPayConfig(id, timestamp, nonceString).getValues();
        return values;
    }

    @RequestMapping(value = "unified", method = RequestMethod.POST)
    public String getUnifiedOrder(@RequestBody WeChatOrderRequest orderRequest,
                                  HttpServletRequest request) {

        String ip = IpUtils.getIpFromRequest(request);
        WeChatOrderResult result = weChatService.saveWeChatUnifiedOrder(orderRequest.getOpenId(), ip, orderRequest.getNonceString(),
                orderRequest.getProdName(), orderRequest.getId(), orderRequest.getPrice());
        if (result != null)
            return result.getPrepayId();
        else
            throw new BadRequestException("get wecha unified order error");

    }

    @RequestMapping(value = "redpack", method = RequestMethod.POST)
    public boolean sendRedPack(@RequestBody WeChatRedPackRequest redPackRequest, HttpServletRequest request) {
        String ip = IpUtils.getIpFromRequest(request);
        return  weChatService.sendRedPack(redPackRequest.getOpenId(), redPackRequest.getMchName(), redPackRequest.getId(),
                redPackRequest.getPrice(), ip, redPackRequest.getRemark(), redPackRequest.getWishing(), redPackRequest.getActionName());
    }

}
