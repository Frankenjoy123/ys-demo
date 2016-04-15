package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ApplicationDomain;
import com.yunsoo.api.dto.Application;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    private ApplicationDomain applicationDomain;


    @RequestMapping(value = "{appId}", method = RequestMethod.GET)
    public Application getById(@PathVariable("appId") String appId) {
        ApplicationObject applicationObject = applicationDomain.getApplicationById(appId);
        if (applicationObject == null) {
            throw new NotFoundException("application not found by [id:" + appId + "]");
        }
        return new Application(applicationObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Application> getByFilter(@RequestParam(value = "type_code", required = false) String typeCode,
                                         @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
                                         Pageable pageable,
                                         HttpServletResponse response) {
        Page<ApplicationObject> applications = applicationDomain.getApplications(typeCode, statusCodeIn, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", applications.toContentRange());
        }
        return applications.map(Application::new).getContent();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Application create(@RequestBody @Valid Application application) {
        ApplicationObject applicationObject = application.toApplicationObject();
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        applicationObject.setCreatedAccountId(currentAccountId);
        applicationObject = applicationDomain.createApplication(applicationObject);
        return new Application(applicationObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable String id, @RequestBody Application application) {
        ApplicationObject applicationObject = application.toApplicationObject();
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        applicationObject.setId(id);
        applicationObject.setModifiedAccountId(currentAccountId);
        applicationDomain.patchUpdateApplication(applicationObject);
    }
}
