package com.yunsoo.third.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.third.Constant;
import com.yunsoo.third.dao.util.IpUtils;
import com.yunsoo.third.dto.wechat.*;
import com.yunsoo.third.service.WeChatService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by yan on 11/2/2016.
 */
@RestController
@RequestMapping("/wechat")
public class WeChatController {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private WeChatService weChatService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public WeChatUser getWeChatWebUser(@RequestParam("access_token") String accessToken,
                                       @RequestParam("openid") String openId) {
        return weChatService.getUserInfoInWeb(accessToken, openId);
    }

    @RequestMapping(value = "user/{openid}", method = RequestMethod.GET)
    public WeChatUser getWeChatUser(@PathVariable("openid") String openId,
                                    @RequestParam(value = "org_id", required = false) String orgId) {
        if (!StringUtils.hasText(orgId))
            orgId = Constant.YUNSU_ORGID;

        WeChatUser user = weChatService.getUserInfo(orgId, openId);
        if (user == null)
            throw new NotFoundException("wechat user not existed");

        return user;
    }


    @RequestMapping(value = "web_token", method = RequestMethod.GET)
    public WeChatWebAccessToken getWeChatWebAccessToken(@RequestParam("code") String code,
                                                        @RequestParam(value = "org_id", required = false) String orgId,
                                                        HttpServletRequest request) {
        if (!StringUtils.hasText(code))
            throw new BadRequestException("no wechat code in the request, url: " + request.getQueryString());

        if (!StringUtils.hasText(orgId))
            orgId = Constant.YUNSU_ORGID;

        WeChatWebAccessToken webToken = weChatService.getWebAccessToken(orgId,code);
        if (webToken == null)
            throw new BadRequestException("get web access token error");

        return webToken;
    }

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public WeChatAccessToken getWeChatToken(@RequestParam(value = "org_id", required = false) String orgId) {
        if (!StringUtils.hasText(orgId))
            orgId = Constant.YUNSU_ORGID;
        return new WeChatAccessToken(weChatService.getAccessTokenFromDB(orgId));
    }

    @RequestMapping(value = "openid_list", method = RequestMethod.GET)
    public WeChatOpenIdList getOpenIdList(@RequestParam("app_id") String appId,
                                          @RequestParam("app_secret") String appSecret) {

        return weChatService.getOpenIdList(appId, appSecret);
    }

    @RequestMapping(value = "jssdk/config", method = RequestMethod.GET)
    public WeChatConfig getWeChatConfig(@RequestParam("url") String url,
                                        @RequestParam(value = "nonce_str", required = false) String nonceString,
                                        @RequestParam(value = "org_id", required = false) String orgId) throws UnsupportedEncodingException {
        if (!StringUtils.hasText(orgId))
            orgId = Constant.YUNSU_ORGID;
        url = URLDecoder.decode(url, "UTF-8");
        return weChatService.getWechatConfig(orgId, url, nonceString);
    }

    @RequestMapping(value = "jssdk/pay_config", method = RequestMethod.GET)
    public Map<String, Object> getWeChatPayConfig(@RequestParam("pre_pay_id") String id,
                                                  @RequestParam(value = "org_id", required = false) String orgId,
                                                  @RequestParam("nonce_str") String nonceString,
                                                  @RequestParam("timestamp") long timestamp) throws UnsupportedEncodingException {
        Map<String, Object> values = weChatService.getWeChatPayConfig(orgId, id, timestamp, nonceString).getValues();
        return values;
    }

    @RequestMapping(value = "unified", method = RequestMethod.POST)
    public String getUnifiedOrder(@RequestBody WeChatOrderRequest orderRequest,
                                  HttpServletRequest request) {

        String ip = IpUtils.getIpFromRequest(request);
        if (!StringUtils.hasText(orderRequest.getOrgId()))
            orderRequest.setOrgId(Constant.YUNSU_ORGID);

        WeChatOrderResult result = null;
        try {
            result = weChatService.saveWeChatUnifiedOrder(orderRequest.getOpenId(), orderRequest.getOpenId(), ip, orderRequest.getNonceString(),
                    orderRequest.getProdName(), orderRequest.getId(), orderRequest.getPrice(), orderRequest.getNotifyUrl(), orderRequest.getOrderType());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get unified order error: " + e.getMessage());
        }
        if (result != null)
            return result.getPrepayId();
        else
            throw new BadRequestException("get wechat unified order error");

    }

    @RequestMapping(value = "redpack", method = RequestMethod.POST)
    public String sendRedPack(@RequestBody WeChatRedPackRequest redPackRequest, HttpServletRequest request) {
        String ip = IpUtils.getIpFromRequest(request);

        if (!StringUtils.hasText(redPackRequest.getOrgId()))
            redPackRequest.setOrgId(Constant.YUNSU_ORGID);

        try {
            return weChatService.sendRedPack(redPackRequest.getOrgId(), redPackRequest.getOpenId(), redPackRequest.getMchName(), redPackRequest.getId(),
                    redPackRequest.getPrice(), ip, redPackRequest.getRemark(), redPackRequest.getWishing(), redPackRequest.getActionName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("send wechat red pack to: "+ redPackRequest.getOpenId()+ " error: " + e.getMessage());
        }

        return null;
    }

    @RequestMapping(value = "notify", method = RequestMethod.GET)
    public WeChatNotifyResult handleWeChatNotifyResult(@RequestParam("notify_info") String notifyInfo, @RequestParam(value = "org_id", required = false) String orgId) {
        if (!StringUtils.hasText(notifyInfo))
            throw new BadRequestException("notify info is empty");

        if (!StringUtils.hasText(orgId))
            orgId = Constant.YUNSU_ORGID;

        return weChatService.handleNotifyResult(orgId, notifyInfo);
    }

    @RequestMapping(value = "pay/{order_id}", method = RequestMethod.GET)
    public String getPayResult(@PathVariable("order_id") String orderId,
                               @RequestParam(value = "nonce_str", required = false) String nonceString,
                               @RequestParam(value = "org_id", required = false) String orgId) {
        if (!StringUtils.hasText(orgId))
            orgId = Constant.YUNSU_ORGID;

        WeChatPayResult payResult = null;
        try {
            payResult = weChatService.queryPayResult(orgId, orderId, nonceString);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get wechat order detail info error, order id: " + orderId+", message: " + e.getMessage());
        }
        if (payResult != null)
            return payResult.getWeChatOrderId();
        else
            throw new BadRequestException("get wechat pay info error");
    }

    @RequestMapping(value = "qrcode/{id}", method = RequestMethod.POST)
    public String createQRCode(@PathVariable("id") String id, @RequestParam(value = "org_id", required = false) String orgId,
                               @RequestParam(value = "permanent", required = false) Boolean permanent) {

        if (!StringUtils.hasText(orgId))
            orgId = Constant.YUNSU_ORGID;

        WeChatQRCodeResponse response = weChatService.createQRCode(orgId, id, permanent == null ? false : permanent);
        if (response.getErrorCode() == 0)
            return response.getTicket();

        throw new BadRequestException("create QRCode failed: error message: " + response.getErrorMsg());
    }

    @RequestMapping(value = "server/config", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public WeChatServerConfig createWeChatServerConfig(@RequestBody @Valid WeChatServerConfig config) {
        if (config == null)
            throw new BadRequestException("wechat config could not be null");

        return weChatService.saveWeChatConfig(config);
    }

    @RequestMapping(value = "server/config/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public WeChatServerConfig updateWeChatServerConfig(@PathVariable("id")String orgId, @RequestBody @Valid WeChatServerConfig config) {
        if (config == null)
            throw new BadRequestException("wechat config could not be null");
        WeChatServerConfig existingConfig = weChatService.getWeChatServerConfigByOrgId(orgId);
        if(existingConfig.getAppId() == null)
            throw new NotFoundException("wechat config with orgId: " + orgId + " not found");

        config.setOrgId(orgId);
        return weChatService.saveWeChatConfig(config);
    }

    @RequestMapping(value = "server/config/{id}", method = RequestMethod.GET)
    public WeChatServerConfig getWeChatServerConfigById(@PathVariable("id")String orgId){
        return  weChatService.getWeChatServerConfigByOrgId(orgId);
    }



}
