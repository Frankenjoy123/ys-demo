package com.yunsoo.api.controller;

import com.yunsoo.api.dto.LogisticsAction;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jerry on 3/24/2015.
 */
@RestController
@RequestMapping("/logisticsaction")
public class LogisticsActionController {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#logisticsAction, 'logisticsaction:create')")
    public LogisticsAction create(@RequestBody LogisticsAction logisticsAction) {

        if (logisticsAction.getOrgId() == null || "current".equals(logisticsAction.getOrgId())) { //get current Organization
            String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
            logisticsAction.setOrgId(orgId);
        }

        LogisticsCheckActionObject logisticsCheckActionObject = toLogisticsCheckActionObject(logisticsAction);
        LogisticsCheckActionObject newObject = dataAPIClient.post("logisticscheckaction", logisticsCheckActionObject, LogisticsCheckActionObject.class);

        LogisticsAction newAction = fromLogisticsCheckActionObject(newObject);
        return newAction;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'logisticsaction:read')")
    public LogisticsAction get(@PathVariable(value = "id") Integer id) {

        LogisticsCheckActionObject logisticsCheckActionObject = dataAPIClient.get("logisticscheckaction/{id}", LogisticsCheckActionObject.class, id);
        if (logisticsCheckActionObject == null)
            throw new NotFoundException("Logistics action not found id=" + id);

        LogisticsAction returnAction = fromLogisticsCheckActionObject(logisticsCheckActionObject);
        return returnAction;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'logisticsaction:read')")
    public List<LogisticsAction> get(@RequestParam(value = "orgId", required = true) String orgId,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        if ("current".equals(orgId)) { //get current Organization
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }

        LogisticsCheckActionObject[] objects =
                dataAPIClient.get("logisticscheckaction?orgId={orgId}&&pageIndex={pageIndex}&&pageSize={pageSize}",
                        LogisticsCheckActionObject[].class,
                        orgId,
                        pageIndex,
                        pageSize);

        if (objects == null)
            throw new NotFoundException("LogisticsCheckActionObject not found");

        List<LogisticsAction> logisticsActions = fromLogisticsCheckActionObjectList(Arrays.asList(objects));

        return logisticsActions;
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#logisticsAction, 'logisticsaction:update')")
    public void patch(@RequestBody LogisticsAction logisticsAction) {

        if (logisticsAction.getOrgId() == null || "current".equals(logisticsAction.getOrgId())) { //get current Organization
            String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
            logisticsAction.setOrgId(orgId);
        }

        LogisticsCheckActionObject logisticsCheckActionObject = toLogisticsCheckActionObject(logisticsAction);
        dataAPIClient.patch("logisticscheckaction", logisticsCheckActionObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Integer id) {

        dataAPIClient.delete("logisticscheckaction/{id}", id);
    }

    private LogisticsAction fromLogisticsCheckActionObject(LogisticsCheckActionObject logisticsCheckActionObject) {
        LogisticsAction logisticsAction = new LogisticsAction();
        logisticsAction.setId(logisticsCheckActionObject.getId());
        logisticsAction.setDescription(logisticsCheckActionObject.getDescription());
        logisticsAction.setName(logisticsCheckActionObject.getName());
        logisticsAction.setOrgId(logisticsCheckActionObject.getOrgId());

        return logisticsAction;
    }

    private List<LogisticsAction> fromLogisticsCheckActionObjectList(List<LogisticsCheckActionObject> logisticsCheckActionObjects) {
        List<LogisticsAction> logisticsActions = new ArrayList<LogisticsAction>();
        for (LogisticsCheckActionObject model : logisticsCheckActionObjects) {
            logisticsActions.add(fromLogisticsCheckActionObject(model));
        }

        return logisticsActions;
    }

    private LogisticsCheckActionObject toLogisticsCheckActionObject(LogisticsAction logisticsAction) {
        LogisticsCheckActionObject logisticsCheckActionObject = new LogisticsCheckActionObject();
        logisticsCheckActionObject.setId(logisticsAction.getId());
        logisticsCheckActionObject.setDescription(logisticsAction.getDescription());
        logisticsCheckActionObject.setName(logisticsAction.getName());
        logisticsCheckActionObject.setOrgId(logisticsAction.getOrgId());

        return logisticsCheckActionObject;
    }
}
