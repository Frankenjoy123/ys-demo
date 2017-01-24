package com.yunsoo.api.controller.analysis;

import com.yunsoo.api.di.service.EMREventService;
import com.yunsoo.api.dto.EMREvent;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
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

/**
 * Created by Admin on 6/13/2016.
 */
@RestController
@RequestMapping("/emr/event")
public class EMREventController {

    //@Autowired
    //private EMREventDomain emrEventDomain;

    @Autowired
    private EMREventService emrEventService;

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
        Page<EMREventObject> entityPage = emrEventService.getEMREventList(orgId, userId, ysId, eventDateTimeStart, eventDateTimeEnd, pageable);
        //返回分享，中奖等信息

        return PageUtils.response(response, entityPage.map(EMREvent::new), pageable != null);
    }

}
