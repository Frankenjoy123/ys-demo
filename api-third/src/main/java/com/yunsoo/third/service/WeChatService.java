package com.yunsoo.third.service;

import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.ObjectUtils;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.third.dao.entity.ThirdWeChatAccessTokenEntity;
import com.yunsoo.third.dao.entity.ThirdWeChatConfigEntity;
import com.yunsoo.third.dao.repository.WeChatAccessTokenRepository;
import com.yunsoo.third.dao.repository.WeChatConfigRepository;
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
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Blob;
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

    private RestClient weChatClient;

    @Autowired
    private WeChatAccessTokenRepository weChatAccessTokenRepository;

    @Autowired
    private WeChatConfigRepository weChatConfigRepository;

    public WeChatService() {
        weChatClient = new RestClient("https://api.weixin.qq.com/");
    }

    public void save(ThirdWeChatAccessTokenEntity entity) {
        weChatAccessTokenRepository.save(entity);
    }

    public ThirdWeChatAccessTokenEntity saveWeChatAccessTicketFromWeChat(String orgId) {
        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);


        WeChatToken accessToken = weChatClient.get("cgi-bin/token?grant_type=client_credential&appid=" + configEntity.getAppId() + "&secret=" + configEntity.getAppSecret(), WeChatToken.class);
        if (accessToken.getErrorCode() == 0) {
            WeChatJSTicket jsApi_ticket = weChatClient.get("cgi-bin/ticket/getticket?access_token=" + accessToken.getAccessToken() + "&type=jsapi", WeChatJSTicket.class);
            if (jsApi_ticket.getErrorCode() == 0) {
                ThirdWeChatAccessTokenEntity token = weChatAccessTokenRepository.findTop1ByAppIdOrderByUpdatedDateTimeDesc(configEntity.getAppId());
                if (token == null) {
                    token = new ThirdWeChatAccessTokenEntity();
                    token.setUpdatedDateTime(DateTime.now());
                    token.setAppId(configEntity.getAppId());
                    token.setAccessToken(accessToken.getAccessToken());
                    token.setJsapiTicket(jsApi_ticket.getTicket());
                    token.setExpiredDatetime(DateTime.now().plusSeconds(jsApi_ticket.getExpiresIn().intValue() - 200));
                    weChatAccessTokenRepository.save(token);
                } else {
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

    public WeChatUser getUserInfoInWeb(String accessToken, String openId) {
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

    public WeChatUser getUserInfo(String orgId, String openId) {
        ThirdWeChatAccessTokenEntity entity = getAccessTokenFromDB(orgId);
        WeChatUser user = weChatClient.get("cgi-bin/user/info?access_token={token}&openid={openid}&lang=zh_CN",
                WeChatUser.class, entity.getAccessToken(), openId);

        if (user.getErrorCode() == 0)
            return user;
        else
            return null;

    }


    public WeChatWebAccessToken getWebAccessToken(String orgId, String code) {
        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);

        String url = "sns/oauth2/access_token?" +
                "appid=" + configEntity.getAppId() +
                "&secret=" + configEntity.getAppSecret() +
                "&code=" + code +
                "&grant_type=authorization_code";

        String webToken = weChatClient.get(url, String.class);
        try {
            webToken = new String(webToken.getBytes("ISO-8859-1"), "UTF-8");
            WeChatWebAccessToken token = ObjectUtils.toObject(webToken, WeChatWebAccessToken.class);
            if (token.getErrorCode() > 0) {
                logger.error("get wechat access token error: " + token.getErrorMsg());
                return null;
            }

            return token;
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

    public ThirdWeChatAccessTokenEntity getAccessTokenFromDB(String orgId) {
        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);

        ThirdWeChatAccessTokenEntity token = weChatAccessTokenRepository.findTop1ByAppIdOrderByUpdatedDateTimeDesc(configEntity.getAppId());
        if (token == null || token.getExpiredDatetime() == null || token.getExpiredDatetime().isBeforeNow()) {
            return saveWeChatAccessTicketFromWeChat(orgId);
        }

        return token;
    }

    public WeChatConfig getWechatConfig(String orgId, String url, String nonceStr) {
        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);

        WeChatConfig config = new WeChatConfig();
        config.setAppId(configEntity.getAppId());
        if (!StringUtils.hasText(nonceStr))
            config.setNoncestr(RandomUtils.generateString(30));
        else
            config.setNoncestr(nonceStr);
        config.setTimestamp(new DateTime().getMillis());
        ThirdWeChatAccessTokenEntity token = getAccessTokenFromDB(orgId);
        String jsapi_ticket = token.getJsapiTicket();
        String param = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + config.getNoncestr() + "&timestamp=" + config.getTimestamp() + "&url=" + url;
        config.setSignature(HashUtils.sha1HexString(param));
        return config;
    }

    public WeChatData getWeChatPayConfig(String orgId, String prePayId, long timeStamp, String nonceStr) throws UnsupportedEncodingException {
        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);

        WeChatData data = new WeChatData();
        data.setValue("appId", configEntity.getAppId());
        data.setValue("timeStamp", timeStamp);
        data.setValue("nonceStr", nonceStr);
        data.setValue("package", "prepay_id=" + prePayId);
        data.setValue("signType", "MD5");
        data.addSign(configEntity.getPrivateKey());
        return data;
    }

    public WeChatOrderResult saveWeChatUnifiedOrder(String orgId, String openId, String ip, String nonceString,
                                                    String productName, String orderId, BigDecimal price,
                                                    String notifyUrl, String orderType) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);

        RestTemplate weChatMchClient = initWeChatMchClient(configEntity);
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        WeChatData data = new WeChatData();
        data.setValue("appid", configEntity.getAppId());
        data.setValue("mch_id", configEntity.getMchId());
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
            data.addSign(configEntity.getPrivateKey());
            String result = weChatMchClient.postForObject(url, data.toXml(), String.class);
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            WeChatOrderResult orderResult = XmlUtils.convertXmlToObject(result, WeChatXmlBaseType.class, WeChatOrderResult.class);
            if ("SUCCESS".equals(orderResult.getReturnCode()) && "SUCCESS".equals(orderResult.getResultCode()))
                return orderResult;
            else {
                if (!"SUCCESS".equals(orderResult.getReturnCode()))
                    logger.error("get wechat unified pay error: " + orderResult.getReturnMsg());
                else
                    logger.error("get wechat unified pay error: code: " + orderResult.getErrCode() + ", message: " + orderResult.getErrCodeDes());
            }
        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error(e);
        }

        return null;
    }

    public WeChatPayResult queryPayResult(String orgId, String orderId, String nonceString) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);

        RestTemplate weChatMchClient = initWeChatMchClient(configEntity);

        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        WeChatData data = new WeChatData();
        data.setValue("appid", configEntity.getAppId());
        data.setValue("mch_id", configEntity.getMchId());
        data.setValue("nonce_str", StringUtils.hasText(nonceString) ? nonceString : RandomUtils.generateString(30));
        data.setValue("out_trade_no", orderId);
        data.setValue("sign_type", "MD5");
        try {
            data.addSign(configEntity.getPrivateKey());
            String result = weChatMchClient.postForObject(url, data.toXml(), String.class);
            result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            WeChatPayResult payResult = XmlUtils.convertXmlToObject(result, WeChatXmlBaseType.class, WeChatPayResult.class);
            if ("SUCCESS".equals(payResult.getReturnCode()) && "SUCCESS".equals(payResult.getResultCode()))
                return payResult;
            else {
                if (!"SUCCESS".equals(payResult.getReturnCode()))
                    logger.error("get wechat pay result error: " + payResult.getReturnMsg());
                else
                    logger.error("get wechat pay result error: code: " + payResult.getErrCode() + ", message: " + payResult.getErrCodeDes());
            }
        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error(e);
        }

        return null;
    }

    public WeChatNotifyResult handleNotifyResult(String orgId, String notifyInfo) {
        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);

        WeChatData data = new WeChatData();
        try {
            Map notifyResultMap = XmlUtils.convertXmlToObject(notifyInfo, HashMap.class);
            data.setValues(notifyResultMap);
            String existingSign = convertToStr(notifyResultMap.get("sign") == null);
            String sign = data.getSign(configEntity.getPrivateKey());
            if (existingSign.equals(sign)) {
                WeChatNotifyResult result = new WeChatNotifyResult();
                result.setOrderType(convertToStr(data.getValue("attach")));
                result.setOrderId(convertToStr(data.getValue("out_trade_no")));
                result.setWeChatOrderId(convertToStr(data.getValue("transaction_id")));
                return result;
            } else {
                throw new ForbiddenException("sign error");
            }


        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error(e);
        }


        return null;
    }

    public WeChatQRCodeResponse createQRCode(String orgId, String id, boolean permanent) {
        ThirdWeChatAccessTokenEntity tokenEntity = getAccessTokenFromDB(orgId);
        String url = "cgi-bin/qrcode/create?access_token={token}";
        WeChatQRCodeRequest request = new WeChatQRCodeRequest();
        if (permanent) {
            request.setActionName("QR_LIMIT_SCENE");
            request.setActionInfo("", id);
        } else {
            request.setActionName("QR_SCENE");
            request.setActionInfo(id, "");
        }

        request.setExpiresSeconds(7 * 24 * 60 * 60); //7days


        return weChatClient.post(url, request, WeChatQRCodeResponse.class, tokenEntity.getAccessToken());
    }

    public String sendRedPack(String orgId, String openId, String mchName, String orderId,
                               BigDecimal price, String ip, String remark, String wishing, String actionName) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {

        ThirdWeChatConfigEntity configEntity = weChatConfigRepository.getByOrgId(orgId);
        if (configEntity == null)
            throw new BadRequestException("the organization don't have any wechat config: " + orgId);

        RestTemplate weChatMchClient = initWeChatMchClient(configEntity);

        String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
        WeChatData data = new WeChatData();
        data.setValue("wxappid", configEntity.getAppId());
        data.setValue("mch_id", configEntity.getMchId());
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
            data.addSign(configEntity.getPrivateKey());
            String result = weChatMchClient.postForObject(url, data.toXml(), String.class);
            //  result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
            WeChatRedPackResult requestResult = XmlUtils.convertXmlToObject(result, WeChatXmlBaseType.class, WeChatRedPackResult.class);
            if ("SUCCESS".equals(requestResult.getReturnCode()) && "SUCCESS".equals(requestResult.getResultCode()))
                return requestResult.getWeChatOrderId();
            else {
                if (!"SUCCESS".equals(requestResult.getReturnCode()))
                    logger.error("send wechat redpack error: " + requestResult.getReturnMsg());
                else
                    logger.error("send wechat redpack error: code: " + requestResult.getErrCode() + ", message: " + requestResult.getErrCodeDes());
            }
        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error(e);
        }

        return null;
    }

    private RestTemplate initWeChatMchClient(ThirdWeChatConfigEntity configEntity) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream instream = new ByteArrayInputStream(configEntity.getSslKey())) {
            keyStore.load(instream, configEntity.getMchId().toCharArray());
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, configEntity.getMchId().toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpclient);
        RestTemplate weChatMchClient = new RestTemplate(factory);

        List<HttpMessageConverter<?>> listMessageConvert = weChatMchClient.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : listMessageConvert) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                MediaType type = new MediaType("text", "plain", Charset.forName("UTF-8"));
                ((StringHttpMessageConverter) httpMessageConverter).setSupportedMediaTypes(Arrays.asList(type, MediaType.ALL));
                break;
            }
        }

        return weChatMchClient;
    }

    public WeChatOpenIdList getOpenIdList(String appId, String appSecret) {
        WeChatToken accessToken = weChatClient.get("cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret, WeChatToken.class);

        if (accessToken.getErrorCode() == 0) {
            String url = "cgi-bin/user/get?access_token={token}";
            return weChatClient.get(url, WeChatOpenIdList.class, accessToken.getAccessToken());
        }

        return null;
    }

    public WeChatServerConfig saveWeChatConfig(WeChatServerConfig config) {
        ThirdWeChatConfigEntity entity = new ThirdWeChatConfigEntity();
        entity.setAppId(config.getAppId());
        entity.setAppSecret(config.getAppSecret());
        entity.setMchId(config.getMchId());
        entity.setPrivateKey(config.getPrivateKey());
        entity.setSslKey(config.getSslKey());
        entity.setOrgId(config.getOrgId());
        weChatConfigRepository.save(entity);
        return config;
    }

    public WeChatServerConfig getWeChatServerConfigByOrgId(String orgId) {
        ThirdWeChatConfigEntity entity = weChatConfigRepository.getByOrgId(orgId);
        WeChatServerConfig config = new WeChatServerConfig();
        if (entity != null) {
            config.setAppId(entity.getAppId());
            config.setAppSecret(entity.getAppSecret());
            config.setPrivateKey(entity.getPrivateKey());
            config.setMchId(entity.getMchId());
            config.setOrgId(orgId);
        }
        config.setOrgId(orgId);
        return config;
    }


    private String convertToStr(Object target) {
        return target == null ? "" : target.toString();
    }
}
