package com.yunsoo.marketing.api.controller;

import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.marketing.dto.UserRight;
import com.yunsoo.marketing.dto.UserRightContact;
import com.yunsoo.marketing.service.UserRightContactService;
import com.yunsoo.marketing.service.UserRightService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@RestController
@RequestMapping("/userright")
public class UserRightController {
    @Autowired
    private UserRightService userRightService;

    @Autowired
    private UserRightContactService userRightContactService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserRight createRight(@RequestBody @Valid UserRight userRight) {
        return userRightService.createUserRight(userRight);
    }

    @RequestMapping(value = "contact", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserRightContact createRightContact(@RequestBody @Valid UserRightContact userRightContact) {
        return userRightContactService.createUserRightContact(userRightContact);
    }

    @RequestMapping(value = "contact", method = RequestMethod.PATCH)
    public void updateRightContact(@RequestBody @Valid UserRightContact userRightContact) {
        userRightContactService.patchUpdate(userRightContact);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'userRight:read')")
    public Page<UserRight> getByFilter(@RequestParam(value = "marketing_id") String marketingId,
                                       @RequestParam(value = "marketing_right_id", required = false) String marketingRightId,
                                       @RequestParam(value = "type_code", required = false) String typeCode,
                                       @RequestParam(value = "status_code", required = false) String statusCode,
                                       @RequestParam(value = "created_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeGE,
                                       @RequestParam(value = "created_datetime_le", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeLE,
                                       @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                       Pageable pageable,
                                       HttpServletResponse response) {

        if (StringUtils.isEmpty(marketingId)) {
            throw new BadRequestException("marketing id is not valid");
        }

        Page<UserRight> userRightPage = userRightService.queryUserMarketing(marketingId, marketingRightId, typeCode, statusCode, createdDateTimeGE, createdDateTimeLE, pageable);

        return userRightPage;
    }

}
