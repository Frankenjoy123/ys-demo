package com.yunsoo.api.controller;

import com.yunsoo.api.config.YSConfigProperties;
import com.yunsoo.api.domain.ApplicationDomain;
import com.yunsoo.api.domain.FileDomain;
import com.yunsoo.api.dto.Application;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/9/7
 * Descriptions:
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private YSConfigProperties YSConfig;

    @Autowired
    private ApplicationDomain applicationDomain;

    @Autowired
    private FileDomain fileDomain;

    //region application

    @RequestMapping(value = "{appId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'application:read')")
    public Application getById(@PathVariable("appId") String appId) {
        ApplicationObject applicationObject = applicationDomain.getApplicationById(appId);
        if (applicationObject == null) {
            throw new NotFoundException("application not found by [id:" + appId + "]");
        }
        return new Application(applicationObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'application:read')")
    public List<Application> getByFilter(@RequestParam(value = "type_code", required = false) String typeCode,
                                         @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
                                         Pageable pageable,
                                         HttpServletResponse response) {
        Page<ApplicationObject> applications = applicationDomain.getApplications(typeCode, statusCodeIn, pageable);
        return PageUtils.response(response, applications.map(Application::new), pageable != null);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission('*', 'org', 'application:create')")
    public Application create(@RequestBody @Valid Application application) {
        ApplicationObject applicationObject = application.toApplicationObject();
        applicationObject = applicationDomain.createApplication(applicationObject);
        return new Application(applicationObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission('*', 'org', 'application:write')")
    public void patchUpdate(@PathVariable String id, @RequestBody Application application) {
        ApplicationObject applicationObject = application.toApplicationObject();
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        applicationObject.setId(id);
        applicationObject.setModifiedAccountId(currentAccountId);
        applicationDomain.patchUpdateApplication(applicationObject);
    }

    //endregion

    //region config

    @RequestMapping(value = "{appId}/config", method = RequestMethod.GET)
    public ResponseEntity<?> getDefaultConfigByApplicationId(@PathVariable("appId") String appId) {
        if (!ObjectIdGenerator.validate(appId)) {
            throw new BadRequestException("appId not valid");
        }
        String env = YSConfig.getEnvironment();
        ResourceInputStream resourceInputStream = fileDomain.getFile(String.format("application/%s/config.%s", appId, env));
        if (resourceInputStream == null) {
            resourceInputStream = fileDomain.getFile(String.format("application/%s/config", appId));
        }
        if (resourceInputStream == null) {
            throw new NotFoundException("config not found");
        }
        return resourceInputStream.toResponseEntity();
    }

    //endregion

}
