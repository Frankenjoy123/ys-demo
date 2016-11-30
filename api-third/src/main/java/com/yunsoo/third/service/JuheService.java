package com.yunsoo.third.service;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import com.yunsoo.third.dao.entity.ThirdMobileVerificationCodeEntity;
import com.yunsoo.third.dao.entity.ThirdSmsTemplateEntity;
import com.yunsoo.third.dao.repository.MobileVerificationCodeRepository;
import com.yunsoo.third.dao.repository.SMSTemplateRepository;
import com.yunsoo.third.dto.juhe.*;
import com.yunsoo.common.util.HashUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by yan on 6/8/2016.
 */
@Service
public class JuheService {

    private Log log = LogFactory.getLog(this.getClass());

    private RestTemplate template;

    private final int expiredTime = 2;

    @Value("${yunsoo.juhe.openId}")
    private String openId;

    @Value("${yunsoo.juhe.keys.sms}")
    private String smsKey;

    @Value("${yunsoo.juhe.keys.ip}")
    private String ipKey;

    @Value("${yunsoo.juhe.keys.mobile_order}")
    private String mobileKey;

    @Value("${yunsoo.juhe.keys.mobile_data}")
    private String mobileDataKey;

    @Value("${yunsoo.juhe.keys.mobile_location}")
    private String mobileLocationKey;

    @Autowired
    private SMSTemplateRepository smsTemplateRepository;

    @Autowired
    private MobileVerificationCodeRepository mobileVerificationCodeRepository;

    public JuheService() {
        template = new RestTemplate();
    }

    public ThirdSmsTemplateEntity getSMSTemplate(String tempName){
        return smsTemplateRepository.findByNameAndSupplier(tempName, "juhe");
    }

    //话费充值
    public JuheMobileOrderResult.OrderResultObject mobileOrder(String mobile, int number, String orderId) {
        String url = "http://op.juhe.cn/ofpay/mobile/onlineorder?key={key}&phoneno={mobile}&cardnum={number}&orderid={orderId}&sign={sign}";
        String sign = HashUtils.md5HexString(openId + mobileKey + mobile + number + orderId);
        JuheMobileOrderResult result = template.getForObject(url, JuheMobileOrderResult.class, mobileKey, mobile, number, orderId, sign);
        if (result.getErrorCode() != 0)
            log.error("send mobile order error. reason: "+ result.getReason() +", mobile: " + mobile + ", number: " + number + ", order_id: " + orderId);
        return  result.getResult();

    }

    //流量充值
    public JuheMobileDataResult.DataResultObject mobileDataFlow(String mobile, int pid,  String orderId) {
        String url = "http://v.juhe.cn/flow/recharge?key={key}&phone={mobile}&pid={pid}&orderid={orderId}&sign={sign}";

        String sign = HashUtils.md5HexString(openId + mobileDataKey + mobile + pid + orderId);
        JuheMobileDataResult result = template.getForObject(url, JuheMobileDataResult.class, mobileDataKey, mobile, pid, orderId, sign);
        if (result.getErrorCode() != 0)
            log.error("send mobile order error. reason: "+ result.getReason() +", mobile: " + mobile + ", pid: " + pid + ", order_id: " + orderId);

        return result.getResult();
    }

    public JuheMobileLocationResult.LocationResultObject getMobileLocation(String mobile){
        if(mobile == null)
            return null;
        String url = "http://apis.juhe.cn/mobile/get?key={key}&phone={mobile}";
        JuheMobileLocationResult result = template.getForObject(url, JuheMobileLocationResult.class, mobileLocationKey, mobile);
        if (result.getErrorCode() != 0)
            log.error("send mobile order error. reason: "+ result.getReason() +", mobile: " + mobile);
        return  result.getResult();
    }

    public JuheIPResult getIP(String ip){
        String url = "http://apis.juhe.cn/ip/ip2addr?ip={ip}&key={key}";
        JuheIPResult result = template.getForObject(url, JuheIPResult.class, ip, ipKey);
        if (result.getErrorCode() != 0)
            log.error("get location of ip: " + ip + " error: " + result.getReason());

        return result;
    }

    //set the ver_code as the first variable
    public boolean sendVerificationCode(String mobile, String tempName, String... variables){
        ThirdMobileVerificationCodeEntity mobileVerificationCodeEntity = new ThirdMobileVerificationCodeEntity();
        mobileVerificationCodeEntity.setMobile(mobile);
        mobileVerificationCodeEntity.setVerificationCode(variables[0]);
        mobileVerificationCodeEntity.setUsedFlag(false);
        mobileVerificationCodeEntity.setCreatedDateTime(DateTime.now());
        mobileVerificationCodeRepository.save(mobileVerificationCodeEntity);

        ThirdSmsTemplateEntity templateEntity = getSMSTemplate(tempName);
        if (templateEntity == null)
            throw new NotFoundException("the related template not found");
        String[] keys = templateEntity.getVariable().split(",");
        if (keys.length != variables.length)
            throw new BadRequestException("error parameter variables");

        HashMap<String, String> map = new HashMap<>();
        int index = 0;
        for (String key : keys) {
            map.put(key, variables[index]);
            index++;
        }
        try {
            JuheSMSResult result = sendSMS(mobile, String.valueOf(templateEntity.getSupplierId()), map);
            if (result.getErrorCode() == 0) {
                mobileVerificationCodeEntity.setSentDateTime(DateTime.now());
                mobileVerificationCodeRepository.save(mobileVerificationCodeEntity);
                return true;
            }
            else{
                log.error("send sms message error. reason: " + result.getReason()  + "mobile: " + mobile + ", temp_name: " + tempName + ", variables: " + variables.toString());
                return false;
            }

        } catch (UnsupportedEncodingException e) {
            throw new UnprocessableEntityException("encode paramenter error for sms sending in juhe");
        }
    }


    public boolean verifySMSCode(String mobileNumber, String verificationCode) {
        ThirdMobileVerificationCodeEntity entity = mobileVerificationCodeRepository.findFirstByMobileAndSentDateTimeNotNullOrderBySentDateTimeDesc(mobileNumber);
        if (entity == null || entity.getUsedFlag() || !entity.getVerificationCode().equals(verificationCode)
                || DateTime.now().compareTo(entity.getSentDateTime().plusMinutes(expiredTime)) > 0)
            return false;

        entity.setUsedFlag(true);
        mobileVerificationCodeRepository.save(entity);
        return true;
    }


    private String encodeSMSVariables(HashMap<String, String> variables) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (String key : variables.keySet()) {
            sb.append("#");
            sb.append(key);
            sb.append("#=");
            sb.append(variables.get(key));
            sb.append("&");
        }
        String result = sb.toString();
        result = result.substring(0, result.length() - 1);  //remove last &
        return URLEncoder.encode(result, "UTF-8");

    }

    private JuheSMSResult sendSMS(String mobile, String tempId, HashMap<String, String> variables) throws UnsupportedEncodingException {
        String url = "http://v.juhe.cn/sms/send?mobile={mobile}&tpl_id={tempId}&tpl_value={variables}&key={key}";
        JuheSMSResult result = template.getForObject(url, JuheSMSResult.class, mobile, tempId, encodeSMSVariables(variables), smsKey);
        if (result.getErrorCode() != 0)
            log.error("send sms message error. mobile: " + mobile + ", tempId: " + tempId + ", variables: " + variables.toString());

        return result;
    }
}
