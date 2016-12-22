package com.yunsoo.third.service;

import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.ObjectUtils;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.third.dao.entity.ThirdWeChatAccessTokenEntity;
import com.yunsoo.third.dao.repository.WeChatAccessTokenRepository;
import com.yunsoo.third.dao.util.XmlUtils;
import com.yunsoo.third.dto.wechat.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by:   Admin
 * Created on:   6/29/2016
 * Descriptions:
 */
@Service
public class WeChatService {
    Log logger = LogFactory.getLog(this.getClass());

    @Value("${yunsoo.wechat.app_id}")
    private String appId;

    @Value("${yunsoo.wechat.app_secret}")
    private String appSecret;

    @Value("${yunsoo.wechat.private_key}")
    private String privateKey;

    @Value("${yunsoo.wechat.mch_id}")
    private String mchId;

    private RestClient weChatClient;
    private RestTemplate weChatMchClient;

    @Autowired
    private WeChatAccessTokenRepository weChatAccessTokenRepository;

   // @Autowired
    public WeChatService(@Value("${yunsoo.wechat.key_file}") String keyFile,
                         @Value("${yunsoo.wechat.mch_id}") String mchId){
        weChatClient = new RestClient("https://api.weixin.qq.com/");
        try {
            initWeChatMchClient(keyFile, mchId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    public void save(ThirdWeChatAccessTokenEntity entity){
        weChatAccessTokenRepository.save(entity);
    }

    public ThirdWeChatAccessTokenEntity saveWeChatAccessTicketFromWeChat(String appId, String secret){
        if(!StringUtils.hasText(appId))
            appId = this.appId;

        if(!StringUtils.hasText(secret))
            secret = this.appSecret;

        WeChatToken accessToken = weChatClient.get("cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret, WeChatToken.class);
        if (accessToken.getErrorCode() == 0) {
            WeChatJSTicket jsApi_ticket = weChatClient.get("cgi-bin/ticket/getticket?access_token=" + accessToken.getAccessToken() + "&type=jsapi", WeChatJSTicket.class);
            if (jsApi_ticket.getErrorCode() == 0) {
                ThirdWeChatAccessTokenEntity token = weChatAccessTokenRepository.findTop1ByAppIdOrderByUpdatedDateTimeDesc(appId);
                if(token == null) {
                    token = new ThirdWeChatAccessTokenEntity();
                    token.setUpdatedDateTime(DateTime.now());
                    token.setAppId(appId);
                    token.setAccessToken(accessToken.getAccessToken());
                    token.setJsapiTicket(jsApi_ticket.getTicket());
                    token.setExpiredDatetime(DateTime.now().plusSeconds(jsApi_ticket.getExpiresIn().intValue() - 200));
                    weChatAccessTokenRepository.save(token);
                }
                else{
                    token.setUpdatedDateTime(DateTime.now());
                    token.setAccessToken(accessToken.getAccessToken());
                    token.setJsapiTicket(jsApi_ticket.getTicket());
                    token.setExpiredDatetime(DateTime.now().plusSeconds(jsApi_ticket.getExpiresIn().intValue() - 200));
                    weChatAccessTokenRepository.save(token);
                }

                return token;
            }
        }

        return null;
    }


    public WeChatUser getUserInfo(String accessToken, String openId){
        String user = weChatClient.get("sns/userinfo?access_token={accessToken}&openid={openId}&lang=zh_CN",
                String.class, accessToken, openId);
        try {
            user = new String(user.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            return com.yunsoo.common.util.ObjectUtils.toObject(user, WeChatUser.class);
        } catch (IOException e) {
            return null;
        }

    }


    public WeChatWebAccessToken getWebAccessToken(String code) {
        String url = "sns/oauth2/access_token?" +
                "appid=" + appId +
                "&secret=" + appSecret +
                "&code=" + code +
                "&grant_type=authorization_code";

        String webToken = weChatClient.get(url, String.class);
        try {
            webToken = new String(webToken.getBytes("ISO-8859-1"), "UTF-8");
            WeChatWebAccessToken token = ObjectUtils.toObject(webToken, WeChatWebAccessToken.class);
            if(token.getErrorCode() > 0){
                logger.error("get wechat access token error: " + token.getErrorMsg());
                return null;
            }

            return token;
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

    public ThirdWeChatAccessTokenEntity getAccessTokenFromDB(String appId, String appSecret){
        if(!StringUtils.hasText(appId))
            appId = this.appId;
        ThirdWeChatAccessTokenEntity token = weChatAccessTokenRepository.findTop1ByAppIdOrderByUpdatedDateTimeDesc(appId);
        if (token == null || token.getExpiredDatetime() == null || token.getExpiredDatetime().isBeforeNow()) {
            return saveWeChatAccessTicketFromWeChat(appId, appSecret);
        }

        return token;
    }

    public WeChatConfig getWechatConfig(String appId, String url, String nonceStr){
        WeChatConfig config = new WeChatConfig();
        if(!StringUtils.hasText(appId))
            appId = this.appId;
        config.setAppId(appId);
        if(!StringUtils.hasText(nonceStr))
            config.setNoncestr(RandomUtils.generateString(30));
        else
            config.setNoncestr(nonceStr);
        config.setTimestamp(new DateTime().getMillis());
        ThirdWeChatAccessTokenEntity token = weChatAccessTokenRepository.findTop1ByAppIdOrderByUpdatedDateTimeDesc(appId);
        String jsapi_ticket = token.getJsapiTicket();
        String param = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + config.getNoncestr() + "&timestamp=" + config.getTimestamp() + "&url=" + url;
        config.setSignature(HashUtils.sha1HexString(param));
        return config;
    }

    public WeChatData getWeChatPayConfig(String prePayId, long timeStamp, String nonceStr) throws UnsupportedEncodingException {
        WeChatData data = new WeChatData();
        data.setValue("appId", appId);
        data.setValue("timeStamp", timeStamp);
        data.setValue("nonceStr", nonceStr);
        data.setValue("package", "prepay_id=" + prePayId);
        data.setValue("signType", "MD5");
        data.addSign(privateKey);
        return data;
    }

    public WeChatOrderResult saveWeChatUnifiedOrder(String openId, String ip, String nonceString, String productName, String orderId, BigDecimal price, String notifyUrl, String orderType) {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        WeChatData data = new WeChatData();
        data.setValue("appid", appId);
        data.setValue("mch_id", mchId);
        data.setValue("nonce_str", nonceString);
        data.setValue("body", productName);
        data.setValue("out_trade_no", orderId);
        data.setValue("total_fee", price.intValue());
        data.setValue("spbill_create_ip", ip);
        data.setValue("trade_type", "JSAPI");
        data.setValue("notify_url", notifyUrl);
        data.setValue("attach", orderType);
        data.setValue("openid", openId);
        data.setValue("sign_type", "MD5");
        try {
            data.addSign(privateKey);
            String result = weChatMchClient.postForObject(url, data.toXml(), String.class);
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            WeChatOrderResult orderResult = XmlUtils.convertXmlToObject(result, WeChatXmlBaseType.class, WeChatOrderResult.class);
            if("SUCCESS".equals(orderResult.getReturnCode()) &&  "SUCCESS".equals(orderResult.getResultCode()))
                return orderResult;
            else{
                if(!"SUCCESS".equals(orderResult.getReturnCode()))
                    logger.error("get wechat unified pay error: " + orderResult.getReturnMsg());
                else
                    logger.error("get wechat unified pay error: code: " + orderResult.getErrCode() + ", message: " + orderResult.getErrCodeDes());
            }
        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error(e);
        }

        return null;
    }

    public WeChatPayResult queryPayResult(String orderId, String nonceString){
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        WeChatData data = new WeChatData();
        data.setValue("appid", appId);
        data.setValue("mch_id", mchId);
        data.setValue("nonce_str", StringUtils.hasText(nonceString)? nonceString : RandomUtils.generateString(30));
        data.setValue("out_trade_no", orderId);
        data.setValue("sign_type", "MD5");
        try {
            data.addSign(privateKey);
            String result = weChatMchClient.postForObject(url, data.toXml(), String.class);
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            WeChatPayResult payResult = XmlUtils.convertXmlToObject(result, WeChatXmlBaseType.class, WeChatPayResult.class);
            if("SUCCESS".equals(payResult.getReturnCode()) &&  "SUCCESS".equals(payResult.getResultCode()))
                return payResult;
            else{
                if(!"SUCCESS".equals(payResult.getReturnCode()))
                    logger.error("get wechat pay result error: " + payResult.getReturnMsg());
                else
                    logger.error("get wechat pay result error: code: " + payResult.getErrCode() + ", message: " + payResult.getErrCodeDes());
            }
        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error(e);
        }

        return null;
    }

    public WeChatNotifyResult handleNotifyResult(String notifyInfo){
        WeChatData data = new WeChatData();
        try {
            Map notifyResultMap = XmlUtils.convertXmlToObject(notifyInfo, HashMap.class);
            data.setValues(notifyResultMap);
            String existingSign = convertToStr(notifyResultMap.get("sign") == null);
            String sign = data.getSign(privateKey);
            if(existingSign.equals(sign)){
                WeChatNotifyResult result = new WeChatNotifyResult();
                result.setOrderType(convertToStr(data.getValue("attach")));
                result.setOrderId(convertToStr(data.getValue("out_trade_no")));
                result.setWeChatOrderId(convertToStr(data.getValue("transaction_id")));
                return  result;
            }
            else{
                throw new ForbiddenException("sign error");
            }


        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error(e);
        }


        return null;
    }

    public WeChatQRCodeResponse createQRCode(String id, boolean permanent){
        ThirdWeChatAccessTokenEntity tokenEntity = getAccessTokenFromDB(null, null);
        String url = "cgi-bin/qrcode/create?access_token={token}";
        WeChatQRCodeRequest request = new WeChatQRCodeRequest();
        if(permanent){
            request.setActionName("QR_LIMIT_SCENE");
            request.setActionInfo("", id);
        }
        else{
            request.setActionName("QR_SCENE");
            request.setActionInfo(id, "");
        }

        request.setExpiresSeconds(7*24*60*60); //7days


        return weChatClient.post(url, request, WeChatQRCodeResponse.class, tokenEntity.getAccessToken());
    }



    public boolean sendRedPack(String openId, String mchName, String orderId,
                            BigDecimal price, String ip, String remark, String wishing, String actionName) {
        String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
        WeChatData data = new WeChatData();
        data.setValue("wxappid", appId);
        data.setValue("mch_id", mchId);
        data.setValue("nonce_str", RandomUtils.generateString(30));
        data.setValue("send_name", mchName);
        data.setValue("mch_billno", orderId);
        data.setValue("re_openid", openId);
        data.setValue("total_amount", price.multiply(new BigDecimal(100)).intValue());
        data.setValue("total_num", 1);
        data.setValue("wishing", wishing);
        data.setValue("client_ip", ip);
        data.setValue("remark", remark);
        data.setValue("act_name", actionName);
        data.setValue("scene_id", "PRODUCT_2");

        try {
            data.addSign(privateKey);
            String result = weChatMchClient.postForObject(url, data.toXml(), String.class);
          //  result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            WeChatXmlBaseType requestResult = XmlUtils.convertXmlToObject(result, WeChatXmlBaseType.class);
            if("SUCCESS".equals(requestResult.getReturnCode()) &&  "SUCCESS".equals(requestResult.getResultCode()))
                return true;
            else{
                if(!"SUCCESS".equals(requestResult.getReturnCode()))
                    logger.error("send wechat redpack error: " + requestResult.getReturnMsg());
                else
                    logger.error("send wechat redpack error: code: " + requestResult.getErrCode() + ", message: " + requestResult.getErrCodeDes());
            }
        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error(e);
        }

        return false;
    }


    private void initWeChatMchClient(String keyFile, String mchId) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        try (FileInputStream instream = new FileInputStream(new File(keyFile))) {
            keyStore.load(instream, mchId.toCharArray());
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mchId.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpclient);
        weChatMchClient = new RestTemplate(factory);

        List<HttpMessageConverter<?>> listMessageConvert = weChatMchClient.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : listMessageConvert) {
            if(httpMessageConverter instanceof StringHttpMessageConverter){
                MediaType type = new MediaType("text", "plain", Charset.forName("UTF-8"));
                ((StringHttpMessageConverter) httpMessageConverter).setSupportedMediaTypes(Arrays.asList( type, MediaType.ALL));
                break;
            }
        }
    }

    public WeChatOpenIdList getOpenIdList(WeChatAccessToken token){
        if(token != null) {
            String url = "cgi-bin/user/get?access_token={token}";
            return weChatClient.get(url, WeChatOpenIdList.class, token.getAccessToken());
        }

        return null;
    }

    private String convertToStr(Object target){
        return  target == null ? "" : target.toString();
    }
}
