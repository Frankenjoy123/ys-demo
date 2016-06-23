package com.yunsoo.api.controller;

import com.yunsoo.api.domain.EMREventDomain;
import com.yunsoo.api.domain.EMRUserDomain;
import com.yunsoo.api.domain.EMRUserProductEventStasticsDomain;
import com.yunsoo.api.domain.UserBlockDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emr/user")
public class EMRUserController {

    @Autowired
    private EMRUserDomain emrUserDomain;

    @Autowired
    private UserBlockDomain userBlockDomain;

    @Autowired
    private EMREventDomain emrEventDomain;

    @Autowired
    private EMRUserProductEventStasticsDomain emrUserProductEventStasticsDomain;

    @RequestMapping(value = "id", method = RequestMethod.GET)
    public EMRUser getUser(@RequestParam(value = "org_id", required = false) String orgId,
                           @RequestParam(value = "user_id", required = false) String userId,
                           @RequestParam(value = "ys_id", required = false) String ysId,
                           @RequestParam(value = "create_datetime_start", required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                           @RequestParam(value = "create_datetime_end", required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd) {

        orgId = AuthUtils.fixOrgId(orgId);
        EMRUserObject emrUserObject = emrUserDomain.getEMRUser(orgId, userId, ysId);

        EMRUser emrUser = new EMRUser(emrUserObject);

        List<EMRUserProductEventStasticsObject> emrUserProductEventStasticsObjects = emrUserProductEventStasticsDomain.getEMRUserProductEventStasticsObjects(orgId, userId
                , ysId, createdDateTimeStart, createdDateTimeEnd);

        List<EMRUserProductEventStastics> emrUserProductEventStasticses = emrUserProductEventStasticsObjects.stream().map(this::toEMRUserProductEventStastics)
                .collect(Collectors.toList());

        emrUser.setEmrUserProductEventStasticses(emrUserProductEventStasticses);

        EMREventObject emrEventObject = emrEventDomain.getLatestEMREvent(orgId, userId, ysId);
        emrUser.setEmrEvent(new EMREvent(emrEventObject));

        PeriodUserConsumptionStatsObject periodUserConsumptionStatsObject = emrEventDomain.getPeriodUserConsumptionStatsObject(orgId, userId, ysId);
        emrUser.setPeriodUserConsumptionStats(new PeriodUserConsumptionStats(periodUserConsumptionStatsObject));

        return emrUser;
    }

    private EMRUserProductEventStastics toEMRUserProductEventStastics(EMRUserProductEventStasticsObject object) {
        if (object == null) return null;

        EMRUserProductEventStastics stastics = new EMRUserProductEventStastics();
        stastics.setDrawCount(object.getDrawCount());
        stastics.setOrgId(object.getOrgId());
        stastics.setProductBaseId(object.getProductBaseId());
        stastics.setProductName(object.getProductName());
        stastics.setRewardCount(object.getRewardCount());
        stastics.setScanCount(object.getScanCount());
        stastics.setWinCount(object.getWinCount());

        return stastics;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<EMRUser> queryUser(@RequestParam(value = "org_id", required = false) String orgId,
                                   @RequestParam(value = "sex", required = false) Boolean sex,
                                   @RequestParam(value = "wx_user", required = false) Boolean wxUser,
                                   @RequestParam(value = "phone", required = false) String phone,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "province", required = false) String province,
                                   @RequestParam(value = "city", required = false) String city,
                                   @RequestParam(value = "user_tags", required = false) String userTags,
                                   @RequestParam(value = "age_start", required = false) Integer ageStart,
                                   @RequestParam(value = "age_end", required = false) Integer ageEnd,
                                   @RequestParam(value = "create_datetime_start", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                   @RequestParam(value = "create_datetime_end", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                   @SortDefault(value = "joinDateTime", direction = Sort.Direction.DESC)
                                   Pageable pageable,
                                   HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMRUserObject> entityPage = emrUserDomain.getEMRUserList(orgId, sex, phone, name, province, city, ageStart, ageEnd, createdDateTimeStart, createdDateTimeEnd, userTags, wxUser, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        List<EMRUser> emrUsers = entityPage.map(emr -> {
            EMRUser user = new EMRUser(emr);

            List<UserBlockObject> userBlockObjects = userBlockDomain.getUserBlockList(user.getUserId(), user.getYsId(), user.getOrgId());
            if (userBlockObjects == null || userBlockObjects.size() == 0) {
                user.setUserBlockId("");
            } else {
                user.setUserBlockId(userBlockObjects.get(0).getId());
            }

            return user;
        }).getContent();

        return emrUsers;
    }

    @RequestMapping(value = "scan", method = RequestMethod.GET)
    public List<EMRUser> queryScanUser(@RequestParam(value = "org_id", required = false) String orgId,
                                       @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                       @RequestParam(value = "province", required = false) String province,
                                       @RequestParam(value = "city", required = false) String city,
                                       @RequestParam(value = "create_datetime_start", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                       @RequestParam(value = "create_datetime_end", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                       Pageable pageable,
                                       HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMRUserObject> entityPage = emrUserDomain.getEMRScanUserList(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMRUser::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "draw", method = RequestMethod.GET)
    public List<EMRUser> queryDrawUser(@RequestParam(value = "org_id", required = false) String orgId,
                                       @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                       @RequestParam(value = "province", required = false) String province,
                                       @RequestParam(value = "city", required = false) String city,
                                       @RequestParam(value = "create_datetime_start", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                       @RequestParam(value = "create_datetime_end", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                       Pageable pageable,
                                       HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMRUserObject> entityPage = emrUserDomain.getEMRDrawUserList(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMRUser::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "wx", method = RequestMethod.GET)
    public List<EMRUser> queryWXUser(@RequestParam(value = "org_id", required = false) String orgId,
                                     @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                     @RequestParam(value = "province", required = false) String province,
                                     @RequestParam(value = "city", required = false) String city,
                                     @RequestParam(value = "create_datetime_start", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                     @RequestParam(value = "create_datetime_end", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                     Pageable pageable,
                                     HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMRUserObject> entityPage = emrUserDomain.getEMRWXUserList(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMRUser::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "win", method = RequestMethod.GET)
    public List<EMRUser> queryWinUser(@RequestParam(value = "org_id", required = false) String orgId,
                                      @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                      @RequestParam(value = "province", required = false) String province,
                                      @RequestParam(value = "city", required = false) String city,
                                      @RequestParam(value = "create_datetime_start", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                      @RequestParam(value = "create_datetime_end", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                      Pageable pageable,
                                      HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMRUserObject> entityPage = emrUserDomain.getEMRWinUserList(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMRUser::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "reward", method = RequestMethod.GET)
    public List<EMRUser> queryRewardUser(@RequestParam(value = "org_id", required = false) String orgId,
                                         @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                         @RequestParam(value = "province", required = false) String province,
                                         @RequestParam(value = "city", required = false) String city,
                                         @RequestParam(value = "create_datetime_start", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                         @RequestParam(value = "create_datetime_end", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                         Pageable pageable,
                                         HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMRUserObject> entityPage = emrUserDomain.getEMRRewardUserList(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMRUser::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "funnel", method = RequestMethod.GET)
    public EMRUserReport queryUserFunnel(@RequestParam(value = "org_id", required = false) String orgId,
                                         @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                         @RequestParam(value = "province", required = false) String province,
                                         @RequestParam(value = "city", required = false) String city,
                                         @RequestParam(value = "create_datetime_start", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                         @RequestParam(value = "create_datetime_end", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd) {

        orgId = AuthUtils.fixOrgId(orgId);
        EMRUserReportObject userReportObject = emrUserDomain.getEMRUserFunnelCount(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);

        return new EMRUserReport(userReportObject);
    }
}
