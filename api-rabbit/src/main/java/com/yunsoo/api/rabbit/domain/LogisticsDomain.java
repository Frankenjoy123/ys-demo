package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.dto.LogisticsPath;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(LogisticsDomain.class);

    public List<LogisticsPath> getLogisticsPathsOrderByStartDate(String key) {
        List<LogisticsPath> logisticsPaths = new ArrayList<LogisticsPath>();

        LogisticsPathObject[] logisticsPathObjects = dataAPIClient.get("logisticspath/key/{key}", LogisticsPathObject[].class, key);
        if (logisticsPathObjects == null || logisticsPathObjects.length == 0)
            throw new NotFoundException("Logistics path not found key=" + key);

        List<LogisticsPathObject> logisticsPathObjectsList = Arrays.asList(logisticsPathObjects);
        for (LogisticsPathObject path : logisticsPathObjectsList) {
            LogisticsPath logisticsPath = new LogisticsPath();
            logisticsPath.setDesc(path.getDesc());
            logisticsPath.setEndDate(path.getEndDate());
            logisticsPath.setOperator(path.getOperator());
            logisticsPath.setProductKey(path.getProductKey());
            logisticsPath.setStartDate(path.getStartDate());

            if (path.getAction_id() != null) {
                try {
                    LogisticsCheckActionObject actionObject = dataAPIClient.get("logisticscheckaction/{id}", LogisticsCheckActionObject.class, path.getAction_id());
                    logisticsPath.setActionObject(actionObject);
                } catch (Exception ex) {
                    logisticsPath.setActionObject(null);
                    LOGGER.error("getLogisticsPathsOrderByStartDate error", ex);
                }
            } else {
                logisticsPath.setActionObject(null);
            }

            if (path.getStartCheckPoint() != null) {
                LogisticsCheckPointObject startPointObject = null;
                try {
                    startPointObject = dataAPIClient.get("logisticscheckpoint/{id}", LogisticsCheckPointObject.class, path.getStartCheckPoint());
                    logisticsPath.setStartCheckPointObject(startPointObject);
                } catch (Exception ex) {
                    logisticsPath.setStartCheckPointObject(null);
                    LOGGER.error("getLogisticsPathsOrderByStartDate error", ex);
                }

                if (startPointObject != null) {
                    try {
                        OrganizationObject startPointOrgObject = dataAPIClient.get("organization/{id}", OrganizationObject.class, startPointObject.getOrgId());
                        logisticsPath.setStartCheckPointOrgObject(startPointOrgObject);
                    }
                    catch (Exception ex) {
                        logisticsPath.setStartCheckPointOrgObject(null);
                        LOGGER.error("getLogisticsPathsOrderByStartDate error", ex);
                    }
                } else {
                    logisticsPath.setStartCheckPointOrgObject(null);
                }
            } else {
                logisticsPath.setStartCheckPointObject(null);
            }

            if (path.getEndCheckPoint() != null) {
                try {
                    LogisticsCheckPointObject endPointObject = dataAPIClient.get("logisticscheckpoint/{id}", LogisticsCheckPointObject.class, path.getEndCheckPoint());
                    logisticsPath.setEndCheckPointObject(endPointObject);
                }
                catch (Exception ex) {
                    logisticsPath.setEndCheckPointObject(null);
                    LOGGER.error("getLogisticsPathsOrderByStartDate error", ex);
                }
            } else {
                logisticsPath.setEndCheckPointObject(null);
            }

            logisticsPaths.add(logisticsPath);
        }

        return logisticsPaths;
    }
}