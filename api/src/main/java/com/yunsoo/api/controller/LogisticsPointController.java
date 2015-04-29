package com.yunsoo.api.controller;

import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jerry on 3/24/2015.
 */
@RestController
@RequestMapping("/logisticspoint")
public class LogisticsPointController {

    @Autowired
    private RestClient dataAPIClient;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#logisticsCheckPointObject, 'logisticspoint:create')")
    public LogisticsCheckPointObject create(@RequestBody LogisticsCheckPointObject logisticsCheckPointObject) {

        LogisticsCheckPointObject newObject = dataAPIClient.post("logisticscheckpoint", logisticsCheckPointObject, LogisticsCheckPointObject.class);
        return newObject;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'logisticspoint:read')")
    public LogisticsCheckPointObject get(@PathVariable(value = "id") String id) {

        LogisticsCheckPointObject logisticsCheckPointObject = dataAPIClient.get("logisticscheckpoint/{id}", LogisticsCheckPointObject.class, id);
        if(logisticsCheckPointObject == null)
            throw new NotFoundException("Logistics point not found id=" + id);

        return logisticsCheckPointObject;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'logisticspoint:read')")
    public List<LogisticsCheckPointObject> get(@RequestParam(value = "orgId", required = true) String orgId,
                                                @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        LogisticsCheckPointObject[] objects =
                dataAPIClient.get("logisticscheckpoint?orgId={orgId}&&pageIndex={pageIndex}&&pageSize={pageSize}",
                        LogisticsCheckPointObject[].class,
                        orgId,
                        pageIndex,
                        pageSize);

        if (objects == null)
            throw new NotFoundException("LogisticsCheckPointObject not found");

        return Arrays.asList(objects);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#logisticsCheckPointObject, 'logisticspoint:update')")
    public void patch(@RequestBody LogisticsCheckPointObject logisticsCheckPointObject) {

        dataAPIClient.patch("logisticscheckpoint", logisticsCheckPointObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {

        dataAPIClient.delete("logisticscheckpoint/{id}", id);
    }
}
