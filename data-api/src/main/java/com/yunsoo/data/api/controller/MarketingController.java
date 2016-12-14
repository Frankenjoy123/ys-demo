package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.*;
import com.yunsoo.data.service.repository.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
@RestController
@RequestMapping("/marketing")
public class MarketingController {

    @Autowired
    private MarketingRepository marketingRepository;

    @Autowired
    private MktDrawRuleRepository mktDrawRuleRepository;

    @Autowired
    private MktDrawPrizeRepository mktDrawPrizeRepository;

    @Autowired
    private MktDrawRecordRepository mktDrawRecordRepository;

    @Autowired
    private MktConsumerRightRepository mktConsumerRightRepository;

    @Autowired
    private MktPrizeContactRepository mktPrizeContactRepository;

    @Autowired
    private ProductKeyBatchRepository productKeyBatchRepository;

    @Autowired
    private MktConsumerRightRedeemCodeRepository mktConsumerRightRedeemCodeRepository;

    @Autowired
    private MktDrawRuleKeyRepository mktDrawRuleKeyRepository;

    @Autowired
    private MktDrawPrizeReportRepository mktDrawPrizeReportRepository;

    @Autowired
    private MktSellerRepository mktSellerRepository;


    @Autowired
    private MktPrizeCostRepository costRepository;

    //get marketing plan by id, provide API
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public MarketingObject getMarketingById(@PathVariable String id) {
        MarketingEntity entity = findMarketingById(id);
        return toMarketingObject(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void updateMarketing(@PathVariable String id, @RequestBody MarketingObject marketingObject) {
        MarketingEntity oldEntity = findMarketingById(id);
        MarketingEntity entity = toMarketingEntity(marketingObject);
        entity.setCreatedDateTime(oldEntity.getCreatedDateTime());
        entity.setCreatedAccountId(oldEntity.getCreatedAccountId());
        marketingRepository.save(entity);
    }

    //get mktDrawPrize by product key, provide API-Rabbit
    @RequestMapping(value = "/drawprize/{key}", method = RequestMethod.GET)
    public MktDrawPrizeObject getMktDrawPrizeByProductKey(@PathVariable String key) {
        List<MktDrawPrizeEntity> entities = mktDrawPrizeRepository.findByProductKey(key);
        if (entities.size() > 0) {
            MktDrawPrizeEntity entity = entities.get(0);
            return toMktDrawPrizeObject(entity);
        } else {
            return null;
        }
    }

    //get mktDrawPrize by product key and ysid, provide API-Rabbit
    @RequestMapping(value = "/drawprize/{key}/user/{id}", method = RequestMethod.GET)
    public MktDrawPrizeObject getMktDrawPrizeByProductKeyAndUser(@PathVariable(value = "key") String key, @PathVariable(value = "id") String id,
                                                                 @RequestParam(value = "oauth_openid", required = false) String oauthOpenid) {

        List<MktDrawRecordEntity> mktDrawRecordEntities = new ArrayList<>();
        if (!StringUtils.isEmpty(oauthOpenid)) {
            mktDrawRecordEntities = mktDrawRecordRepository.findByProductKeyAndOauthOpenidAndIsPrized(key, oauthOpenid, true);
        } else {
            mktDrawRecordEntities = mktDrawRecordRepository.findByProductKeyAndYsidAndIsPrized(key, id, true);
        }
        if (mktDrawRecordEntities.size() > 0) {
            MktDrawRecordEntity mktDrawRecordEntity = mktDrawRecordEntities.get(0);
            String scanRecordId = mktDrawRecordEntity.getScanRecordId();
            List<MktDrawPrizeEntity> mktDrawPrizeEntityList = mktDrawPrizeRepository.findByScanRecordId(scanRecordId);
            if (mktDrawPrizeEntityList.size() > 0) {
                MktDrawPrizeEntity entity = mktDrawPrizeEntityList.get(0);
                return toMktDrawPrizeObject(entity);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    //get mktDrawRecord by product key, provide API-Rabbit
    @RequestMapping(value = "/draw", method = RequestMethod.GET)
    public List<MktDrawRecordObject> queryRecord(@RequestParam("ys_id") String ysId, @RequestParam("marketing_id") String marketingId) {
        List<MktDrawRecordEntity> entities = mktDrawRecordRepository.findByYsidAndMarketingId(ysId, marketingId);
        return entities.stream().map(this::toMktDrawRecordObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/draw/{key}", method = RequestMethod.GET)
    public MktDrawRecordObject getMktDrawRecordByProductKey(@PathVariable("key") String key) {
        List<MktDrawRecordEntity> entities = mktDrawRecordRepository.findByProductKey(key);
        if (entities.size() > 0) {
            MktDrawRecordEntity entity = entities.get(0);
            return toMktDrawRecordObject(entity);
        } else {
            return null;
        }
    }

    // query marketing draw record by product key and ysid
    @RequestMapping(value = "/draw/{key}/user/{id}", method = RequestMethod.GET)
    public MktDrawRecordObject getMktDrawRecordByProductKeyAndUser(@PathVariable(value = "key") String key, @PathVariable(value = "id") String id,
                                                                   @RequestParam(value = "oauth_openid") String oauthOpenId) {
        List<MktDrawRecordEntity> entities = mktDrawRecordRepository.findByProductKeyAndOauthOpenid(key, oauthOpenId);
        if (entities.size() > 0) {
            MktDrawRecordEntity entity = entities.get(0);
            return toMktDrawRecordObject(entity);
        } else {
            return null;
        }
    }

    //get marketing plan by id, provide API
    @RequestMapping(value = "/seller/wechat/{openid}", method = RequestMethod.GET)
    public MktSellerObject getMktSellerByOpenid(@PathVariable String openid) {

        MktSellerEntity entity = mktSellerRepository.findOne(openid);
        if (entity == null) {
            throw new NotFoundException("marketing wechat seller not found by [openid: " + openid + ']');
        }
        return toMktSellerObject(entity);
    }

    //get wechat marketing plan by openid, provide API-rabbit
    @RequestMapping(value = "/seller/wechat/marketing/{id}", method = RequestMethod.GET)
    public List<MarketingObject> getWechatMarketingByOpenid(@PathVariable String id) {

        List<MarketingEntity> entities = marketingRepository.findByCreatedAccountIdAndTypeCodeOrderByCreatedDateTimeDesc(id, LookupCodes.MktType.DRAW04);
        return entities.stream().map(this::toMarketingObject).collect(Collectors.toList());
    }




    //query marketing plan, provide API
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<MarketingObject> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                             @RequestParam(value = "org_ids", required = false) List<String> orgIds,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "search_text", required = false) String searchText,
                                             @RequestParam(value = "start_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime startTime,
                                             @RequestParam(value = "end_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime endTime,
                                             @RequestParam(value = "product_base_id", required = false) String productBaseId,

                                             Pageable pageable,
                                             HttpServletResponse response) {
        Page<MarketingEntity> entityPage = null;
        if ((status != null) && (orgId != null) && (!LookupCodes.MktStatus.AVALAIBLESTATUS.equals(status))) {
            entityPage = marketingRepository.findByOrgIdAndStatusCodeOrderByCreatedDateTimeDesc(orgId, status, pageable);
        } else {
            if (LookupCodes.MktStatus.AVALAIBLESTATUS.equals(status)) {
                entityPage = marketingRepository.findByOrgIdAndStatusCodeInOrderByCreatedDateTimeDesc(orgId, LookupCodes.MktStatus.ANALYZE_STATUS, pageable);
            } else {
                if (orgId != null)
//                    entityPage = marketingRepository.findByOrgIdAndStatusCodeInOrderByCreatedDateTimeDesc(orgId, LookupCodes.MktStatus.AVALAIBLE_STATUS, pageable);
                    entityPage = marketingRepository.queryMkt(orgId, startTime, endTime, productBaseId, searchText, pageable);

                else if (orgIds != null && orgIds.size() > 0) {
                    entityPage = marketingRepository.query(orgIds, status, startTime, endTime, productBaseId, searchText, pageable);
                } else
                    throw new BadRequestException("one of the request parameter org_id or org_ids is required");
            }
        }


        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.getContent().stream()
                .map(this::toMarketingObject)
                .collect(Collectors.toList());
    }

    //query marketing plan, provide API
    @RequestMapping(value = "consumer", method = RequestMethod.GET)
    public List<MktConsumerRightObject> getConsumerRightByFilter(@RequestParam(value = "org_id") String orgId,
                                                                 @RequestParam(value = "type_code", required = false) String typeCode,
                                                                 Pageable pageable,
                                                                 HttpServletResponse response) {

        Page<MktConsumerRightEntity> entityPage = null;

        if (orgId == null) {
            throw new BadRequestException("org id is required");
        }

        if (typeCode != null) {
            entityPage = mktConsumerRightRepository.findByOrgIdAndStatusCodeAndTypeCode(orgId, LookupCodes.MktConsumerRightStatus.CREATED, typeCode, pageable);
        } else {

            entityPage = mktConsumerRightRepository.findByOrgIdAndStatusCode(orgId, LookupCodes.MktConsumerRightStatus.CREATED, pageable);
        }

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.getContent().stream()
                .map(this::toMktConsumerRightObject)
                .collect(Collectors.toList());
    }

    //get marketing consumer right by id
    @RequestMapping(value = "consumer/{id}", method = RequestMethod.GET)
    public MktConsumerRightObject getMktConsumerRightById(@PathVariable(value = "id") String id) {
        MktConsumerRightEntity entity = mktConsumerRightRepository.findOne(id);
        if (entity == null || LookupCodes.MktConsumerRightStatus.DELETED.equals(entity.getStatusCode())) {
            throw new NotFoundException("marketing consumer right not found by [id:" + id + "]");
        }
        return toMktConsumerRightObject(entity);
    }

    //update marketing consumer right
    @RequestMapping(value = "consumer/{id}", method = RequestMethod.PUT)
    public void updateMktConsumerRight(@PathVariable(value = "id") String id,
                                       @RequestBody MktConsumerRightObject mktConsumerRightObject) {
        MktConsumerRightEntity oldentity = mktConsumerRightRepository.findOne(id);
        if (oldentity != null && !LookupCodes.MktConsumerRightStatus.DELETED.equals(oldentity.getStatusCode())) {
            MktConsumerRightEntity entity = toMktConsumerRightEntity(mktConsumerRightObject);
            mktConsumerRightRepository.save(entity);
        }
    }

    //delete
    @RequestMapping(value = "consumer/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        MktConsumerRightEntity entity = mktConsumerRightRepository.findOne(id);
        if (entity != null && !LookupCodes.MktConsumerRightStatus.DELETED.equals(entity.getStatusCode())) {
            entity.setStatusCode(LookupCodes.MktConsumerRightStatus.DELETED);
            mktConsumerRightRepository.save(entity);
        }
    }

    //create marketing consumer right
    @RequestMapping(value = "consumer", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktConsumerRightObject createMktConsumerRight(@RequestBody MktConsumerRightObject mktConsumerRightObject) {
        MktConsumerRightEntity entity = toMktConsumerRightEntity(mktConsumerRightObject);
        entity.setId(null);
        entity.setStatusCode(LookupCodes.MktConsumerRightStatus.CREATED);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        MktConsumerRightEntity newEntity = mktConsumerRightRepository.save(entity);
        return toMktConsumerRightObject(newEntity);
    }

    //get marketing consumer right by key
    @RequestMapping(value = "consumer/key/{key}", method = RequestMethod.GET)
    public MktConsumerRightObject getMktConsumerRightByProductKey(@PathVariable(value = "key") String key) {

        List<MktDrawPrizeEntity> mktDrawPrizeEntityList = mktDrawPrizeRepository.findByProductKey(key);
        if (mktDrawPrizeEntityList.size() > 0) {
            MktDrawPrizeEntity mktDrawPrizeEntity = mktDrawPrizeEntityList.get(0);
            String drawRuleId = mktDrawPrizeEntity.getDrawRuleId();
            MktDrawRuleEntity mktDrawRuleEntity = mktDrawRuleRepository.findOne(drawRuleId);
            if (mktDrawRuleEntity != null) {
                String consumerRightId = mktDrawRuleEntity.getConsumerRightId();
                if (consumerRightId != null) {
                    MktConsumerRightEntity mktConsumerRightEntity = mktConsumerRightRepository.findOne(consumerRightId);
                    if (mktConsumerRightEntity != null) {
                        return toMktConsumerRightObject(mktConsumerRightEntity);
                    } else {
                        return null;
                    }

                } else {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    //get marketing consumer right by key and ysid
    @RequestMapping(value = "consumer/key/{key}/user/{id}", method = RequestMethod.GET)
    public MktConsumerRightObject getMktConsumerRightByProductKeyAndUser(@PathVariable(value = "key") String key, @PathVariable(value = "id") String id) {

        List<MktDrawRecordEntity> mktDrawRecordEntityList = mktDrawRecordRepository.findByProductKeyAndYsidAndIsPrized(key, id, true);
        if (mktDrawRecordEntityList.size() > 0) {
            MktDrawRecordEntity mktDrawRecordEntity = mktDrawRecordEntityList.get(0);
            String scanRecordId = mktDrawRecordEntity.getScanRecordId();
            List<MktDrawPrizeEntity> mktDrawPrizeEntityList = mktDrawPrizeRepository.findByScanRecordId(scanRecordId);
            if (mktDrawPrizeEntityList.size() > 0) {
                MktDrawPrizeEntity mktDrawPrizeEntity = mktDrawPrizeEntityList.get(0);
                String drawRuleId = mktDrawPrizeEntity.getDrawRuleId();
                MktDrawRuleEntity mktDrawRuleEntity = mktDrawRuleRepository.findOne(drawRuleId);
                if (mktDrawRuleEntity != null) {
                    String consumerRightId = mktDrawRuleEntity.getConsumerRightId();
                    if (consumerRightId != null) {
                        MktConsumerRightEntity mktConsumerRightEntity = mktConsumerRightRepository.findOne(consumerRightId);
                        if (mktConsumerRightEntity != null) {
                            return toMktConsumerRightObject(mktConsumerRightEntity);
                        } else {
                            return null;
                        }

                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    //get marketing consumer right redeem code by product key
    @RequestMapping(value = "consumer/redeemcode/{key}", method = RequestMethod.GET)
    public MktConsumerRightRedeemCodeObject getRedeemCodeByConsumerRightId(@PathVariable String key) {

        List<MktDrawPrizeEntity> mktDrawPrizeEntityList = mktDrawPrizeRepository.findByProductKey(key);
        if (mktDrawPrizeEntityList.size() > 0) {
            MktDrawPrizeEntity mktDrawPrizeEntity = mktDrawPrizeEntityList.get(0);
            String prizeId = mktDrawPrizeEntity.getDrawRecordId();
            String ruleId = mktDrawPrizeEntity.getDrawRuleId();

            List<MktConsumerRightRedeemCodeEntity> mktConsumerRightRedeemCodeEntities = mktConsumerRightRedeemCodeRepository.findByDrawPrizeId(prizeId);
            if (mktConsumerRightRedeemCodeEntities.size() > 0) {
                MktConsumerRightRedeemCodeEntity mktConsumerRightRedeemCodeEntity = mktConsumerRightRedeemCodeEntities.get(0);
                return toMktConsumerRightRedeemCodeObject(mktConsumerRightRedeemCodeEntity);

            } else {

                if (ruleId != null) {
                    MktDrawRuleEntity mktDrawRuleEntity = mktDrawRuleRepository.findOne(ruleId);
                    if (mktDrawRuleEntity != null) {
                        String consumerRightId = mktDrawRuleEntity.getConsumerRightId();
                        if (consumerRightId != null) {
                            MktConsumerRightRedeemCodeEntity entity = mktConsumerRightRedeemCodeRepository.findTop1ByConsumerRightIdAndStatusCodeOrderByCreatedDateTime(consumerRightId, LookupCodes.MktConsumerRightRedeemCodeStatus.AVAILABLE);
                            if (entity == null) {
                                throw new NotFoundException("consumer right redeem code not found.");
                            }
                            if (entity.getTypeCode().equals(LookupCodes.MktConsumerRightRedeemCodeType.UNIQUE)) {
                                entity.setStatusCode(LookupCodes.MktConsumerRightRedeemCodeStatus.USED);
                                entity.setModifiedDateTime(DateTime.now());
                                entity.setDrawPrizeId(prizeId);
                            } else if (entity.getTypeCode().equals(LookupCodes.MktConsumerRightRedeemCodeType.COMMON)) {
                                entity.setModifiedDateTime(DateTime.now());
                                entity.setDrawPrizeId(prizeId);
                            }
                            mktConsumerRightRedeemCodeRepository.save(entity);
                            MktDrawPrizeEntity prizeEntity = mktDrawPrizeRepository.findOne(prizeId);
                            prizeEntity.setPrizeContent(entity.getValue());
                            mktDrawPrizeRepository.save(prizeEntity);
                            return toMktConsumerRightRedeemCodeObject(entity);

                        } else {
                            return null;
                        }
                    } else {
                        throw new NotFoundException("marketing rule not found.");
                    }
                } else {
                    throw new NotFoundException("marketing rule not found.");
                }
            }
        } else {
            throw new NotFoundException("prize record not found.");
        }

    }

    //get marketing consumer right redeem code for prize
    @RequestMapping(value = "consumer/redeemcode/generate/{key}", method = RequestMethod.GET)
    public MktConsumerRightRedeemCodeObject getRedeemCodeByProductKey(@PathVariable String key,
                                                                      @RequestParam(value = "draw_rule_id") String drawRuleId) {

        MktDrawRuleEntity mktDrawRuleEntity = mktDrawRuleRepository.findOne(drawRuleId);
        if (mktDrawRuleEntity != null) {
            String consumerRightId = mktDrawRuleEntity.getConsumerRightId();
            if (consumerRightId != null) {
                MktConsumerRightRedeemCodeEntity entity = mktConsumerRightRedeemCodeRepository.findTop1ByConsumerRightIdAndStatusCodeOrderByCreatedDateTime(consumerRightId, LookupCodes.MktConsumerRightRedeemCodeStatus.AVAILABLE);
                if (entity != null) {
                    if (entity.getTypeCode().equals(LookupCodes.MktConsumerRightRedeemCodeType.UNIQUE)) {
                        entity.setStatusCode(LookupCodes.MktConsumerRightRedeemCodeStatus.USED);
                        entity.setModifiedDateTime(DateTime.now());
                    } else if (entity.getTypeCode().equals(LookupCodes.MktConsumerRightRedeemCodeType.COMMON)) {
                        entity.setModifiedDateTime(DateTime.now());
                    }
                    mktConsumerRightRedeemCodeRepository.save(entity);
                    return toMktConsumerRightRedeemCodeObject(entity);
                } else {
                    return null;
                }

            } else {
                return null;
            }
        } else {
            throw new NotFoundException("draw rule not found.");
        }
    }



    //query marketing prize, provide API
    @RequestMapping(value = "/drawRecord/sum", method = RequestMethod.GET)
    public Long countDrawRecordByMarketingId(
            @RequestParam(value = "marketing_id") String marketingId,
            @RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
            @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime) {
        if (marketingId == null) {
            throw new BadRequestException("marketing id is not valid");
        }

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if ((startTime != null) && !StringUtils.isEmpty(startTime.toString()))
            startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        if ((endTime != null) && !StringUtils.isEmpty(endTime.toString()))
            endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        Long quantity = mktDrawRecordRepository.sumByMarketingId(marketingId, startDateTime, endDateTime);
        return quantity;
    }

    //query marketing prize, provide API
    @RequestMapping(value = "/totalcount", method = RequestMethod.GET)
    public Long countMarketingsByOrgId(
            @RequestParam(value = "org_id") String orgId) {
        if (orgId == null) {
            throw new BadRequestException("orgId id is not valid");
        }
        Long quantity = marketingRepository.countByOrgIdAndStatusCodeIn(orgId, LookupCodes.MktStatus.AVALAIBLE_STATUS);
        return quantity;
    }

    @RequestMapping(value = "/drawPrize/totalcount", method = RequestMethod.GET)
    public Long countMktDrawPrizesByOrgId(@RequestParam(value = "org_id") String orgId) {
        if (orgId == null) {
            throw new BadRequestException("org id is not valid.");
        }
        List<String> marketingIdIn = marketingRepository.findMarketingIdByOrgId(orgId);
        if (marketingIdIn != null) {
            return mktDrawPrizeRepository.countByMarketingIdIn(marketingIdIn);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/drawPrize/totalcount/marketing/{id}", method = RequestMethod.GET)
    public Long countMktDrawPrizesByMarketingId(@PathVariable(value = "id") String marketingId) {
        return mktDrawPrizeRepository.countByMarketingId(marketingId);

    }


    //query marketing prize, provide API
    @RequestMapping(value = "/drawPrize/sum", method = RequestMethod.GET)
    public Long countDrawPrizeByDrawRuleId(
            @RequestParam(value = "draw_rule_id") String drawRuleId,
            @RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
            @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime) {
        if (drawRuleId == null) {
            throw new BadRequestException("draw rule id is not valid");
        }
        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if ((startTime != null) && !StringUtils.isEmpty(startTime.toString()))
            startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        if ((endTime != null) && !StringUtils.isEmpty(endTime.toString()))
            endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        Long quantity = mktDrawPrizeRepository.sumDrawRuleId(drawRuleId, startDateTime, endDateTime);
        return quantity;
    }


    //query marketing prize, provide API
    @RequestMapping(value = "/drawPrize/marketing", method = RequestMethod.GET)
    public List<MktDrawPrizeObject> getMktDrawPrizeByMarketingId(
            @RequestParam(value = "marketing_id") String marketingId,
            @RequestParam(value = "account_type", required = false) String accountType,
            @RequestParam(value = "prize_type_code", required = false) String prizeTypeCode,
            @RequestParam(value = "status_code", required = false) String statusCode,
            @RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
            @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
            Pageable pageable,
            HttpServletResponse response) {

        if (StringUtils.isEmpty(accountType))
            accountType = null;
        if (StringUtils.isEmpty(prizeTypeCode))
            prizeTypeCode = null;

        if (StringUtils.isEmpty(statusCode))
            statusCode = null;

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if ((startTime != null) && !StringUtils.isEmpty(startTime.toString()))
            startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        if ((endTime != null) && !StringUtils.isEmpty(endTime.toString()))
            endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        Page<MktDrawPrizeEntity> entityPage = mktDrawPrizeRepository.query(marketingId, accountType, prizeTypeCode, statusCode, startDateTime, endDateTime, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.getContent().stream()
                .map(this::toMktDrawPrizeObject)
                .collect(Collectors.toList());
    }

    // query top 10 prize info
    @RequestMapping(value = "/drawPrize/{id}/top", method = RequestMethod.GET)
    public List<MktDrawPrizeObject> getTop10MktDrawPrizeByMarketingId(
            @PathVariable(value = "id") String marketingId,
            @RequestParam(value = "status_code_in", required = false)List<String> statusCodeIn,
            @RequestParam(value = "record_ids", required = false)List<String> recordIds) {

        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        List<MktDrawPrizeEntity> entityList;
        if(recordIds == null)
            entityList = mktDrawPrizeRepository.findTop10ByMarketingIdAndStatusCodeInOrderByCreatedDateTimeDesc(marketingId, statusCodeIn);
        else
            entityList = mktDrawPrizeRepository.findTop10ByMarketingIdAndStatusCodeInAndDrawRecordIdInOrderByCreatedDateTimeDesc(marketingId, statusCodeIn, recordIds);

        return entityList.stream().map(this::toMktDrawPrizeObject).collect(Collectors.toList());
    }

    //query marketing prize, provide API
    @RequestMapping(value = "/drawPrizeReport/marketing", method = RequestMethod.GET)
    public List<MktDrawPrizeReportObject> getMktDrawPrizeReportByMarketingId(
            @RequestParam(value = "marketing_id") String marketingId,
            @RequestParam(value = "account_type", required = false) String accountType,
            @RequestParam(value = "prize_type_code", required = false) String prizeTypeCode,
            @RequestParam(value = "status_code", required = false) String statusCode,
            @RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
            @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime) {

        if (StringUtils.isEmpty(accountType))
            accountType = null;
        if (StringUtils.isEmpty(prizeTypeCode))
            prizeTypeCode = null;

        if (StringUtils.isEmpty(statusCode))
            statusCode = null;

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if ((startTime != null) && !StringUtils.isEmpty(startTime.toString()))
            startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        if ((endTime != null) && !StringUtils.isEmpty(endTime.toString()))
            endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<MktDrawPrizeReportEntity> entityList = mktDrawPrizeReportRepository.queryMktDrawPrizeReport(marketingId, accountType, prizeTypeCode, statusCode, startDateTime, endDateTime);
        if ((entityList != null) && (entityList.size() > 0)) {
            return entityList.stream()
                    .map(this::toMktDrawPrizeReportObject)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }


    //create marketing plan, provide API
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MarketingObject createMarketing(@RequestBody MarketingObject marketingObject) {
        MarketingEntity entity = toMarketingEntity(marketingObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        entity.setStatusCode(LookupCodes.MktStatus.CREATED);

        MarketingEntity newEntity = marketingRepository.save(entity);
        return toMarketingObject(newEntity);
    }

    //create marketing draw record by key, provide: API-Rabbit
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRecordObject createDrawRecord(@RequestBody MktDrawRecordObject mktDrawRecordObject) {
//        String productKey = mktDrawRecordObject.getProductKey();
//        Product product = productService.getByKey(productKey);
//        if (product == null) {
//            throw new NotFoundException("Product");
//        }
//        List<MktDrawRecordEntity> mktDrawRecordEntityList = mktDrawRecordRepository.findByProductKey(productKey);
//        if (mktDrawRecordEntityList.size() > 0) {
//            throw new ConflictException("This product has been already drawed.");
//        }

        MktDrawRecordEntity entity = toMktDrawRecordEntity(mktDrawRecordObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }

        try {
            MktDrawRecordEntity newEntity = mktDrawRecordRepository.save(entity);

            return toMktDrawRecordObject(newEntity);
        }
        catch (org.hibernate.exception.ConstraintViolationException ex){
            throw new ConflictException("This product has been already drawed.");
        }
    }

    @RequestMapping(value = "/draw/{id}", method = RequestMethod.PATCH)
    public void patchDrawRecord(@PathVariable("id")String id, @RequestBody MktDrawRecordObject record){
        MktDrawRecordEntity entity = mktDrawRecordRepository.findOne(id);
        if(entity!=null){
            if(record.getYsid()!=null && !record.getYsid().equals(entity.getYsid()))
                entity.setYsid(record.getYsid());

            if(record.getOauthOpenid() != null && !record.getOauthOpenid().equals(entity.getOauthOpenid()))
                entity.setOauthOpenid(record.getOauthOpenid());

            mktDrawRecordRepository.save(entity);
        }
        else
            throw new NotFoundException("the draw record not found with id: " + id);
    }

    //create marketing draw prize by draw record id, provide: API-Rabbit
    @RequestMapping(value = "/drawPrize", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawPrizeObject createDrawPrize(@RequestBody MktDrawPrizeObject mktDrawPrizeObject) {
        String drawRecordId = mktDrawPrizeObject.getDrawRecordId();

        MktDrawRecordEntity mktDrawRecordEntity = mktDrawRecordRepository.findOne(drawRecordId);
        if (mktDrawRecordEntity == null) {
            throw new NotFoundException("This draw record has not been found.");
        }

        MktDrawPrizeEntity entity = toMktDrawPrizeEntity(mktDrawPrizeObject);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setStatusCode(LookupCodes.MktDrawPrizeStatus.CREATED);
        entity.setScanRecordId(mktDrawRecordEntity.getScanRecordId());
        entity.setProductKey(mktDrawRecordEntity.getProductKey());
        MktDrawPrizeEntity newEntity = mktDrawPrizeRepository.save(entity);

        MarketingEntity marketing = marketingRepository.findOne(mktDrawPrizeObject.getMarketingId());
        marketing.setBalance(marketing.getBalance() - mktDrawPrizeObject.getAmount());
        marketingRepository.save(marketing);


        if (marketing.getTypeCode().equals(LookupCodes.MktType.ENVELOPE)) {
            List<MktDrawRuleEntity> drawRuleEntityList = mktDrawRuleRepository.findByMarketingIdOrderById(marketing.getId());
            drawRuleEntityList.forEach(mktDrawRuleEntity -> {
                if (mktDrawRuleEntity.getAvailableQuantity() != null) {
                    mktDrawRuleEntity.setAvailableQuantity(mktDrawRuleEntity.getAvailableQuantity() - 1);
                    mktDrawRuleRepository.save(mktDrawRuleEntity);
                }
            });
        } else {
            MktDrawRuleEntity mktDrawRuleEntity = mktDrawRuleRepository.findOne(mktDrawPrizeObject.getDrawRuleId());
            if (mktDrawRuleEntity.getAvailableQuantity() != null) {
                mktDrawRuleEntity.setAvailableQuantity(mktDrawRuleEntity.getAvailableQuantity() - 1);
                mktDrawRuleRepository.save(mktDrawRuleEntity);
            }

        }


        return toMktDrawPrizeObject(newEntity);
    }

    //get marketing prize contact by id
    @RequestMapping(value = "/drawPrize/contact/{id}", method = RequestMethod.GET)
    public MktPrizeContactObject getMktPrizeContactById(@PathVariable(value = "id") String id) {
        MktPrizeContactEntity entity = mktPrizeContactRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("marketing prize contact not found by [id:" + id + "]");
        }
        return toMktPrizeContactObject(entity);
    }

    //get marketing prize contact by prize Id
    @RequestMapping(value = "/drawPrize/contact/prize/{id}", method = RequestMethod.GET)
    public MktPrizeContactObject getMktPrizeContactByPrizeId(@PathVariable(value = "id") String id) {
        List<MktPrizeContactEntity> mktPrizeContactEntityList = mktPrizeContactRepository.findByMktPrizeId(id);
        if ((mktPrizeContactEntityList != null) && (mktPrizeContactEntityList.size() > 0)) {
            MktPrizeContactEntity mktPrizeContactEntity = mktPrizeContactEntityList.get(0);
            return toMktPrizeContactObject(mktPrizeContactEntity);
        } else {
            return null;
        }
    }



    //create marketing prize contact, provide: API-Rabbit
    @RequestMapping(value = "/drawPrize/{id}/contact", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktPrizeContactObject createPrizeContact(@PathVariable String id, @RequestBody MktPrizeContactObject mktPrizeContactObject) {
        MktDrawPrizeEntity mktDrawPrizeEntity = mktDrawPrizeRepository.findOne(id);
        if ((mktDrawPrizeEntity == null) || ((mktDrawPrizeEntity.getPrizeContactId() != null) && (!mktDrawPrizeEntity.getPrizeContactId().equals("")))) {
            throw new NotFoundException("Invalid operation: not allowed to input prize contact info.");
        }

        MktPrizeContactEntity entity = toMktPrizeContactEntity(mktPrizeContactObject);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        if (entity.getMktPrizeId() == null) {
            entity.setMktPrizeId(id);
        }
        entity.setModifiedDateTime(null);
        MktPrizeContactEntity newEntity = mktPrizeContactRepository.save(entity);
        return toMktPrizeContactObject(newEntity);
    }

    //update marketing prize contact, provide: API-Rabbit
    @RequestMapping(value = "/drawPrize/{id}/contact", method = RequestMethod.PATCH)
    public void updatePrizeContact(@PathVariable String id, @RequestBody MktPrizeContactObject mktPrizeContactObject) {
        MktDrawPrizeEntity mktDrawPrizeEntity = mktDrawPrizeRepository.findOne(id);
        if ((mktDrawPrizeEntity == null) || (mktDrawPrizeEntity.getPrizeContactId() == null) || (mktDrawPrizeEntity.getPrizeContactId().equals(""))) {
            throw new NotFoundException("Invalid operation: not allowed to update prize contact info.");
        }
        MktPrizeContactEntity mktPrizeContactEntity = mktPrizeContactRepository.findOne(mktDrawPrizeEntity.getPrizeContactId());
        if (mktPrizeContactEntity == null) {
            throw new NotFoundException("This draw prize contact has not been found");
        }

        if (mktPrizeContactObject.getName() != null)
            mktPrizeContactEntity.setName(mktPrizeContactObject.getName());
        if (mktPrizeContactObject.getPhone() != null)
            mktPrizeContactEntity.setPhone(mktPrizeContactObject.getPhone());
        if (mktPrizeContactObject.getProvince() != null)
            mktPrizeContactEntity.setProvince(mktPrizeContactObject.getProvince());
        if (mktPrizeContactObject.getCity() != null)
            mktPrizeContactEntity.setCity(mktPrizeContactObject.getCity());
        if (mktPrizeContactObject.getDistrict() != null)
            mktPrizeContactEntity.setDistrict(mktPrizeContactObject.getDistrict());
        if (mktPrizeContactObject.getAddress() != null)
            mktPrizeContactEntity.setAddress(mktPrizeContactObject.getAddress());
        mktPrizeContactEntity.setModifiedDateTime(DateTime.now());
        mktPrizeContactRepository.save(mktPrizeContactEntity);
    }


    @RequestMapping(value = "/drawRule/{id}", method = RequestMethod.PUT)
    public void updateMktDrawRule(@PathVariable String id, @RequestBody MktDrawRuleObject mktDrawRuleObject) {
        MktDrawRuleEntity oldEntity = findMktDrawRuleById(id);
        MktDrawRuleEntity entity = toMktDrawRuleEntity(mktDrawRuleObject);
        entity.setModifiedDateTime(oldEntity.getModifiedDateTime());
        mktDrawRuleRepository.save(entity);
    }

    @RequestMapping(value = "/Rule/{id}", method = RequestMethod.GET)
    public MktDrawRuleObject getMktDrawRuleById(@PathVariable String id) {
        MktDrawRuleEntity entity = findMktDrawRuleById(id);
        return toMktDrawRuleObject(entity);
    }


    //update marketing draw prize by product key, provide: API-Rabbit
    @RequestMapping(value = "/drawPrize", method = RequestMethod.PATCH)
    public MktDrawPrizeObject updateDrawPrize(@RequestBody MktDrawPrizeObject mktDrawPrizeObject) {
        if(mktDrawPrizeObject ==null)
            throw new BadRequestException("marketing prize object is null");

        String productKey = mktDrawPrizeObject.getProductKey();
        List<MktDrawPrizeEntity> entities = mktDrawPrizeRepository.findByProductKey(productKey);
        if (entities.size() == 0) {
            throw new NotFoundException("This draw prize has not been found");
        }

        MktDrawPrizeEntity entity = entities.get(0);
        if (!entity.getDrawRecordId().equals(mktDrawPrizeObject.getDrawRecordId()))
            throw new NotFoundException("This draw prize has not been found");
        if(mktDrawPrizeObject.getPrizeAccount() != null)
            entity.setPrizeAccount(mktDrawPrizeObject.getPrizeAccount());
        if(mktDrawPrizeObject.getPrizeAccountName() != null)
            entity.setPrizeAccountName(mktDrawPrizeObject.getPrizeAccountName());

        if(mktDrawPrizeObject.getMobile() != null)
            entity.setMobile(mktDrawPrizeObject.getMobile());
        if(mktDrawPrizeObject.getStatusCode() != null)
            entity.setStatusCode(mktDrawPrizeObject.getStatusCode());
        if(mktDrawPrizeObject.getPaidDateTime() != null)
            entity.setPaidDateTime(mktDrawPrizeObject.getPaidDateTime());
        if(mktDrawPrizeObject.getPrizeContactId() != null)
            entity.setPrizeContactId(mktDrawPrizeObject.getPrizeContactId());
        if (LookupCodes.MktDrawPrizeStatus.PAID.equals(mktDrawPrizeObject.getStatusCode())) {
            entity.setPaidDateTime(DateTime.now());
        }
        MktDrawPrizeEntity newEntity = mktDrawPrizeRepository.save(entity);

        return toMktDrawPrizeObject(newEntity);
    }

    //update marketing draw prize when sending wechat red packets, provide: API-Rabbit
    @RequestMapping(value = "/drawPrize/wechat", method = RequestMethod.PATCH)
    public MktDrawPrizeObject updateWechatPrize(@RequestBody MktDrawPrizeObject mktDrawPrizeObject) {
        if (mktDrawPrizeObject == null)
            throw new BadRequestException("marketing prize object is null");

        MktDrawPrizeObject temp = findMktDrawPrizeById(mktDrawPrizeObject.getDrawRecordId());
        MktDrawPrizeEntity entity = toMktDrawPrizeEntity(temp);

        if (mktDrawPrizeObject.getPrizeAccount() != null)
            entity.setPrizeAccount(mktDrawPrizeObject.getPrizeAccount());
        if (mktDrawPrizeObject.getPrizeAccountName() != null)
            entity.setPrizeAccountName(mktDrawPrizeObject.getPrizeAccountName());
        if (mktDrawPrizeObject.getMobile() != null)
            entity.setMobile(mktDrawPrizeObject.getMobile());
        if (mktDrawPrizeObject.getStatusCode() != null)
            entity.setStatusCode(mktDrawPrizeObject.getStatusCode());
        if (mktDrawPrizeObject.getPaidDateTime() != null)
            entity.setPaidDateTime(mktDrawPrizeObject.getPaidDateTime());
        if (LookupCodes.MktDrawPrizeStatus.PAID.equals(mktDrawPrizeObject.getStatusCode())) {
            entity.setPaidDateTime(DateTime.now());
        }
        MktDrawPrizeEntity newEntity = mktDrawPrizeRepository.save(entity);

        return toMktDrawPrizeObject(newEntity);
    }

    //create marketing draw rule provide API
    @RequestMapping(value = "/drawRule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRuleObject createDrawRule(@RequestBody MktDrawRuleObject mktDrawRuleObject) {
        MktDrawRuleEntity entity = toMktDrawRuleEntity(mktDrawRuleObject);
        entity.setAvailableQuantity(entity.getTotalQuantity());
        MktDrawRuleEntity newEntity = mktDrawRuleRepository.save(entity);
        return toMktDrawRuleObject(newEntity);
    }

    //create marketing draw rule list provide API
    @RequestMapping(value = "/drawRule/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRuleObject createDrawRuleList(@RequestBody List<MktDrawRuleObject> mktDrawRuleObjectList) {

        if (mktDrawRuleObjectList == null || mktDrawRuleObjectList.size() == 0) {
            return null;
        }
        List<MktDrawRuleEntity> mktDrawRuleEntities = new ArrayList<>();
        for (MktDrawRuleObject object : mktDrawRuleObjectList) {
            MktDrawRuleEntity entity = toMktDrawRuleEntity(object);
            mktDrawRuleEntities.add(entity);
        }
        for (MktDrawRuleEntity mktDrawRuleentity : mktDrawRuleEntities) {
            mktDrawRuleentity.setId(null); //make sure it's insert
            mktDrawRuleentity.setCreatedDateTime(DateTime.now());
            mktDrawRuleentity.setAvailableQuantity(mktDrawRuleentity.getTotalQuantity());
        }

        List<MktDrawRuleEntity> newEntities = mktDrawRuleRepository.save(mktDrawRuleEntities);

        return toMktDrawRuleObject(newEntities.get(0));

    }

    @RequestMapping(value = "/drawRule/list", method = RequestMethod.PUT)
    public void updateMktDrawRuleList(@RequestBody List<MktDrawRuleObject> mktDrawRuleObjectList) {
        if (mktDrawRuleObjectList == null || mktDrawRuleObjectList.size() == 0) {
            return;
        }

        List<MktDrawRuleEntity> mktDrawRuleEntities = new ArrayList<>();
        for (MktDrawRuleObject object : mktDrawRuleObjectList) {

            MktDrawRuleEntity oldEntity = findMktDrawRuleById(object.getId());
            MktDrawRuleEntity entity = toMktDrawRuleEntity(object);
            entity.setCreatedDateTime(oldEntity.getCreatedDateTime());
            entity.setCreatedAccountId(oldEntity.getCreatedAccountId());
            mktDrawRuleEntities.add(entity);
        }

        mktDrawRuleRepository.save(mktDrawRuleEntities);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public List<String> getBatchIds(@PathVariable(value = "id") String id) {

        List<ProductKeyBatchEntity> productKeyBatchEntities = productKeyBatchRepository.findByMarketingId(id);

        List<String> batchIds = new ArrayList<>();
        if (productKeyBatchEntities != null) {
            for (ProductKeyBatchEntity pkEntity : productKeyBatchEntities) {
                batchIds.add(pkEntity.getBatchNo());
            }
        }
        return batchIds;
    }

    // query prized rule id list by user id or ys_id

    @RequestMapping(value = "/drawprize/rulelist", method = RequestMethod.GET)
    public List<String> getPrizedRuleIdListByUser(@RequestParam(value = "marketing_id") String marketingId,
                                                  @RequestParam(value = "user_id", required = false) String userId,
                                                  @RequestParam(value = "ys_id", required = false) String ysId) {

        if ((userId == null) && (ysId == null)) {
            throw new BadRequestException("one of the request parameter user_id or ys_id is required");
        }
        List<MktDrawRecordEntity> mktDrawRecordEntityList = new ArrayList<>();
        List<String> productKeyList = new ArrayList<>();
        List<String> ruleIdList = new ArrayList<>();
        if ((userId != null) && (!userId.equals(LookupCodes.SystemIds.ANONYMOUS_USER_ID))) {
            mktDrawRecordEntityList = mktDrawRecordRepository.findByMarketingIdAndUserIdAndIsPrized(marketingId, userId, true);
        } else if (ysId != null) {
            mktDrawRecordEntityList = mktDrawRecordRepository.findByMarketingIdAndYsidAndIsPrized(marketingId, ysId, true);
        }
        if (mktDrawRecordEntityList.size() > 0) {
            for (MktDrawRecordEntity mdrEntity : mktDrawRecordEntityList) {
                if (mdrEntity.getProductKey() != null) {
                    productKeyList.add(mdrEntity.getProductKey());
                }
            }
        }
        if (productKeyList.size() > 0) {
            for (String productKey : productKeyList) {
                List<MktDrawPrizeEntity> mktDrawPrizeEntities = mktDrawPrizeRepository.findByProductKey(productKey);
                if (mktDrawPrizeEntities.size() > 0) {
                    MktDrawPrizeEntity prizeEntity = mktDrawPrizeEntities.get(0);
                    if (prizeEntity.getDrawRuleId() != null) {
                        if (!ruleIdList.contains(prizeEntity.getDrawRuleId())) {
                            ruleIdList.add(prizeEntity.getDrawRuleId());
                        }
                    }
                }
            }
        }

        return ruleIdList;
    }


    //delete marketing plan
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarketing(@PathVariable(value = "id") String id) {
        MarketingEntity entity = marketingRepository.findOne(id);
        if (entity != null) {
            entity.setStatusCode(LookupCodes.MktStatus.DELETED);
            marketingRepository.save(entity);
        }
        List<ProductKeyBatchEntity> productKeyBatchEntities = productKeyBatchRepository.findByMarketingId(id);
        if (productKeyBatchEntities != null) {
            for (ProductKeyBatchEntity pkEntity : productKeyBatchEntities) {
                pkEntity.setMarketingId(null);
            }
            productKeyBatchRepository.save(productKeyBatchEntities);
        }
    }

    //delete marketing rule by marketing Id
    @RequestMapping(value = "/drawRule/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarketingRuleByMarketingId(@PathVariable(value = "id") String id) {
        if (id != null) {
            List<MktDrawRuleEntity> mktDrawRuleEntities = mktDrawRuleRepository.findByMarketingIdOrderById(id);
            if (mktDrawRuleEntities.size() > 0) {
                for (MktDrawRuleEntity entity : mktDrawRuleEntities) {
                    String mktDrawRuleId = entity.getId();
                    mktDrawRuleRepository.delete(mktDrawRuleId);
                }
            }
        }
    }

    //delete marketing rule by rule Id
    @RequestMapping(value = "/drawRule/rule/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarketingRuleById(@PathVariable(value = "id") String id) {
        MktDrawRuleEntity mktDrawRuleEntity = mktDrawRuleRepository.findOne(id);
        if (mktDrawRuleEntity != null) {
            mktDrawRuleRepository.delete(id);
        }
    }


    @RequestMapping(value = "/drawRule/{id}", method = RequestMethod.GET)
    public List<MktDrawRuleObject> findMarketingRulesById(@PathVariable(value = "id") String marketingId) {
        List<MktDrawRuleEntity> mktDrawRuleEntities = mktDrawRuleRepository.findByMarketingIdOrderById(marketingId);
        return mktDrawRuleEntities.stream().map(this::toMktDrawRuleObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/drawRule/consumer/{id}", method = RequestMethod.GET)
    public List<MktDrawRuleObject> findMarketingRulesByConsumerRightId(@PathVariable(value = "id") String consumerRightId) {
        List<MktDrawRuleEntity> mktDrawRuleEntities = mktDrawRuleRepository.findByConsumerRightId(consumerRightId);
        return mktDrawRuleEntities.stream().map(this::toMktDrawRuleObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/drawRule/key", method = RequestMethod.GET)
    public MktDrawRuleKeyObject getDrawRuleByKey(@RequestParam(value = "marketing_id") String marketingId,
                                                 @RequestParam(value = "product_key") String productKey) {

        if ((marketingId == null) || (productKey == null)) {
            throw new BadRequestException("Request parameter marketing_id or product_key is required");
        }
        List<MktDrawRuleKeyEntity> mktDrawRuleKeyEntityList = new ArrayList<>();

        mktDrawRuleKeyEntityList = mktDrawRuleKeyRepository.findByProductKeyAndMarketingId(productKey, marketingId);
        if (mktDrawRuleKeyEntityList.size() > 0) {
            MktDrawRuleKeyEntity mktDrawRuleKeyEntity = mktDrawRuleKeyEntityList.get(0);
            return toMktDrawRuleKeyObject(mktDrawRuleKeyEntity);
        } else {
            return null;
        }
    }



    @RequestMapping(value = "/drawPrize/record/{id}", method = RequestMethod.GET)
    public MktDrawPrizeObject findMktDrawPrizeById(@PathVariable(value = "id") String id) {
        MktDrawPrizeEntity entity = mktDrawPrizeRepository.findOne(id);
        if (entity != null) {
            return toMktDrawPrizeObject(entity);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/alipay_batchtransfer", method = RequestMethod.GET)
    public List<MktDrawPrizeObject> findAlipayMktDrawPrize() {
        List<MktDrawPrizeEntity> mktDrawPrizeEntities = mktDrawPrizeRepository.findByStatusCodeAndAccountType("submit", "alipay");
        return mktDrawPrizeEntities.stream().map(this::toMktDrawPrizeObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public int countMarketingByStatus(@RequestParam("org_ids") List<String> orgIds, @RequestParam("status") String status) {
        return marketingRepository.countByOrgIdInAndStatusCode(orgIds, status);
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public List<MarketingObject> getMarketingCostList(@RequestParam("org_ids") List<String> orgIds,
                                                      @RequestParam("status") List<String> status, Pageable pageable) {
        List<Object[]> result = marketingRepository.sumMarketingByOrgIds(orgIds, status, pageable);
        List<MarketingObject> marketingObjectList = new ArrayList<>();
        for (Object[] item : result) {
            MarketingObject object = new MarketingObject();
            object.setOrgId((String) item[0]);
            object.setBudget((Double) item[1]);
            marketingObjectList.add(object);
        }
        return marketingObjectList;
    }

    @RequestMapping(value = "/cost", method = RequestMethod.POST)
    public MktPrizeCostObject savePrizeCost(@RequestBody MktPrizeCostObject costObject){
        if(costObject != null) {
            MktPrizeCostEntity entity = new MktPrizeCostEntity();
            entity.setType(costObject.getType());
            entity.setName(costObject.getName());
            entity.setOrderId(costObject.getOrderId());
            entity.setMobile(costObject.getMobile());
            entity.setDrawRecordId(costObject.getDrawRecordId());
            entity.setCost(costObject.getCost());
            costRepository.save(entity);
            return  costObject;
        }

        return null;
    }


    private MarketingEntity findMarketingById(String id) {
        MarketingEntity entity = marketingRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("marketing not found by [id: " + id + ']');
        }
        return entity;
    }

    private MktDrawRuleEntity findMktDrawRuleById(String id) {
        MktDrawRuleEntity entity = mktDrawRuleRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("marketing not found by [id: " + id + ']');
        }
        return entity;
    }


    private MarketingObject toMarketingObject(MarketingEntity entity) {
        if (entity == null) {
            return null;
        }
        MarketingObject object = new MarketingObject();
        object.setId(entity.getId());
        object.setName(entity.getName());
        object.setWishes(entity.getWishes());
        object.setOrgId(entity.getOrgId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setTypeCode(entity.getTypeCode());
        object.setBudget(entity.getBudget());
        object.setBalance(entity.getBalance());
        object.setPrizeTypeCode(entity.getPrizeTypeCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        object.setStatusCode(entity.getStatusCode());
        object.setComments(entity.getComments());
        object.setQuantity(entity.getQuantity());
        object.setStartDateTime(entity.getStartDateTime());
        object.setEndDateTime(entity.getEndDateTime());
        object.setRulesText(entity.getRulesText());
        object.setIsPrizedAll(entity.getIsPrizedAll());
        object.setIsMobileVerified(entity.getIsMobileVerified());
        return object;
    }

    private MktDrawRuleObject toMktDrawRuleObject(MktDrawRuleEntity entity) {
        if (entity == null) {
            return null;
        }
        MktDrawRuleObject object = new MktDrawRuleObject();
        object.setId(entity.getId());
        object.setMarketingId(entity.getMarketingId());
        object.setConsumerRightId(entity.getConsumerRightId());
        object.setPrizeTypeCode(entity.getPrizeTypeCode());
        object.setAmount(entity.getAmount());
        object.setProbability(entity.getProbability());
        object.setComments(entity.getComments());
        object.setAppliedEnv(entity.getAppliedEnv());
        object.setWeight(entity.getWeight());
        object.setIsEqual(entity.getIsEqual());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        object.setTotalQuantity(entity.getTotalQuantity());
        object.setAvailableQuantity(entity.getAvailableQuantity());

        return object;
    }

    private MktDrawRecordObject toMktDrawRecordObject(MktDrawRecordEntity entity) {
        if (entity == null) {
            return null;
        }
        MktDrawRecordObject object = new MktDrawRecordObject();
        object.setId(entity.getId());
        object.setScanRecordId(entity.getScanRecordId());
        object.setMarketingId(entity.getMarketingId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setProductKey(entity.getProductKey());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setUserId(entity.getUserId());
        object.setIsPrized(entity.getIsPrized());
        object.setYsid(entity.getYsid());
        object.setOauthOpenid(entity.getOauthOpenid());
        return object;
    }

    private MktDrawPrizeObject toMktDrawPrizeObject(MktDrawPrizeEntity entity) {
        if (entity == null) {
            return null;
        }
        MktDrawPrizeObject object = new MktDrawPrizeObject();
        object.setDrawRecordId(entity.getDrawRecordId());
        object.setProductKey(entity.getProductKey());
        object.setScanRecordId(entity.getScanRecordId());
        object.setMarketingId(entity.getMarketingId());
        object.setDrawRuleId(entity.getDrawRuleId());
        object.setAmount(entity.getAmount());
        object.setMobile(entity.getMobile());
        object.setPrizeTypeCode(entity.getPrizeTypeCode());
        object.setStatusCode(entity.getStatusCode());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setPaidDateTime(entity.getPaidDateTime());
        object.setAccountType(entity.getAccountType());
        object.setPrizeAccount(entity.getPrizeAccount());
        object.setPrizeAccountName(entity.getPrizeAccountName());
        object.setPrizeContent(entity.getPrizeContent());
        object.setPrizeContactId(entity.getPrizeContactId());
        object.setComments(entity.getComments());
        return object;
    }


    private MktConsumerRightObject toMktConsumerRightObject(MktConsumerRightEntity entity) {
        if (entity == null) {
            return null;
        }
        MktConsumerRightObject object = new MktConsumerRightObject();
        object.setId(entity.getId());
        object.setName(entity.getName());
        object.setTypeCode(entity.getTypeCode());
        object.setAmount(entity.getAmount());
        object.setComments(entity.getComments());
        object.setOrgId(entity.getOrgId());
        object.setStatusCode(entity.getStatusCode());
        object.setCmccFlowId(entity.getCmccFlowId());
        object.setCuccFlowId(entity.getCuccFlowId());
        object.setCtccFlowId(entity.getCtccFlowId());
        object.setStoreUrl(entity.getStoreUrl());
        object.setImageName(entity.getImageName());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private MktPrizeContactObject toMktPrizeContactObject(MktPrizeContactEntity entity) {
        if (entity == null) {
            return null;
        }
        MktPrizeContactObject object = new MktPrizeContactObject();
        object.setId(entity.getId());
        object.setMktPrizeId(entity.getMktPrizeId());
        object.setName(entity.getName());
        object.setPhone(entity.getPhone());
        object.setProvince(entity.getProvince());
        object.setCity(entity.getCity());
        object.setDistrict(entity.getDistrict());
        object.setAddress(entity.getAddress());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private MktConsumerRightRedeemCodeObject toMktConsumerRightRedeemCodeObject(MktConsumerRightRedeemCodeEntity entity) {
        if (entity == null) {
            return null;
        }
        MktConsumerRightRedeemCodeObject object = new MktConsumerRightRedeemCodeObject();
        object.setId(entity.getId());
        object.setConsumerRightId(entity.getConsumerRightId());
        object.setOrgId(entity.getOrgId());
        object.setTypeCode(entity.getTypeCode());
        object.setStatusCode(entity.getStatusCode());
        object.setValue(entity.getValue());
        object.setDrawPrizeId(entity.getDrawPrizeId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private MktDrawRuleKeyObject toMktDrawRuleKeyObject(MktDrawRuleKeyEntity entity) {
        if (entity == null) {
            return null;
        }
        MktDrawRuleKeyObject object = new MktDrawRuleKeyObject();
        object.setId(entity.getId());
        object.setMarketingId(entity.getMarketingId());
        object.setProductKey(entity.getProductKey());
        object.setRuleId(entity.getRuleId());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }


    private MarketingEntity toMarketingEntity(MarketingObject object) {
        if (object == null) {
            return null;
        }
        MarketingEntity entity = new MarketingEntity();
        entity.setId(object.getId());
        entity.setName(object.getName());
        entity.setWishes(object.getWishes());
        entity.setOrgId(object.getOrgId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setTypeCode(object.getTypeCode());
        entity.setBudget(object.getBudget());
        entity.setBalance(object.getBalance());
        entity.setPrizeTypeCode(object.getPrizeTypeCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        entity.setStatusCode(object.getStatusCode());
        entity.setComments(object.getComments());
        entity.setQuantity(object.getQuantity());
        entity.setStartDateTime(object.getStartDateTime());
        entity.setEndDateTime(object.getEndDateTime());
        entity.setRulesText(object.getRulesText());
        entity.setIsPrizedAll(object.getIsPrizedAll());
        entity.setIsMobileVerified(object.getIsMobileVerified());
        return entity;
    }

    private MktDrawRuleEntity toMktDrawRuleEntity(MktDrawRuleObject object) {
        if (object == null) {
            return null;
        }
        MktDrawRuleEntity entity = new MktDrawRuleEntity();
        entity.setId(object.getId());
        entity.setMarketingId(object.getMarketingId());
        entity.setConsumerRightId(object.getConsumerRightId());
        entity.setPrizeTypeCode(object.getPrizeTypeCode());
        entity.setAmount(object.getAmount());
        entity.setProbability(object.getProbability());
        entity.setComments(object.getComments());
        entity.setAppliedEnv(object.getAppliedEnv());
        entity.setWeight(object.getWeight());
        entity.setIsEqual(object.getIsEqual());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        entity.setTotalQuantity(object.getTotalQuantity());
        entity.setAvailableQuantity(object.getAvailableQuantity());
        return entity;
    }


    private MktDrawRecordEntity toMktDrawRecordEntity(MktDrawRecordObject object) {
        if (object == null) {
            return null;
        }
        MktDrawRecordEntity entity = new MktDrawRecordEntity();
        entity.setId(object.getId());
        entity.setScanRecordId(object.getScanRecordId());
        entity.setMarketingId(object.getMarketingId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setProductKey(object.getProductKey());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setUserId(object.getUserId());
        entity.setIsPrized(object.getIsPrized());
        entity.setYsid(object.getYsid());
        entity.setOauthOpenid(object.getOauthOpenid());
        return entity;
    }

    private MktDrawPrizeEntity toMktDrawPrizeEntity(MktDrawPrizeObject object) {
        if (object == null) {
            return null;
        }
        MktDrawPrizeEntity entity = new MktDrawPrizeEntity();
        entity.setDrawRecordId(object.getDrawRecordId());
        entity.setProductKey(object.getProductKey());
        entity.setScanRecordId(object.getScanRecordId());
        entity.setMarketingId(object.getMarketingId());
        entity.setDrawRuleId(object.getDrawRuleId());
        entity.setAmount(object.getAmount());
        entity.setMobile(object.getMobile());
        entity.setPrizeTypeCode(object.getPrizeTypeCode());
        entity.setStatusCode(object.getStatusCode());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setPaidDateTime(object.getPaidDateTime());
        entity.setAccountType(object.getAccountType());
        entity.setPrizeAccount(object.getPrizeAccount());
        entity.setPrizeAccountName(object.getPrizeAccountName());
        entity.setPrizeContent(object.getPrizeContent());
        entity.setPrizeContactId(object.getPrizeContactId());
        entity.setComments(object.getComments());
        entity.setPrizeContactId(object.getPrizeContactId());
        return entity;
    }

    private MktConsumerRightEntity toMktConsumerRightEntity(MktConsumerRightObject object) {
        if (object == null) {
            return null;
        }
        MktConsumerRightEntity entity = new MktConsumerRightEntity();
        entity.setId(object.getId());
        entity.setName(object.getName());
        entity.setTypeCode(object.getTypeCode());
        entity.setAmount(object.getAmount());
        entity.setComments(object.getComments());
        entity.setOrgId(object.getOrgId());
        entity.setStatusCode(object.getStatusCode());
        entity.setCmccFlowId(object.getCmccFlowId());
        entity.setCuccFlowId(object.getCuccFlowId());
        entity.setCtccFlowId(object.getCtccFlowId());
        entity.setStoreUrl(object.getStoreUrl());
        entity.setImageName(object.getImageName());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }

    private MktPrizeContactEntity toMktPrizeContactEntity(MktPrizeContactObject object) {
        if (object == null) {
            return null;
        }
        MktPrizeContactEntity entity = new MktPrizeContactEntity();
        entity.setId(object.getId());
        entity.setMktPrizeId(object.getMktPrizeId());
        entity.setName(object.getName());
        entity.setPhone(object.getPhone());
        entity.setProvince(object.getProvince());
        entity.setCity(object.getCity());
        entity.setDistrict(object.getDistrict());
        entity.setAddress(object.getAddress());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }

    private MktDrawPrizeReportObject toMktDrawPrizeReportObject(MktDrawPrizeReportEntity entity) {
        if (entity == null) {
            return null;
        }
        MktDrawPrizeReportObject object = new MktDrawPrizeReportObject();
        object.setProductKey(entity.getProductKey());
        object.setAmount(entity.getAmount());
        object.setMobile(entity.getMobile());
        object.setStatusCode(entity.getStatusCode());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setAccountType(entity.getAccountType());
        object.setPrizeAccount(entity.getPrizeAccount());
        object.setPrizeAccountName(entity.getPrizeAccountName());
        object.setProductBaseName(entity.getProductBaseName());
        object.setIp(entity.getIp());
        object.setCity(entity.getCity());
        object.setRuleName(entity.getRuleName());
        object.setGravatarUrl(entity.getGravatarUrl());
        object.setOauthOpenid(entity.getOauthOpenid());
        return object;
    }

    private MktSellerObject toMktSellerObject(MktSellerEntity entity) {
        if (entity == null) {
            return null;
        }
        MktSellerObject object = new MktSellerObject();
        object.setOpenid(entity.getOpenid());
        object.setOrgId(entity.getOrgId());
        object.setName(entity.getName());
        object.setSex(entity.getSex());
        object.setCity(entity.getCity());
        object.setProvince(entity.getProvince());
        object.setCountry(entity.getCountry());
        object.setGravatarUrl(entity.getGravatarUrl());
        object.setShopUrl(entity.getShopUrl());
        return object;
    }



}
