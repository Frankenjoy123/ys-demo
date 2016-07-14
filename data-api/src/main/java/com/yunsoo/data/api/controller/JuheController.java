package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.juhe.*;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import com.yunsoo.data.service.entity.*;
import com.yunsoo.data.service.repository.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by yan on 6/8/2016.
 */
@RestController
@RequestMapping("/juhe")
public class JuheController {

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
    private SMSTemplateRepository repository;

    @Autowired
    private MktDrawPrizeRepository prizeRepository;

    @Autowired
    private MktPrizeCostRepository costRepository;

    @Autowired
    private MktDataFlowRepository dataFlowRepository;

    @Autowired
    private MobileVerificationCodeRepository mobileVerificationCodeRepository;

    @Autowired
    private MktDrawRuleRepository ruleRepository;

    @Autowired
    private MktConsumerRightRepository consumerRightRepository;

    public JuheController() {
        template = new RestTemplate();
    }


    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    public boolean sendSMS(@RequestParam("mobile") String mobile, @RequestParam("temp_name") String tempName, @RequestParam(value = "variables", required = false) String... variables) {

        SmsTemplateEntity templateEntity = repository.findByName(tempName);
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
            SMSResultObject result = sendSMSInJuhe(mobile, templateEntity.getId(), map);
            if (result.getErrorCode() == 0) {
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

    @RequestMapping(value = "/verificationCode/{mobile}", method = RequestMethod.GET)
    public boolean sendVerificationCode(@PathVariable("mobile") String mobile, @RequestParam("temp_name") String tempName){
        String ver_code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        MobileVerificationCodeEntity mobileVerificationCodeEntity = new MobileVerificationCodeEntity();
        mobileVerificationCodeEntity.setMobile(mobile);
        mobileVerificationCodeEntity.setVerificationCode(ver_code);
        mobileVerificationCodeEntity.setUsedFlag(false);
        mobileVerificationCodeEntity.setCreatedDateTime(DateTime.now());
        mobileVerificationCodeRepository.save(mobileVerificationCodeEntity);

        boolean result = sendSMS(mobile, tempName, ver_code );
        if(result) {
            mobileVerificationCodeEntity.setSentDateTime(DateTime.now());
            mobileVerificationCodeRepository.save(mobileVerificationCodeEntity);
        }

        return result;
    }


    @RequestMapping(value = "/verifycode", method = RequestMethod.POST)
    public boolean VerifyCode(@RequestParam("mobile") String mobile, @RequestParam("verification_code") String verificationCode) {
        if ((mobile == null) || (verificationCode == null)) {
            throw new BadRequestException("mobile phone or verification code can not be null");
        }
        boolean isVerifiedPass = false;
        isVerifiedPass = verifySMSCode(mobile, verificationCode);

        return isVerifiedPass;
    }

    public MobileLocationResultObject getMobileLocation(@RequestParam("mobile") String mobile){
        return getMobileLocationInJuhe(mobile);
    }

    @RequestMapping(value = "/ip", method = RequestMethod.GET)
    public IPResultObject getLocationByIp(@RequestParam("ip") String ip){
        IPResultObject result = getIPInJuhe(ip);
        if(result.getErrorCode() == 0)
            return  result;
        else{
            log.error("get ip error. reason: " + result.getReason()  + ". ip: " + ip);
            return null;
        }
    }


    @RequestMapping(value = "/mobile/order", method = RequestMethod.GET)
    public boolean mobileOrder (@RequestParam("draw_prize_id") String id){
        MktDrawPrizeEntity prize = prizeRepository.findOne(id);
        if(prize == null)
            throw new NotFoundException("the related prize not found");

        MobileOrderResultObject resultObject = mobileOrderInJuhe(prize.getMobile(), prize.getAmount().intValue(), id);
        if(resultObject.getErrorCode() == 0){
            MktPrizeCostEntity entity = new MktPrizeCostEntity();
            entity.setCost(new BigDecimal(resultObject.getResult().getOrderPrice()));
            entity.setDrawRecordId(id);
            entity.setMobile(prize.getMobile());
            entity.setName(resultObject.getResult().getCardName());
            entity.setOrderId(resultObject.getResult().getJuheOrderId());
            entity.setType(LookupCodes.MktPrizeCostType.MOBILE_FEE);
            costRepository.save(entity);
            return  true;
        }
        else{
             return false;
        }
    }

    @RequestMapping(value = "/mobile/data", method = RequestMethod.GET)
    public boolean mobileDataFlow (@RequestParam("draw_prize_id") String id){
        MktDrawPrizeEntity prize = prizeRepository.findOne(id);
        if(prize == null)
            throw new NotFoundException("the related prize not found");

        MktDrawRuleEntity ruleEntity = ruleRepository.findOne(prize.getDrawRuleId());
        if (ruleEntity == null)
            throw new NotFoundException("prize rule not found");

        String consumerRightId = ruleEntity.getConsumerRightId();
        if (consumerRightId == null)
            throw new NotFoundException("consumer right not found");
        MktConsumerRightEntity consumerRightEntity = consumerRightRepository.findOne(consumerRightId);
        Integer pid = getPidByMobileAndConsumerRight(prize.getMobile(), consumerRightEntity);
        if (pid != null) {
            MktDataFlowEntity flowEntity = dataFlowRepository.findOne(pid.intValue());
            if(flowEntity == null)
                throw new NotFoundException("could not found related mobile data flow for mobile: " + prize.getMobile() + ", data flow: " + prize.getAmount().toString());

            MobileDataResultObject resultObject = mobileDataFlowInJuhe(prize.getMobile(), flowEntity.getId(), id);
            if (resultObject.getErrorCode() == 0) {
                MktPrizeCostEntity entity = new MktPrizeCostEntity();
                entity.setCost(new BigDecimal(resultObject.getResult().getOrderPrice()));
                entity.setDrawRecordId(id);
                entity.setMobile(prize.getMobile());
                entity.setName(resultObject.getResult().getCardName());
                entity.setOrderId(resultObject.getResult().getJuheOrderId());
                entity.setType(LookupCodes.MktPrizeCostType.MOBILE_FEE);
                costRepository.save(entity);
                return true;
            } else {
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean verifySMSCode(String mobileNumber, String verificationCode) {
        MobileVerificationCodeEntity entity = mobileVerificationCodeRepository.findFirstByMobileAndSentDateTimeNotNullOrderBySentDateTimeDesc(mobileNumber);
        if (entity == null || entity.getUsedFlag() || !entity.getVerificationCode().equals(verificationCode)
                || DateTime.now().compareTo(entity.getSentDateTime().plusMinutes(expiredTime)) > 0)
            return false;

        entity.setUsedFlag(true);
        mobileVerificationCodeRepository.save(entity);
        return true;
    }

    public Integer getPidByMobileAndConsumerRight(String mobile, MktConsumerRightEntity entity) {
        if ((mobile == null) || (entity == null))
            throw new BadRequestException("mobile or consumer right not found");

        MobileLocationResultObject location = getMobileLocation(mobile);
        if (location != null && location.getErrorCode() == 0) {
            String mobileType = location.getResult().getCompany();
            if ("中国移动".equals(mobileType))
                return entity.getCmccFlowId();
            if ("中国联通".equals(mobileType))
                return entity.getCuccFlowId();
            if ("中国电信".equals(mobileType))
                return entity.getCtccFlowId();
        } else {
            if (location == null)
                log.error("the prize don't have mobile info");
            else if (location.getErrorCode() != 0)
                log.error("could not get the location of mobile, reason: " + location.getReason() + ", mobile: " + mobile);
        }
        return null;
    }


    private MobileOrderResultObject mobileOrderInJuhe(String mobile, int number, String orderId) {
        String url = "http://op.juhe.cn/ofpay/mobile/onlineorder?key={key}&phoneno={mobile}&cardnum={number}&orderid={orderId}&sign={sign}";
        String sign = HashUtils.md5HexString(openId + mobileKey + mobile + number + orderId);
        MobileOrderResultObject result = template.getForEntity(url, MobileOrderResultObject.class, mobileKey, mobile, number, orderId, sign).getBody();
        if (result.getErrorCode() != 0)
            log.error("send mobile order error. reason: "+ result.getReason() +", mobile: " + mobile + ", number: " + number + ", order_id: " + orderId);
        return  result;

    }

    private MobileDataResultObject mobileDataFlowInJuhe(String mobile, int pid,  String orderId) {
        String url = "http://v.juhe.cn/flow/recharge?key={key}&phone={mobile}&pid={pid}&orderid={orderId}&sign={sign}";

        String sign = HashUtils.md5HexString(openId + mobileDataKey + mobile + pid + orderId);
        MobileDataResultObject result = template.getForEntity(url, MobileDataResultObject.class, mobileDataKey, mobile, pid, orderId, sign).getBody();
        if (result.getErrorCode() != 0)
            log.error("send mobile order error. reason: "+ result.getReason() +", mobile: " + mobile + ", pid: " + pid + ", order_id: " + orderId);

        return result;

      /* test
        MobileDataResultObject object = new MobileDataResultObject();
        object.setErrorCode(0);
        object.setReason("Success");
        MobileDataResultObject.DataResultObject resultObject = object.new DataResultObject();
        resultObject.setOrderId(orderId);
        resultObject.setCardName("流量测试");
        resultObject.setOrderPrice("3.10");
        resultObject.setPhone(mobile);
        resultObject.setJuheOrderId("146580480974645861");
        object.setResult(resultObject);
        return object;*/
    }

    private SMSResultObject sendSMSInJuhe(String mobile, int tempId, HashMap<String, String> variables) throws UnsupportedEncodingException {
        String url = "http://v.juhe.cn/sms/send?mobile={mobile}&tpl_id={tempId}&tpl_value={variables}&key={key}";
        SMSResultObject result = template.getForEntity(url, SMSResultObject.class, mobile, tempId, encodeSMSVariables(variables), smsKey).getBody();
        if (result.getErrorCode() != 0)
            log.error("send sms message error. mobile: " + mobile + ", tempId: " + tempId + ", variables: " + variables.toString());

        return result;
    }

    private MobileLocationResultObject getMobileLocationInJuhe(String mobile){
        if(mobile == null)
            return null;
        String url = "http://apis.juhe.cn/mobile/get?key={key}&phone={mobile}";
        MobileLocationResultObject result = template.getForEntity(url, MobileLocationResultObject.class, mobileLocationKey, mobile).getBody();
        if (result.getErrorCode() != 0)
            log.error("send mobile order error. reason: "+ result.getReason() +", mobile: " + mobile);
        return  result;
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

    private IPResultObject getIPInJuhe(String ip){
        String url = "http://apis.juhe.cn/ip/ip2addr?ip={ip}&key={key}";
        IPResultObject result = template.getForEntity(url, IPResultObject.class, ip, ipKey).getBody();
        if (result.getErrorCode() != 0)
            log.error("get location of ip: " + ip + " error: " + result.getReason());

        return result;
    }

}
