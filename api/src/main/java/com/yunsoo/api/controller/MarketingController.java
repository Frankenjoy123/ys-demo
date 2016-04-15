package com.yunsoo.api.controller;

import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.Marketing;
import com.yunsoo.api.dto.MktDrawPrize;
import com.yunsoo.api.dto.MktDrawRule;
import com.yunsoo.api.dto.ScanRecord;
import com.yunsoo.api.payment.ParameterNames;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/marketing")
public class MarketingController {

    @Autowired
    private MarketingDomain marketingDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private ProductKeyDomain productKeyDomain;

    @Autowired
    private UserScanDomain userScanDomain;

    @Autowired
    private OrganizationDomain organizationDomain;


    @RequestMapping(value = "drawRule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRule createMktDrawRule(@RequestBody MktDrawRule mktDrawRule) {
        if (mktDrawRule == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        String marketingId = mktDrawRule.getMarketingId();
        MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
        if (marketingObject == null) {
            throw new NotFoundException("marketing can not be found by the id");
        }
        MktDrawRuleObject mktDrawRuleObject = mktDrawRule.toMktDrawRuleObject();
        String currentUserId = AuthUtils.getCurrentAccount().getId();
        mktDrawRuleObject.setCreatedAccountId(currentUserId);
        mktDrawRuleObject.setCreatedDateTime(DateTime.now());

        MktDrawRuleObject newMktDrawRuleObject = marketingDomain.createMktDrawRule(mktDrawRuleObject);
        return new MktDrawRule(newMktDrawRuleObject);
    }



    //query marketing plan by org id
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Marketing> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                       @RequestParam(value = "carrier_id", required = false) String carrierId,
                                       @RequestParam(value = "status", required = false) String status,
                                       Pageable pageable,
                                       HttpServletResponse response) {

        List<String> orgIds = null;
        if(carrierId != null)
            orgIds = organizationDomain.getBrandIdsForCarrier(carrierId);
        else
            orgId = AuthUtils.fixOrgId(orgId);

        Page<MarketingObject> marketingPage = marketingDomain.getMarketingList(orgId, orgIds, status, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", marketingPage.toContentRange());
        }

        List<Marketing> marketingList = new ArrayList<>();
        Map<String, String> orgList = new HashMap<>();
        marketingPage.getContent().forEach(object -> {
            Marketing marketing = new Marketing(object);
            ProductBaseObject pbo = productBaseDomain.getProductBaseById(object.getProductBaseId());
            if (pbo != null) {
                marketing.setProductBaseName(pbo.getName());
            }
            if(carrierId != null){
                if(orgList.containsKey(marketing.getOrgId()))
                    marketing.setOrgName(orgList.get(marketing.getOrgId()));
                else {
                    OrganizationObject org = organizationDomain.getOrganizationById(marketing.getOrgId());
                    if (org != null) {
                        orgList.put(org.getId(), org.getName());
                        marketing.setOrgName(org.getName());
                    }
                }
            }
            List<MktDrawRuleObject> mktDrawRuleObjectList = marketingDomain.getRuleList(object.getId());
            if (mktDrawRuleObjectList != null) {
                List<MktDrawRule> mktDrawRuleList = mktDrawRuleObjectList.stream().map(MktDrawRule::new).collect(Collectors.toList());
                marketing.setMarketingRules(mktDrawRuleList);
            }
            marketingList.add(marketing);
        });

        return marketingList;
    }

    //create marketing plan
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Marketing createMarketing(@RequestParam(value = "batchId", required = false) String batchId,
                                     @RequestBody Marketing marketing) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        MarketingObject marketingObject = marketing.toMarketingObject();
        marketingObject.setCreatedAccountId(currentAccountId);
        marketingObject.setCreatedDateTime(DateTime.now());
        if (StringUtils.hasText(marketing.getOrgId()))
            marketingObject.setOrgId(marketing.getOrgId());
        else
            marketingObject.setOrgId(AuthUtils.getCurrentAccount().getOrgId());

        MarketingObject mktObject;
        if (batchId != null) {
            ProductKeyBatchObject batchObject = productKeyDomain.getPkBatchById(batchId);
            if (batchObject != null) {
                marketingObject.setOrgId(batchObject.getOrgId());
                marketingObject.setProductBaseId(batchObject.getProductBaseId());
                mktObject = marketingDomain.createMarketing(marketingObject);

                batchObject.setMarketingId(mktObject.getId());

                productKeyDomain.patchUpdateProductKeyBatch(batchObject);

            } else {
                mktObject = marketingDomain.createMarketing(marketingObject);
            }
        } else {
            mktObject = marketingDomain.createMarketing(marketingObject);
        }
        return new Marketing(mktObject);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public void updateMarketing(@PathVariable(value = "id") String id, @RequestBody Marketing marketing) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        MarketingObject marketingObject = marketingDomain.getMarketingById(id);
        if (marketingObject != null) {
            marketingObject.setName(marketing.getName());
            marketingObject.setWishes(marketing.getWishes());
            marketingObject.setBudget(marketing.getBudget());
            marketingObject.setModifiedDateTime(DateTime.now());
            marketingObject.setModifiedAccountId(currentAccountId);
            marketingDomain.updateMarketing(marketingObject);
        }
    }

    @RequestMapping(value = "/totalcount", method = RequestMethod.GET)
    public Long countByOrgId(@RequestParam(value = "org_id", required = false) String orgId) {
        orgId = AuthUtils.fixOrgId(orgId);
        if (orgId == null)
            throw new BadRequestException("org id can not be null");

        return marketingDomain.countMarketingsByOrgId(orgId);
    }

    @RequestMapping(value = "/drawPrize/totalcount", method = RequestMethod.GET)
    public Long countMktDrawPrizesByOrgId(@RequestParam(value = "org_id", required = false) String orgId) {
        orgId = AuthUtils.fixOrgId(orgId);
        if (orgId == null)
            throw new BadRequestException("org id can not be null");

        return marketingDomain.countMktDrawPrizesByOrgId(orgId);


    }



    @RequestMapping(value = "keys/sum/{id}", method = RequestMethod.GET)
    public Long countKeysByMarketingId(@PathVariable(value = "id") String marketingId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        return marketingDomain.countProductKeysByMarketingId(marketingId);
    }

    @RequestMapping(value = "drawRecords/sum/{id}", method = RequestMethod.GET)
    public Long countDrawRecordsByMarketingId(@PathVariable(value = "id") String marketingId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        return marketingDomain.countDrawRecordsByMarketingId(marketingId);
    }

    @RequestMapping(value = "drawPrize/sum/{id}", method = RequestMethod.GET)
    public Long countDrawPrizeByDrawRuleId(@PathVariable(value = "id") String drawRuleId) {
        if (drawRuleId == null)
            throw new BadRequestException("draw rule id can not be null");

        return marketingDomain.countDrawPrizeByDrawRuleId(drawRuleId);
    }


    @RequestMapping(value = "{id}/active", method = RequestMethod.PUT)
    public void enableMarketing(@PathVariable(value = "id")String id, @RequestParam(value="comments", required = false)String comments){
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        MarketingObject marketingObject = marketingDomain.getMarketingById(id);
        if(marketingObject != null) {
            marketingObject.setStatusCode(LookupCodes.MktStatus.PAID);
            marketingObject.setModifiedDateTime(DateTime.now());
            marketingObject.setModifiedAccountId(currentAccountId);
            if(StringUtils.hasText(comments))
                marketingObject.setComments(marketingObject.getComments() == null ? "": marketingObject.getComments() + comments + ";");
            marketingDomain.updateMarketing(marketingObject);
        }
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.PUT)
    public void disableMarketing(@PathVariable(value = "id")String id, @RequestParam(value="comments", required = false)String comments){
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        MarketingObject marketingObject = marketingDomain.getMarketingById(id);
        if(marketingObject != null) {
            marketingObject.setStatusCode(LookupCodes.MktStatus.DISABLED);
            marketingObject.setModifiedDateTime(DateTime.now());
            marketingObject.setModifiedAccountId(currentAccountId);
            if(StringUtils.hasText(comments))
                marketingObject.setComments(marketingObject.getComments() == null ? "": marketingObject.getComments() + comments + ";");
            marketingDomain.updateMarketing(marketingObject);
        }
    }

    @RequestMapping(value = "drawPrize/marketing", method = RequestMethod.GET)
    public List<MktDrawPrize> getMktDrawPrizeByFilter(@RequestParam(value = "marketing_id") String marketingId,
                                                      @RequestParam(value = "account_type", required = false) String accountType,
                                                      @RequestParam(value = "status_code", required = false) String statusCode,
                                                      @RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                      @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                      @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                                      Pageable pageable,
                                                      HttpServletResponse response) {

        Page<MktDrawPrizeObject> mktDrawPrizePage = marketingDomain.getMktDrawPrizeByFilter(marketingId, accountType, statusCode, startTime, endTime, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", mktDrawPrizePage.toContentRange());
        }

        List<MktDrawPrize> mktDrawPrizeList = new ArrayList<>();
        mktDrawPrizePage.getContent().forEach(object -> {
            MktDrawPrize mktDrawPrize = new MktDrawPrize(object);
            MarketingObject mkto = marketingDomain.getMarketingById(object.getMarketingId());
            if (mkto != null) {
                String productBaseId = mkto.getProductBaseId();
                ProductBaseObject pbo = productBaseDomain.getProductBaseById(productBaseId);
                mktDrawPrize.setProductBaseName(pbo.getName());
            }
            String scanRecordId = object.getScanRecordId();
            UserScanRecordObject userScanRecordObject = userScanDomain.getScanRecordById(scanRecordId);
            if (userScanRecordObject != null) {
                mktDrawPrize.setScanRecord(new ScanRecord(userScanRecordObject));
            }
            MktDrawRuleObject mktDrawRuleObject = marketingDomain.getMktDrawRuleById(object.getDrawRuleId());
            if (mktDrawRuleObject != null) {
                mktDrawPrize.setMktDrawRule(new MktDrawRule(mktDrawRuleObject));
            }
            mktDrawPrizeList.add(mktDrawPrize);
        });

        return mktDrawPrizeList;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Marketing getMarketing(@PathVariable(value = "id") String marketingId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        Marketing marketing = new Marketing(marketingDomain.getMarketingById(marketingId));
        List<MktDrawRuleObject> mktDrawRuleObjectList = marketingDomain.getRuleList(marketingId);

        if (mktDrawRuleObjectList != null) {
            List<MktDrawRule> mktDrawRuleList = mktDrawRuleObjectList.stream().map(MktDrawRule::new).collect(Collectors.toList());
            marketing.setMarketingRules(mktDrawRuleList);
        }
        return marketing;
    }


    //delete marketing plan by id
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarketing(@PathVariable(value = "id") String id) {
        MarketingObject marketingObject = marketingDomain.getMarketingById(id);
        if (marketingObject != null) {
            marketingDomain.deleteMarketingById(id);
        }
    }

    @RequestMapping(value = "drawRule/{id}", method = RequestMethod.GET)
    public List<MktDrawRule> getMarketingRuleList(@PathVariable(value = "id") String marketingId) {
        if (marketingId == null)
            throw new BadRequestException("marketing id can not be null");

        return marketingDomain.getRuleList(marketingId).stream().map(MktDrawRule::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "Rule/{id}", method = RequestMethod.GET)
    public MktDrawRule getMarketingRuleById(@PathVariable(value = "id") String id) {
        if (id == null)
            throw new BadRequestException("rule id can not be null");

        MktDrawRuleObject mktDrawRuleObject = marketingDomain.getMktDrawRuleById(id);
        return new MktDrawRule(mktDrawRuleObject);
    }


    @RequestMapping(value = "/drawRule/{id}", method = RequestMethod.PUT)
    public void updateMktDrawRule(@PathVariable(value = "id") String id, @RequestBody MktDrawRule mktDrawRule) {
        if (mktDrawRule == null) {
            throw new BadRequestException("marketing draw rule can not be null");
        }

        MktDrawRuleObject mktDrawRuleObject = marketingDomain.getMktDrawRuleById(id);
        if (mktDrawRuleObject == null) {
            throw new NotFoundException("marketing draw rule can not be found");
        }

        String currentUserId = AuthUtils.getCurrentAccount().getId();
        mktDrawRuleObject.setComments(mktDrawRule.getComments());
        mktDrawRuleObject.setAmount(mktDrawRule.getAmount());
        mktDrawRuleObject.setProbability(mktDrawRule.getProbability());
        mktDrawRuleObject.setModifiedAccountId(currentUserId);
        mktDrawRuleObject.setModifiedDateTime(DateTime.now());

        marketingDomain.updateMktDrawRule(mktDrawRuleObject);
    }


    @RequestMapping(value = "drawPrize", method = RequestMethod.PUT)
    public void updateMktDrawPrize(@RequestBody MktDrawPrize mktDrawPrize) {
        if (mktDrawPrize == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = mktDrawPrize.toMktDrawPrizeObject();
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);

    }

    @RequestMapping(value = "drawPrize/disable/{id}", method = RequestMethod.GET)
    public void disableMktDrawPrize(@PathVariable(value = "id") String id) {
        if (id == null) {
            throw new BadRequestException("marketing draw record id can not be null");
        }
        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeById(id);
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.INVALID);
        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
    }
    @RequestMapping(value = "/count/unpaid")
    public int countUnpaidMarketing(@RequestParam("org_ids")List<String> orgIds){
        if(orgIds == null || orgIds.size() == 0)
            throw new BadRequestException("org ids can not be null");

        return marketingDomain.countMarketing(orgIds, LookupCodes.MktStatus.CREATED);
    }

    @RequestMapping(value = "statistics")
    public List<Marketing> marketingStatistics(@RequestParam("org_ids")List<String> orgIds, Pageable pageable){
        List<MarketingObject> statisticsObjectList = marketingDomain.statisticsMarketing(orgIds, Arrays.asList(LookupCodes.MktStatus.PAID, LookupCodes.MktStatus.AVAILABLE), pageable);
        return statisticsObjectList.stream().map(Marketing::new).collect(Collectors.toList());
    }

    //alipay batch transfer
    @RequestMapping(value = "alipay_batchtransfer", method = RequestMethod.GET)
    public Map<String, String> batchTransfer() {
        Map<String, String> parametersMap = marketingDomain.getAlipayBatchTansferParameters();
        List<String> keys = new ArrayList<>(parametersMap.keySet());
        keys.sort(null);
        return parametersMap;

    }

    @RequestMapping(value = "alipay/notify", method = RequestMethod.POST)
    public void alipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> parametersMap = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();

        String notifyType = request.getParameter(ParameterNames.NOTIFY_TYPE);
        List<String> drawRecordIds = new ArrayList<String>();
        List<String> drawRecordFailedIds = new ArrayList<String>();
        String batchNo = request.getParameter("batch_no");

        if (notifyType.equals("batch_trans_notify")) {
            String successDetails = request.getParameter("success_details");
            String failDetails = request.getParameter("fail_details");

            if (successDetails != null) {
                String[] orderPaymentDetails = successDetails.split("\\|");
                for (int i = 0; i < orderPaymentDetails.length; i++) {
                    String[] orderPaymentDetail = orderPaymentDetails[i].split("\\^");
                    drawRecordIds.add(orderPaymentDetail[0]);
                }
                if (drawRecordIds.size() != 0) {
                    for (String id : drawRecordIds) {
                        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeById(id);
                        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.PAID);
                        mktDrawPrizeObject.setPaidDateTime(DateTime.now());
                        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
                    }
                }
            }
            if (failDetails != null) {
                String[] orderPaymentFailedDetails = failDetails.split("\\|");
                for (int i = 0; i < orderPaymentFailedDetails.length; i++) {
                    String[] orderPaymentFailedDetail = orderPaymentFailedDetails[i].split("\\^");
                    drawRecordFailedIds.add(orderPaymentFailedDetail[0]);
                }
                if (drawRecordFailedIds.size() != 0) {
                    for (String id : drawRecordFailedIds) {
                        MktDrawPrizeObject mktDrawPrizeObject = marketingDomain.getMktDrawPrizeById(id);
                        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.INVALID);
                        marketingDomain.updateMktDrawPrize(mktDrawPrizeObject);
                    }
                }

            }
            response.getWriter().print("success");

        }

    }


}
