package com.yunsoo.api.controller;

import com.yunsoo.api.domain.EMREventDomain;
import com.yunsoo.api.dto.EMREvent;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.EMREventObject;
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

/**
 * Created by Admin on 6/13/2016.
 */
@RestController
@RequestMapping("/emr/event")
public class EMREventController {

    @Autowired
    private EMREventDomain emrEventDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<EMREvent> queryEvent(@RequestParam(value = "org_id", required = false) String orgId,
                                     @RequestParam(value = "user_id", required = false) String userId,
                                     @RequestParam(value = "ys_id", required = false) String ysId,
                                     @RequestParam(value = "event_datetime_start", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate eventDateTimeStart,
                                     @RequestParam(value = "event_datetime_end", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate eventDateTimeEnd,
                                     @SortDefault(value = "eventDateTime", direction = Sort.Direction.DESC)
                                     Pageable pageable,
                                     HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMREventObject> entityPage = emrEventDomain.getEMREventList(orgId, userId, ysId, eventDateTimeStart, eventDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMREvent::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "share", method = RequestMethod.GET)
    public List<EMREvent> queryShareEvent(@RequestParam(value = "org_id", required = false) String orgId,
                                          @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                          @RequestParam(value = "province", required = false) String province,
                                          @RequestParam(value = "city", required = false) String city,
                                          @RequestParam(value = "create_datetime_start", required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                          @RequestParam(value = "create_datetime_end", required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                          @SortDefault(value = "eventDateTime", direction = Sort.Direction.DESC)
                                          Pageable pageable,
                                          HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMREventObject> entityPage = emrEventDomain.getEMREventFilterByShare(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMREvent::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "store_url", method = RequestMethod.GET)
    public List<EMREvent> queryStoreUrlEvent(@RequestParam(value = "org_id", required = false) String orgId,
                                             @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                             @RequestParam(value = "province", required = false) String province,
                                             @RequestParam(value = "city", required = false) String city,
                                             @RequestParam(value = "create_datetime_start", required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                             @RequestParam(value = "create_datetime_end", required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                             @SortDefault(value = "eventDateTime", direction = Sort.Direction.DESC)
                                             Pageable pageable,
                                             HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMREventObject> entityPage = emrEventDomain.getEMREventFilterByStoreUrl(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMREvent::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public List<EMREvent> queryCommentEvent(@RequestParam(value = "org_id", required = false) String orgId,
                                            @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                            @RequestParam(value = "province", required = false) String province,
                                            @RequestParam(value = "city", required = false) String city,
                                            @RequestParam(value = "create_datetime_start", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                            @RequestParam(value = "create_datetime_end", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                            @SortDefault(value = "eventDateTime", direction = Sort.Direction.DESC)
                                            Pageable pageable,
                                            HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<EMREventObject> entityPage = emrEventDomain.getEMREventFilterByComment(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", entityPage.toContentRange());
        }

        return entityPage.getContent().stream()
                .map(EMREvent::new)
                .collect(Collectors.toList());
    }
}
