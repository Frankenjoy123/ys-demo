package com.yunsoo.api.controller;

import com.yunsoo.api.dto.LogisticsPoint;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.LogisticsCheckPointObject;
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
@RequestMapping("/logisticspoint")
public class LogisticsPointController {

    @Autowired
    private RestClient dataApiClient;


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#logisticsPoint, 'logistics_point:create')")
    public LogisticsPoint create(@RequestBody LogisticsPoint logisticsPoint) {

        if (logisticsPoint.getOrgId() == null || "current".equals(logisticsPoint.getOrgId())) { //get current Organization
            String orgId = AuthUtils.getCurrentAccount().getOrgId();
            logisticsPoint.setOrgId(orgId);
        }

        LogisticsCheckPointObject logisticsCheckPointObject = toLogisticsCheckPointObject(logisticsPoint);
        LogisticsCheckPointObject newObject = dataApiClient.post("logisticscheckpoint", logisticsCheckPointObject, LogisticsCheckPointObject.class);

        LogisticsPoint newPoint = fromLogisticsCheckPointObject(newObject);

        return newPoint;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'logistics_point:read')")
    public LogisticsPoint get(@PathVariable(value = "id") String id) {

        LogisticsCheckPointObject logisticsCheckPointObject = dataApiClient.get("logisticscheckpoint/{id}", LogisticsCheckPointObject.class, id);
        if(logisticsCheckPointObject == null)
            throw new NotFoundException("Logistics point not found id=" + id);

        LogisticsPoint resultPoint = fromLogisticsCheckPointObject(logisticsCheckPointObject);

        return resultPoint;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'logistics_point:read')")
    public List<LogisticsPoint> get(@RequestParam(value = "orgId", required = true) String orgId,
                                                @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        if ("current".equals(orgId)) { //get current Organization
            orgId = AuthUtils.getCurrentAccount().getOrgId();
        }

        LogisticsCheckPointObject[] objects =
                dataApiClient.get("logisticscheckpoint?orgId={orgId}&&pageIndex={pageIndex}&&pageSize={pageSize}",
                        LogisticsCheckPointObject[].class,
                        orgId,
                        pageIndex,
                        pageSize);

        if (objects == null)
            throw new NotFoundException("LogisticsCheckPointObject not found");

        List<LogisticsPoint> logisticsPoints = fromLogisticsCheckPointObjectList(Arrays.asList(objects));

        return logisticsPoints;
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#logisticsPoint, 'logistics_point:write')")
    public void patch(@RequestBody LogisticsPoint logisticsPoint) {

        if (logisticsPoint.getOrgId() == null || "current".equals(logisticsPoint.getOrgId())) { //get current Organization
            String orgId = AuthUtils.getCurrentAccount().getOrgId();
            logisticsPoint.setOrgId(orgId);
        }

        LogisticsCheckPointObject logisticsCheckPointObject = toLogisticsCheckPointObject(logisticsPoint);
        dataApiClient.patch("logisticscheckpoint", logisticsCheckPointObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {

        dataApiClient.delete("logisticscheckpoint/{id}", id);
    }

    private LogisticsPoint fromLogisticsCheckPointObject(LogisticsCheckPointObject logisticsCheckPointObject)
    {
        LogisticsPoint logisticsPoint = new LogisticsPoint();
        logisticsPoint.setId(logisticsCheckPointObject.getId());
        logisticsPoint.setOrgId(logisticsCheckPointObject.getOrgId());
        logisticsPoint.setDescription(logisticsCheckPointObject.getDescription());
        logisticsPoint.setName(logisticsCheckPointObject.getName());
        logisticsPoint.setLocationId(logisticsCheckPointObject.getLocationId());

        return logisticsPoint;
    }

    private List<LogisticsPoint> fromLogisticsCheckPointObjectList(List<LogisticsCheckPointObject> logisticsCheckPointObjects)
    {
        List<LogisticsPoint> logisticsPoints = new ArrayList<LogisticsPoint>();
        for (LogisticsCheckPointObject model : logisticsCheckPointObjects) {
            logisticsPoints.add(fromLogisticsCheckPointObject(model));
        }

        return logisticsPoints;
    }

    private LogisticsCheckPointObject toLogisticsCheckPointObject(LogisticsPoint logisticsPoint)
    {
        LogisticsCheckPointObject logisticsCheckPointObject = new LogisticsCheckPointObject();
        logisticsCheckPointObject.setId(logisticsPoint.getId());
        logisticsCheckPointObject.setName(logisticsPoint.getName());
        logisticsCheckPointObject.setLocationId(logisticsPoint.getLocationId());
        logisticsCheckPointObject.setDescription(logisticsPoint.getDescription());
        logisticsCheckPointObject.setOrgId(logisticsPoint.getOrgId());

        return logisticsCheckPointObject;
    }
}
