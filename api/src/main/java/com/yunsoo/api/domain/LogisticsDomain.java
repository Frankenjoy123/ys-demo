package com.yunsoo.api.domain;

import com.yunsoo.api.dto.LogisticsPath;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jerry on 3/16/2015.
 */
@Component
public class LogisticsDomain {

    @Autowired
    private RestClient dataAPIClient;

    public List<LogisticsPath> getLogisticsPathsOrderByStartDate(String key) {
        List<LogisticsPath> logisticsPaths = new ArrayList<LogisticsPath>();

        LogisticsPathObject[] logisticsPathObjects = dataAPIClient.get("logisticspath/key/{key}", LogisticsPathObject[].class, key);
        if(logisticsPathObjects == null || logisticsPathObjects.length == 0)
            throw new NotFoundException("Logistics path not found key=" + key);

        List<LogisticsPathObject> logisticsPathObjectsList = Arrays.asList(logisticsPathObjects);
        for(LogisticsPathObject path : logisticsPathObjectsList)
        {
            LogisticsPath logisticsPath = new LogisticsPath();
            logisticsPath.setDesc(path.getDesc());
            logisticsPath.setEndDate(path.getEndDate());
            logisticsPath.setOperator(path.getOperator());
            logisticsPath.setProductKey(path.getProductKey());
            logisticsPath.setStartDate(path.getStartDate());

            if(path.getAction_id() != null) {
                LogisticsCheckActionObject actionObject = dataAPIClient.get("logisticscheckaction/id/{id}", LogisticsCheckActionObject.class, path.getAction_id());
                logisticsPath.setActionObject(actionObject);
            }
            else {
                logisticsPath.setActionObject(null);
            }

            if(path.getStartCheckPoint() != null) {
                LogisticsCheckPointObject startPointObject = dataAPIClient.get("logisticscheckpoint/id/{id}", LogisticsCheckPointObject.class, path.getStartCheckPoint());
                logisticsPath.setStartCheckPointObject(startPointObject);

                if(startPointObject != null) {
                    OrganizationObject startPointOrgObject = dataAPIClient.get("organization/{id}", OrganizationObject.class, startPointObject.getOrgId());
                    logisticsPath.setStartCheckPointOrgObject(startPointOrgObject);
                }
                else{
                    logisticsPath.setStartCheckPointOrgObject(null);
                }
            }
            else {
                logisticsPath.setStartCheckPointObject(null);
            }

            if(path.getEndCheckPoint() != null) {
                LogisticsCheckPointObject endPointObject = dataAPIClient.get("logisticscheckpoint/id/{id}", LogisticsCheckPointObject.class, path.getEndCheckPoint());
                logisticsPath.setEndCheckPointObject(endPointObject);
            }
            else {
                logisticsPath.setEndCheckPointObject(null);
            }

            logisticsPaths.add(logisticsPath);
        }

        return logisticsPaths;
    }
}
