package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.dto.Application;
import com.yunsoo.api.rabbit.dto.ApplicationResult;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Zhe on 2015/6/15.
 * General client application controller.
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ApplicationResult getById(@PathVariable String id) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");

        ApplicationResult result = new ApplicationResult();

        //set current application
        Application application = dataAPIClient.get("application/{id}", Application.class, id);
        result.setCurrentApplication(application);

        //set latest application version
        List<Application> applicationList = dataAPIClient.get("/application/type/{0}", new ParameterizedTypeReference<List<Application>>() {
        }, application.getTypeCode());
        result.setLatestApplication(applicationList.get(applicationList.size() - 1));

        return result;
    }

    @RequestMapping(value = "type/{typeid}", method = RequestMethod.GET)
    public List<Application> getByTypeId(@PathVariable String typeid) {
        if (typeid == null || typeid.isEmpty()) throw new BadRequestException("typeid不能为空！");

        List<Application> applicationList = dataAPIClient.get("/application/type/{0}", new ParameterizedTypeReference<List<Application>>() {
        }, typeid);
        return applicationList;
    }
}
