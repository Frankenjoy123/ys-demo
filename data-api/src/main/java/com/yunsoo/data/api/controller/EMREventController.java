package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.EMREventObject;
import com.yunsoo.common.data.object.PeriodUserConsumptionStatsObject;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.EMREventEntity;
import com.yunsoo.data.service.repository.EMREventRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emr/event")
public class EMREventController {

    @Autowired
    private EMREventRepository emrEventRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<EMREventObject> findByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                             @RequestParam(value = "user_id", required = false) String userId,
                                             @RequestParam(value = "ys_id", required = false) String ysId,
                                             @RequestParam(value = "event_datetime_start", required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate eventDateTimeStart,
                                             @RequestParam(value = "event_datetime_end", required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate eventDateTimeEnd,
                                             Pageable pageable,
                                             HttpServletResponse response) {

        DateTime eventDateTimeStartTo = null;
        DateTime eventDateTimeEndTo = null;

        if (eventDateTimeStart != null && !StringUtils.isEmpty(eventDateTimeStart.toString()))
            eventDateTimeStartTo = eventDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (eventDateTimeEnd != null && !StringUtils.isEmpty(eventDateTimeEnd.toString()))
            eventDateTimeEndTo = eventDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        Page<EMREventEntity> entityPage = emrEventRepository.findByFilter(orgId, userId, ysId, eventDateTimeStartTo, eventDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMREventObject)
                .collect(Collectors.toList());
    }

    /*
    * filter by share
    * @param orgId
    * @param productBaseId
    * @param province
    * @param city
    * @param city
    * @param createdDateTimeStart
    * @param createdDateTimeEnd
    */
    @RequestMapping(value = "share", method = RequestMethod.GET)
    public List<EMREventObject> findEventFilterByShare(@RequestParam(value = "org_id") String orgId,
                                                       @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                       @RequestParam(value = "province", required = false) String province,
                                                       @RequestParam(value = "city", required = false) String city,
                                                       @RequestParam(value = "create_datetime_start", required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                       @RequestParam(value = "create_datetime_end", required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                       Pageable pageable,
                                                       HttpServletResponse response) {

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            startDateTime = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            endDateTime = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMREventEntity> entityPage = emrEventRepository.findEventFilterByShare(orgId, productBaseId, province, city, startDateTime, endDateTime, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMREventObject)
                .collect(Collectors.toList());
    }

    /*
    * filter by store_url
    * @param orgId
    * @param productBaseId
    * @param province
    * @param city
    * @param city
    * @param createdDateTimeStart
    * @param createdDateTimeEnd
    */
    @RequestMapping(value = "store_url", method = RequestMethod.GET)
    public List<EMREventObject> findEventFilterByStoreUrl(@RequestParam(value = "org_id") String orgId,
                                                          @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                          @RequestParam(value = "province", required = false) String province,
                                                          @RequestParam(value = "city", required = false) String city,
                                                          @RequestParam(value = "create_datetime_start", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                          @RequestParam(value = "create_datetime_end", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                          Pageable pageable,
                                                          HttpServletResponse response) {

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            startDateTime = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            endDateTime = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMREventEntity> entityPage = emrEventRepository.findEventFilterByStoreUrl(orgId, productBaseId, province, city, startDateTime, endDateTime, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMREventObject)
                .collect(Collectors.toList());
    }

    /*
    * filter by comment
    * @param orgId
    * @param productBaseId
    * @param province
    * @param city
    * @param city
    * @param createdDateTimeStart
    * @param createdDateTimeEnd
    */
    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public List<EMREventObject> findEventFilterByComment(@RequestParam(value = "org_id") String orgId,
                                                         @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                         @RequestParam(value = "province", required = false) String province,
                                                         @RequestParam(value = "city", required = false) String city,
                                                         @RequestParam(value = "create_datetime_start", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                         @RequestParam(value = "create_datetime_end", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                         Pageable pageable,
                                                         HttpServletResponse response) {

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            startDateTime = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            endDateTime = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMREventEntity> entityPage = emrEventRepository.findEventFilterByComment(orgId, productBaseId, province, city, startDateTime, endDateTime, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMREventObject)
                .collect(Collectors.toList());
    }


    /**
     * 最近一次消费
     *
     * @param orgId
     * @param userId
     * @param ysId
     * @param response
     * @return
     */
    @RequestMapping(value = "latest_consumption", method = RequestMethod.GET)
    public EMREventObject findLatestConsumption(@RequestParam(value = "org_id", required = true) String orgId,
                                                @RequestParam(value = "user_id", required = false) String userId,
                                                @RequestParam(value = "ys_id", required = false) String ysId,
                                                HttpServletResponse response) {
        EMREventEntity entity = emrEventRepository.recentlyConsumptionEvent(orgId, userId, ysId);
        return toEMREventObject(entity);
    }

    @RequestMapping(value = "period_consumption_stats", method = RequestMethod.GET)
    public PeriodUserConsumptionStatsObject findLatestConsumption(@RequestParam(value = "org_id", required = true) String orgId,
                                                                  @RequestParam(value = "user_id", required = false) String userId,
                                                                  @RequestParam(value = "ys_id", required = false) String ysId,
                                                                  @RequestParam(value = "period", required = false) Integer days,
                                                                  HttpServletResponse response) {
        if (days == null) days = 60;

        DateTime eventDateTimeEndTo = DateTime.now();
        DateTime eventDateTimeStartTo = eventDateTimeEndTo.minusDays(60);

        int totalCount = emrEventRepository.periodConsumptionCount(orgId, userId, ysId, eventDateTimeStartTo, eventDateTimeEndTo);
        PeriodUserConsumptionStatsObject object = new PeriodUserConsumptionStatsObject();
        object.setTotalCount(totalCount);
        object.setDailyCount((double) totalCount / days);
        object.setWeeklyCount((double) totalCount * 7.0 / days);
        object.setMonthlyCount((double) totalCount * 30.0 / days);

        return object;
    }


    private EMREventObject toEMREventObject(EMREventEntity entity) {
        if (entity == null) {
            return null;
        }

        EMREventObject object = new EMREventObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setYsId(entity.getYsId());
        object.setOrgId(entity.getOrgId());
        object.setName(entity.getName());
        object.setIp(entity.getIp());
        object.setCity(entity.getCity());
        object.setProvince(entity.getProvince());
        object.setProductBaseId(entity.getProductBaseId());
        object.setProductName(entity.getProductName());
        object.setProductKey(entity.getProductKey());
        object.setKeyBatchId(entity.getKeyBatchId());
        object.setPriceStatusCode(entity.getPriceStatusCode());
        object.setIsPriced(entity.getIsPriced());
        object.setScanDateTime(entity.getScanDateTime());
        object.setEventDateTime(entity.getEventDateTime());
        object.setWxOpenId(entity.getWxOpenId());
        object.setMarketingId(entity.getMarketingId());
        object.setValue(entity.getValue());
        return object;
    }

}
