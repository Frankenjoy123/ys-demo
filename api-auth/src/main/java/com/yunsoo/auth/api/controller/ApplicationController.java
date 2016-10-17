package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dto.Application;
import com.yunsoo.auth.service.ApplicationService;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;


    @RequestMapping(value = "{appId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'application:read')")
    public Application getById(@PathVariable("appId") String appId) {
        Application application = applicationService.getById(appId);
        if (application == null) {
            throw new NotFoundException("application not found by [id:" + appId + "]");
        }
        return application;
    }

    @RequestMapping(value = "{appId}/name", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getNameById(@PathVariable("appId") String appId) {
        Application application = applicationService.getById(appId);
        if (application == null) {
            throw new NotFoundException("application not found by [id:" + appId + "]");
        }
        return application.getName();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'application:read')")
    public List<Application> getList(Pageable pageable,
                                     @RequestParam(value = "type_code", required = false) String typeCode,
                                     HttpServletResponse response) {
        Page<Application> applications = applicationService.getList(typeCode, pageable);
        return PageUtils.response(response, applications, pageable != null);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission('*', 'org', 'application:create')")
    public Application create(@RequestBody @Valid Application application) {
        return applicationService.create(application);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission('*', 'org', 'application:write')")
    public void patchUpdate(@PathVariable String id, @RequestBody Application application) {
        application.setId(id);
        applicationService.patchUpdate(application);
    }

}