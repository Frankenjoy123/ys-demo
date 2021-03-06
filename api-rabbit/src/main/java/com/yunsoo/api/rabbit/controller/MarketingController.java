package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.MarketingDomain;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.domain.UserScanDomain;
import com.yunsoo.api.rabbit.dto.*;
import com.yunsoo.api.rabbit.key.dto.Product;
import com.yunsoo.api.rabbit.key.service.ProductService;
import com.yunsoo.api.rabbit.third.dto.WeChatRedPackRequest;
import com.yunsoo.api.rabbit.third.dto.WeChatServerConfig;
import com.yunsoo.api.rabbit.third.dto.WeChatUser;
import com.yunsoo.api.rabbit.third.service.JuheService;
import com.yunsoo.api.rabbit.third.service.WeChatService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */

@RestController
@RequestMapping(value = "/marketing")
public class MarketingController {

    private org.apache.commons.logging.Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private MarketingDomain marketingDomain;

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private JuheService juheService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private UserScanDomain userScanDomain;

    @Autowired
    private UserDomain userDomain;


    //获取Key所对应的抽奖记录
    @RequestMapping(value = "draw/{key}", method = RequestMethod.GET)
    public MktDrawRecord getMktDrawRecordByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktDrawRecordObject mktDrawRecordObject = marketingDomain.getMktDrawRecordByProductKey(key);
        if (mktDrawRecordObject != null) {
            return new MktDrawRecord(mktDrawRecordObject);
        } else {
            return null;
        }
    }

    //获取Key所对应的抽奖信息by product key and ysid
    @RequestMapping(value = "draw/{key}/user/{ysid}", method = RequestMethod.GET)
    public MktDrawInfo getMktDrawRecordByProductKeyAndUser(@PathVariable(value = "key") String key, @PathVariable(value = "ysid") String ysId,
                                                           @RequestParam(value = "oauth_openid") String oauthOpenId,
                                                           @RequestParam(value = "marketing_id") String marketingId) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        if (oauthOpenId == null) {
            throw new BadRequestException("oauth openid can not be null");
        }
        MktDrawInfo mktDrawInfo = new MktDrawInfo();
        MktDrawRecordObject mktDrawRecordObject = marketingDomain.getMktDrawRecordByProductKeyAndUserAndOauthOpenId(key, ysId, oauthOpenId, marketingId);
        if (mktDrawRecordObject != null) {
            mktDrawInfo.setMktDrawRecord(new MktDrawRecord(mktDrawRecordObject));
            MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKeyAndUserAndOauthOpenId(key, ysId, oauthOpenId);
            mktDrawInfo.setMktDrawPrize(new MktDrawPrize(mktDrawPrizeObject));
            return mktDrawInfo;
        } else {
            return null;
        }

    }

    //create marketing plan for micro shop sending wechat red packets
    @RequestMapping(value = "marketing/draw04", method = RequestMethod.POST)
    public Marketing createMarketing(@RequestParam(value = "openid") String openid,
                                     @RequestBody Marketing marketing) {
        if (!StringUtils.hasText(openid)) {
            throw new BadRequestException("seller openid should not be empty.");
        }
        MktSellerObject mktSellerObject = marketingDomain.getMktSellerByOpenid(openid);
        if (mktSellerObject == null) {
            throw new BadRequestException("seller openid invalid.");
        }
        MarketingObject marketingObject = marketing.toMarketingObject();
        marketingObject.setCreatedDateTime(DateTime.now());
        marketingObject.setTypeCode(LookupCodes.MktType.DRAW04);
        if (marketing.getBudget() != null) {
            marketingObject.setBalance(marketing.getBudget());
        }
        marketingObject.setOrgId(mktSellerObject.getOrgId());
        marketingObject.setQuantity((marketing.getBudget().intValue()));
        MarketingObject mktObject = marketingDomain.createMarketing(marketingObject);

        return new Marketing(mktObject);
    }

    @RequestMapping(value = "marketing/draw04/{id}", method = RequestMethod.PUT)
    public void updateWechatMarketing(@PathVariable(value = "id") String marketingId) {
        if (marketingId == null) {
            throw new BadRequestException("wechat marketing id can not be null");
        }
        marketingDomain.updateWechatMarketing(marketingId);
    }

    //create marketing draw rules for micro shop sending wechat red packets
    @RequestMapping(value = "drawRule/draw04/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRule createMktDrawRuleList(@RequestBody List<MktDrawRule> mktDrawRuleList) {
        if (mktDrawRuleList == null || mktDrawRuleList.size() == 0) {
            throw new BadRequestException("marketing draw rule list can not be null");
        }
        List<MktDrawRuleObject> mktDrawRuleObjectList = new ArrayList<>();
        for (MktDrawRule mktDrawRule : mktDrawRuleList) {
            String marketingId = mktDrawRule.getMarketingId();
            MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
            if (marketingObject == null) {
                throw new NotFoundException("marketing can not be found by the id");
            }
            MktDrawRuleObject mktDrawRuleObject = mktDrawRule.toMktDrawRuleObject();
            mktDrawRuleObject.setTotalQuantity(marketingObject.getQuantity());
            mktDrawRuleObject.setAvailableQuantity(marketingObject.getQuantity());
            mktDrawRuleObject.setCreatedDateTime(DateTime.now());
            mktDrawRuleObjectList.add(mktDrawRuleObject);
        }

        MktDrawRuleObject newMktDrawRuleObject = marketingDomain.createMktDrawRuleList(mktDrawRuleObjectList);
        return new MktDrawRule(newMktDrawRuleObject);
    }

    //获取Key所对应的兑奖情况
    @RequestMapping(value = "drawPrize/{key}", method = RequestMethod.GET)
    public MktDrawPrize getMktDrawPrizeByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKey(key);
        if (mktDrawPrizeObject != null) {
            MktDrawPrize prize = new MktDrawPrize(mktDrawPrizeObject);
            MktDrawRuleObject rule = marketingDomain.getDrawRuleById(prize.getDrawRuleId());
            if (StringUtils.hasText(rule.getConsumerRightId())) {
                MktConsumerRightObject right = marketingDomain.getConsumerRightById(rule.getConsumerRightId());
                prize.setMktConsumerRight(new MktConsumerRight(right));
            }
            return prize;
        } else {
            return null;
        }
    }

    //获取Key所对应的兑奖情况 by product key and ysid
    @RequestMapping(value = "drawPrize/{key}/user/{ysid}", method = RequestMethod.GET)
    public MktDrawPrize getMktDrawPrizeByProductKeyAndUser(@PathVariable(value = "key") String key, @PathVariable(value = "ysid") String ysId) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKeyAndUser(key, ysId);
        if (mktDrawPrizeObject != null) {
            MktDrawPrize prize = new MktDrawPrize(mktDrawPrizeObject);
            MktDrawRuleObject rule = marketingDomain.getDrawRuleById(prize.getDrawRuleId());
            if (StringUtils.hasText(rule.getConsumerRightId())) {
                MktConsumerRightObject right = marketingDomain.getConsumerRightById(rule.getConsumerRightId());
                prize.setMktConsumerRight(new MktConsumerRight(right));
            }
            return prize;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "drawPrize/{id}/contact", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktPrizeContact createMktPrizeContact(@PathVariable(value = "id") String prizeId, @RequestBody MktPrizeContact mktPrizeContact) {
        if (mktPrizeContact == null) {
            throw new BadRequestException("marketing prize contact can not be null");
        }
        mktPrizeContact.setMktPrizeId(prizeId);

        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByPrizeId(prizeId);
        if (mktDrawPrizeObject == null) {
            throw new NotFoundException("marketing draw prize can not be found");
        }
        MktPrizeContactObject prizeContactObject = marketingDomain.getMktPrizeContactByPrizeId(prizeId);
        if (prizeContactObject != null) {
            throw new BadRequestException("marketing prize contact has been already input.");
        }
        MktPrizeContactObject mktPrizeContactObject = mktPrizeContact.toMktPrizeContactObject();
        MktPrizeContactObject newObject = marketingDomain.createMktPrizeContact(mktPrizeContactObject);

        mktDrawPrizeObject.setPrizeContactId(newObject.getId());
        mktDrawPrizeObject.setPrizeAccountName(mktPrizeContact.getName());
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
        mktDrawPrizeObject.setPrizeAccount(mktPrizeContact.getPhone());
        mktDrawPrizeObject.setMobile(mktPrizeContact.getPhone());
        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);


        return new MktPrizeContact(newObject);
    }


    // query draw01 prize contact by product key
    @RequestMapping(value = "drawPrize/contact/{key}", method = RequestMethod.GET)
    public MktPrizeContact getPrizeContactByProductKey(@PathVariable(value = "key") String productKey) {
        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKey(productKey);
        if (mktDrawPrizeObject == null) {
            throw new NotFoundException("marketing draw prize can not be found");
        }
        String prizeContactId = mktDrawPrizeObject.getPrizeContactId();
        if (!StringUtils.hasText(prizeContactId)) {
            return null;
        }
        MktPrizeContactObject mktPrizeContactObject = marketingDomain.getMktPrizeContactById(prizeContactId);

        if ((!mktDrawPrizeObject.getStatusCode().equals(LookupCodes.MktDrawPrizeStatus.CREATED)) && (mktPrizeContactObject != null)) {
            return new MktPrizeContact(mktPrizeContactObject);
        } else {
            return null;
        }
    }

    // query draw01 prize contact by product key and ysId
    @RequestMapping(value = "drawPrize/contact/{key}/user/{ysid}", method = RequestMethod.GET)
    public MktPrizeContact getPrizeContactByProductKeyAndUser(@PathVariable(value = "key") String productKey, @PathVariable(value = "ysid") String ysId) {
        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKeyAndUser(productKey, ysId);
        if (mktDrawPrizeObject == null) {
            throw new NotFoundException("marketing draw prize can not be found");
        }
        String prizeContactId = mktDrawPrizeObject.getPrizeContactId();
        if (!StringUtils.hasText(prizeContactId)) {
            return null;
        }
        MktPrizeContactObject mktPrizeContactObject = marketingDomain.getMktPrizeContactById(prizeContactId);

        if ((!mktDrawPrizeObject.getStatusCode().equals(LookupCodes.MktDrawPrizeStatus.CREATED)) && (mktPrizeContactObject != null)) {
            return new MktPrizeContact(mktPrizeContactObject);
        } else {
            return null;
        }
    }


    // create draw01 prize contact and update draw prize record
    @RequestMapping(value = "drawPrize/contact/{key}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktPrizeContact createPrizeContact(@PathVariable(value = "key") String productKey, @RequestBody MktPrizeContact mktPrizeContact) {
        if (mktPrizeContact == null) {
            throw new BadRequestException("marketing prize contact can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeByProductKey(productKey);
        if (mktDrawPrizeObject == null) {
            throw new NotFoundException("marketing draw prize can not be found");
        }
        if (!mktDrawPrizeObject.getStatusCode().equals(LookupCodes.MktDrawPrizeStatus.CREATED)) {
            throw new BadRequestException("Only created draw prize can be allowed to update contact.");
        }
        String prizeId = mktDrawPrizeObject.getDrawRecordId();

        mktPrizeContact.setMktPrizeId(prizeId);

        MktPrizeContactObject prizeContactObject = marketingDomain.getMktPrizeContactByPrizeId(prizeId);
        if (prizeContactObject != null) {
            throw new BadRequestException("marketing prize contact has been already input.");
        }
        MktPrizeContactObject mktPrizeContactObject = mktPrizeContact.toMktPrizeContactObject();
        MktPrizeContactObject newObject = marketingDomain.createMktPrizeContact(mktPrizeContactObject);

        mktDrawPrizeObject.setPrizeContactId(newObject.getId());
        mktDrawPrizeObject.setPrizeAccountName(mktPrizeContact.getName());
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
        mktDrawPrizeObject.setPrizeAccount(mktPrizeContact.getPhone());
        mktDrawPrizeObject.setMobile(mktPrizeContact.getPhone());
        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);

        return new MktPrizeContact(newObject);
    }


    @RequestMapping(value = "drawPrize", method = RequestMethod.PUT)
    public void updateMktDrawPrize(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        boolean result = true;

        MktDrawPrizeObject currentPrize = marketingDomain.getMktDrawPrizeByProductKey(mktDrawPrize.getProductKey());
        if (currentPrize != null && (LookupCodes.MktDrawPrizeStatus.CREATED.equals(currentPrize.getStatusCode()))) {
            MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
            mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
            mktDrawPrizeObject.setDrawRecordId(currentPrize.getDrawRecordId());

            if (currentPrize.getPrizeTypeCode() == null)
                currentPrize.setPrizeTypeCode(LookupCodes.MktPrizeType.WEBCHAT);

            if (StringUtils.hasText(mktDrawPrizeObject.getMobile()))
                currentPrize.setMobile(mktDrawPrizeObject.getMobile());

            switch (currentPrize.getPrizeTypeCode()) {
                case LookupCodes.MktPrizeType.MOBILE_FEE:
                    marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
                    boolean isMobileFeeSuccess = marketingDomain.createMobileOrder(currentPrize);
                    if (!isMobileFeeSuccess) {
                        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.FAILED);
                        result = false;
                    } else {
                        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                        mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                    }
                    break;
                case LookupCodes.MktPrizeType.MOBILE_DATA:
                    marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
                    boolean isMobileDataSuccess = marketingDomain.createMobileDataFlow(currentPrize);
                    if (!isMobileDataSuccess) {
                        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.FAILED);
                        result = false;
                    } else {
                        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                        mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                    }
                    break;
                case LookupCodes.MktPrizeType.COUPON:
                    mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                    mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                    break;
                case LookupCodes.MktPrizeType.WEBCHAT:
                    MarketingObject marketingObject = marketingDomain.getMarketingById(currentPrize.getMarketingId());
                    //send red pack
                    WeChatServerConfig config = weChatService.getOrgIdHasWeChatSettings(marketingObject.getOrgId());
                    if (config == null)
                        throw new NotFoundException("wechat settings not found with org: " + marketingObject.getOrgId());

                    if(marketingDomain.sendWeChatRedPack(marketingObject,currentPrize.getPrizeAccount(),currentPrize.getAmount(),
                            currentPrize.getDrawRecordId(), config.getOrgId())) {
                        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                        mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                    }
                    else
                        throw new RestErrorResultException(new ErrorResult(5004, "send wechat red pack failed"));
                    break;
                default:
                    break;
            }

            marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);

            if (mktDrawPrize.getYsid() != null) {
                MktDrawRecordObject record = marketingDomain.getMktDrawRecordByProductKey(mktDrawPrize.getProductKey());
                record.setYsid(mktDrawPrize.getYsid());
                marketingDomain.updateMktDrawRecord(record);
            }

        } else
            throw new RestErrorResultException(new ErrorResult(5002, "prize had been sent"));


        if (!result)
            throw new RestErrorResultException(new ErrorResult(5003, "send prize failed"));
    }

    @RequestMapping(value = "drawPrize/invalid", method = RequestMethod.PUT)
    public void updateFailedMktDrawPrize(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        MktDrawPrizeObject currentPrize = marketingDomain.getMktDrawPrizeByProductKey(mktDrawPrize.getProductKey());
        if (currentPrize != null && (LookupCodes.MktDrawPrizeStatus.CREATED.equals(currentPrize.getStatusCode()) ||
                LookupCodes.MktDrawPrizeStatus.SUBMIT.equals(currentPrize.getStatusCode()))) {
            MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
            mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.INVALID);
            mktDrawPrizeObject.setDrawRecordId(currentPrize.getDrawRecordId());
            marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
        }

    }

    //业务API需要做校验： 比如先判断Key是否中奖 for 海涛
    @RequestMapping(value = "consumer/redeemcode/{key}", method = RequestMethod.GET)
    public MktConsumerRightRedeemCode getConsumerRedeemCodeByIdAndPrizeId(@PathVariable String key) {
        MktConsumerRightRedeemCodeObject mktConsumerRightRedeemCodeObject = marketingDomain.getMktConsumerRightRedeemCodeByProductKey(key);

        if (mktConsumerRightRedeemCodeObject != null) {
            return new MktConsumerRightRedeemCode(mktConsumerRightRedeemCodeObject);
        } else {
            return null;
        }
    }

    //业务API需要做校验： 比如先判断Key是否中奖  for 海涛
    @RequestMapping(value = "consumer/redeemcode/generate/{key}", method = RequestMethod.GET)
    public MktConsumerRightRedeemCode getRandomConsumerRedeemCodeByIdAndPrizeId(@PathVariable String key,
                                                                                @RequestParam(value = "draw_rule_id") String drawRuleId) {
        MktConsumerRightRedeemCodeObject mktConsumerRightRedeemCodeObject = marketingDomain.getRandomMktConsumerRightRedeemCodeByProductKey(key, drawRuleId);

        if (mktConsumerRightRedeemCodeObject != null) {
            return new MktConsumerRightRedeemCode(mktConsumerRightRedeemCodeObject);
        } else {
            return null;
        }
    }


    @RequestMapping(value = "consumer/key/{key}", method = RequestMethod.GET)
    public MktConsumerRight getMktConsumerRightByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktConsumerRightObject mktConsumerRightObject = marketingDomain.getConsumerRightByProductKey(key);
        if (mktConsumerRightObject != null) {
            return new MktConsumerRight(mktConsumerRightObject);
        } else {
            return null;
        }
    }

    // query consumer right by product key and ysid
    @RequestMapping(value = "consumer/key/{key}/user/{ysid}", method = RequestMethod.GET)
    public MktConsumerRight getMktConsumerRightByProductKeyAndUser(@PathVariable String key, @PathVariable String ysId) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }
        MktConsumerRightObject mktConsumerRightObject = marketingDomain.getConsumerRightByProductKeyAndUser(key, ysId);
        if (mktConsumerRightObject != null) {
            return new MktConsumerRight(mktConsumerRightObject);
        } else {
            return null;
        }
    }


    //判断营销方案是否可用，暨用户能否参加营销活动
    @RequestMapping(value = "validate/key/{key}", method = RequestMethod.GET)
    public String getMarketingValidateByProductKey(@PathVariable String key) {
        if (key == null) {
            throw new BadRequestException("product key can not be null");
        }

        //search product by key
        Product product = productService.getProductByKey(key);
        if (product == null) {
            throw new NotFoundException("product not found");
        }
        ProductKeyBatchObject productKeyBatchObject = productDomain.getProductKeyBatch(product.getKeyBatchId());

        if (productKeyBatchObject != null) {
            //marketing info
            String marketingId = productKeyBatchObject.getMarketingId();
            if (marketingId != null) {
                MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
                long now = DateTime.now().getMillis();
                if (marketingObject != null
                        && (marketingObject.getStartDateTime() == null || marketingObject.getStartDateTime().getMillis() <= now)
                        && (marketingObject.getEndDateTime() == null || marketingObject.getEndDateTime().getMillis() >= now)
                        && LookupCodes.MktStatus.PAID.equals(marketingObject.getStatusCode())) {
                    return LookupCodes.MktVerifyStatus.VALID;
                } else {
                    return LookupCodes.MktVerifyStatus.INVALID;
                }
            } else {
                return LookupCodes.MktVerifyStatus.INVALID;
            }
        } else {
            throw new BadRequestException("product key batch not found");
        }


    }

    @RequestMapping(value = "drawPrize/{key}/sms", method = RequestMethod.POST)
    public boolean sendPrizeSMS(@PathVariable(value = "key") String productKey,
                                @RequestParam(value = "mobile") String mobile) {
        MktDrawPrizeObject prize = marketingDomain.getMktDrawPrizeByProductKey(productKey);
        if (prize == null)
            throw new NotFoundException("prize for product key not found");
        return juheService.sendVerificationCode(mobile, LookupCodes.SMSTemplate.SENDPRIZE);
    }

    @RequestMapping(value = "drawPrize/smsverfiy", method = RequestMethod.PUT)
    public boolean validatePrizeVerificationCode(@RequestParam(value = "mobile") String mobile,
                                                 @RequestParam(value = "verification_code") String verificationCode) {

        return juheService.validateVerificationCode(mobile, verificationCode);
    }

    @RequestMapping(value = "drawPrize/{id}/random", method = RequestMethod.POST)
    public MktDrawRule getRandomPrizeAmount(@PathVariable(value = "id") String marketingId,
                                            @RequestBody @Valid MktDraw mktDraw) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        if (mktDraw == null)
            throw new BadRequestException("prize content can not be null");

        String key = mktDraw.getProductKey();

//        ProductObject product = productDomain.getProduct(key);
//        if (product == null) {
//            throw new NotFoundException("product can not be found by the key");
//        }
        // one ysid has opportunity to draw for every product key
//        MktDrawRecordObject currentRecord = marketingDomain.getMktDrawRecordByProductKey(key);
//        if(currentRecord != null)
//            throw new ConflictException("key already prized");

        MktDrawPrizeObject prize = new MktDrawPrizeObject();
        prize.setPrizeAccountName(mktDraw.getPrizeAccountName());
        prize.setMarketingId(mktDraw.getMarketingId());
        prize.setProductKey(mktDraw.getProductKey());
        prize.setScanRecordId(mktDraw.getScanRecordId());
        //save prize
        MktDrawRecordObject record = new MktDrawRecordObject();
        record.setOauthOpenid(mktDraw.getOauthOpenId());
        record.setScanRecordId(mktDraw.getScanRecordId());
        record.setProductBaseId(mktDraw.getProductBaseId());
        record.setProductKey(mktDraw.getProductKey());
        record.setMarketingId(mktDraw.getMarketingId());
        record.setYsid(mktDraw.getYsId());
        record.setUserId(mktDraw.getUserId());

        // apply to no scan record id
        if (!StringUtils.hasText(mktDraw.getScanRecordId())) {
            String tempScanRecordId = ObjectIdGenerator.getNew();
            record.setScanRecordId(tempScanRecordId);
            prize.setScanRecordId(tempScanRecordId);
        }

        MktDrawRuleObject mktDrawRuleObject = marketingDomain.getMktRandomPrize(marketingId, mktDraw.getScanRecordId(), key);

        if (mktDrawRuleObject != null) {
            record.setIsPrized(true);
            if (mktDrawRuleObject.getPrizeTypeCode() != null)
                prize.setPrizeTypeCode(mktDrawRuleObject.getPrizeTypeCode());
            else
                prize.setPrizeTypeCode(LookupCodes.MktPrizeType.WEBCHAT);

            MktDrawRule mktDrawRule = new MktDrawRule(mktDrawRuleObject);
            String consumerRightId = mktDrawRuleObject.getConsumerRightId();
            if (consumerRightId != null) {
                MktConsumerRightObject consumerRightObject = marketingDomain.getConsumerRightById(consumerRightId);
                if (consumerRightObject != null) {
                    mktDrawRule.setMktConsumerRight(new MktConsumerRight(consumerRightObject));
                    prize.setPrizeTypeCode(consumerRightObject.getTypeCode());
                }
            }

            //save prize
            MktDrawRecordObject saveRecord = marketingDomain.createMktDrawRecord(record);

            prize.setDrawRuleId(mktDrawRule.getId());
            prize.setAmount(mktDrawRule.getAmount());
            if (prize.getPrizeTypeCode().equals(LookupCodes.MktPrizeType.WEBCHAT)) {
                prize.setPrizeAccount(saveRecord.getOauthOpenid());
            }
            setAccount(prize);
            prize.setDrawRecordId(saveRecord.getId());
            MktDrawPrizeObject prizeObject = marketingDomain.createMktDrawPrize(prize);
            return mktDrawRule;

        } else {
            record.setIsPrized(false);
            marketingDomain.createMktDrawRecord(record);

            return null;
        }
    }

    @RequestMapping(value = "drawRule/{id}", method = RequestMethod.GET)
    public List<MktDrawRule> getMarketingRuleList(@PathVariable(value = "id") String marketingId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        return marketingDomain.getRuleList(marketingId).stream().map(MktDrawRule::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Marketing getMarketing(@PathVariable(value = "id") String marketingId) {
        MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
        if (marketingObject == null) {
            throw new NotFoundException("marketing not found");
        }
        return new Marketing(marketingObject);
    }

    @RequestMapping(value = "drawPrize/{id}/top", method = RequestMethod.GET)
    public List<MktDrawPrize> getTop10MarketingPrizeList(@PathVariable(value = "id") String marketingId, @RequestParam(value = "ys_id", required = false) String ysId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        List<MktDrawPrizeObject> mktDrawPrizeObjectList = marketingDomain.getTop10PrizeList(marketingId, ysId);
        List<MktDrawPrize> mktDrawPrizeList = new ArrayList<>();

        mktDrawPrizeObjectList.forEach(object -> {
            MktDrawPrize mktDrawPrize = new MktDrawPrize(object);
            MktDrawRuleObject mktDrawRuleObject = marketingDomain.getDrawRuleById(object.getDrawRuleId());
            MktConsumerRightObject mktConsumerRightObject = marketingDomain.getConsumerRightById(mktDrawRuleObject.getConsumerRightId());
            if (mktConsumerRightObject != null) {
                mktDrawPrize.setMktConsumerRight(new MktConsumerRight(mktConsumerRightObject));
            }
            mktDrawPrizeList.add(mktDrawPrize);
        });

        return mktDrawPrizeList;
    }

    // query wechat prize top 10
    @RequestMapping(value = "drawPrize/{id}/top10", method = RequestMethod.GET)
    public WeChatPrize getTop10MarketingPrizeWeChatList(@PathVariable(value = "id") String marketingId, @RequestParam(value = "ys_id", required = false) String ysId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        WeChatPrize weChatPrize = new WeChatPrize();
        Long totalNumber = marketingDomain.getPrizeNumberByMarketingId(marketingId);
        weChatPrize.setPrizeCount(totalNumber.intValue());
        List<MktDrawPrizeObject> mktDrawPrizeObjectList = marketingDomain.getTop10PrizeList(marketingId, ysId);
        List<MktDrawPrize> mktDrawPrizeList = new ArrayList<>();

        mktDrawPrizeObjectList.forEach(object -> {
            MktDrawPrize mktDrawPrize = new MktDrawPrize(object);
            mktDrawPrizeList.add(mktDrawPrize);
        });
        weChatPrize.setWechatPrize(mktDrawPrizeList);

        return weChatPrize;
    }

    @RequestMapping(value = "redpack/{scenario_id}", method = RequestMethod.POST)
    public MktRedPackResult sendWeChatRedPack(@PathVariable("scenario_id") Number scenarioId, @RequestParam("openid") String openId) {
        String key = productDomain.getKeyFromRadis(scenarioId, "");
        logger.info("current key is: " + key + ", with scenarioId: " + scenarioId);
        productDomain.clearKeyToRadis(scenarioId);  //clear the key
        MktRedPackResult result = new MktRedPackResult();
        result.setExisted(true);
        if (StringUtils.hasText(key)) {
            Product product = productService.getProductByKey(key);
            MktDrawRecordObject existRecord = marketingDomain.getMktDrawRecordByProductKey(key);

            if (existRecord == null) {
                result.setExisted(false);
                ProductKeyBatchObject keyBatchObject = productDomain.getProductKeyBatch(product.getKeyBatchId());
                UserScanRecordObject recordObject = userScanDomain.getLatestScanRecordByProductKey(key);
                WeChatServerConfig config = weChatService.getOrgIdHasWeChatSettings(keyBatchObject.getOrgId());
                if (config == null)
                    throw new NotFoundException("wechat settings not found with org: " + keyBatchObject.getOrgId());

                WeChatUser weChatUser = weChatService.getWeChatUser(openId, config.getOrgId());
                MarketingObject marketingObject = marketingDomain.getMarketingById(keyBatchObject.getMarketingId());

                UserObject existUser = userDomain.getUserByOpenIdAndType(openId, "webchat");
                if (existUser == null) {
                    UserObject userObject = new UserObject();
                    userObject.setOauthOpenid(openId);
                    userObject.setOauthTypeCode("webchat");
                    userObject.setCity(weChatUser.getCity());
                    userObject.setProvince(weChatUser.getProvince());
                    userObject.setGravatarUrl(weChatUser.getImageUrl());
                    userObject.setSex(weChatUser.getSex().equals("1") ? false : true);
                    userObject.setName(weChatUser.getNickName());
                    existUser = userDomain.createUser(userObject);
                }
                MktDrawPrizeObject prize = new MktDrawPrizeObject();
                prize.setPrizeAccountName(existUser.getName());
                prize.setMarketingId(marketingObject.getId());
                prize.setProductKey(key);

                //save prize
                MktDrawRecordObject record = new MktDrawRecordObject();
                record.setOauthOpenid(openId);
                record.setProductBaseId(product.getProductBaseId());
                record.setProductKey(key);
                record.setMarketingId(marketingObject.getId());
                record.setUserId(existUser.getId());

                // apply to no scan record id
                if (recordObject == null) {
                    String tempScanRecordId = ObjectIdGenerator.getNew();
                    record.setScanRecordId(tempScanRecordId);
                    prize.setScanRecordId(tempScanRecordId);
                } else
                    record.setScanRecordId(recordObject.getId());

                MktDrawRuleObject mktDrawRuleObject = marketingDomain.getMktRandomPrize(marketingObject.getId(), record.getScanRecordId(), key);

                if (mktDrawRuleObject != null) {
                    result.setIsPrized(true);
                    result.setAmount(new BigDecimal(mktDrawRuleObject.getAmount()));

                    record.setIsPrized(true);
                    prize.setPrizeTypeCode(LookupCodes.MktPrizeType.WEBCHAT);
                    prize.setPrizeAccount(openId);
                    prize.setAccountType(LookupCodes.MktPrizeType.WEBCHAT);

                    MktDrawRule mktDrawRule = new MktDrawRule(mktDrawRuleObject);

                    //save prize
                    MktDrawRecordObject saveRecord = marketingDomain.createMktDrawRecord(record);
                    prize.setDrawRuleId(mktDrawRule.getId());
                    prize.setAmount(mktDrawRule.getAmount());
                    prize.setDrawRecordId(saveRecord.getId());
                    MktDrawPrizeObject prizeObject = marketingDomain.createMktDrawPrize(prize);

                    //send red pack
                    if(marketingDomain.sendWeChatRedPack(marketingObject,openId,mktDrawRule.getAmount(), saveRecord.getId(), config.getOrgId() )) {
                        //set paid status
                        prizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                        prizeObject.setPaidDateTime(DateTime.now());
                        marketingDomain.updateMktDrawPrize(prizeObject);
                    }
                    else
                        result.setFailed(true);

                } else {
                    record.setIsPrized(false);
                    marketingDomain.createMktDrawRecord(record);
                }
            }
            logger.info("wechat red pack result: " + result.toString());
            return result;
        }

        return null;
    }


    private void setAccount(MktDrawPrizeObject prize) {
        prize.setAccountType(prize.getPrizeTypeCode());

        if (LookupCodes.MktPrizeType.MOBILE_FEE.equals(prize.getPrizeTypeCode()) || LookupCodes.MktPrizeType.MOBILE_DATA.equals(prize.getPrizeTypeCode())) {
            prize.setAccountType("mobile");
            prize.setPrizeAccountName("手机用户");
        } else if (LookupCodes.MktPrizeType.COUPON.equals(prize.getPrizeTypeCode())) {
            if (prize.getPrizeAccountName() == null)
                prize.setPrizeAccountName("手机用户");
        }
    }

}
