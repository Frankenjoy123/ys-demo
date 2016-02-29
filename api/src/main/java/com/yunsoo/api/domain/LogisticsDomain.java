package com.yunsoo.api.domain;

import com.yunsoo.api.dto.LogisticsPath;
import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.data.object.LogisticsPathObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by  : Jerry
 * Created on  : 3/16/2015
 * Descriptions:
 */
@Component
public class LogisticsDomain {

    @Autowired
    private RestClient dataAPIClient;

    private Log log = LogFactory.getLog(this.getClass());

    public List<LogisticsPath> getLogisticsPathsOrderByStartDateTime(String key) {
        List<LogisticsPath> logisticsPaths = new ArrayList<>();

        LogisticsPathObject[] logisticsPathObjects = dataAPIClient.get("logisticspath/key/{key}", LogisticsPathObject[].class, key);
        if (logisticsPathObjects == null || logisticsPathObjects.length == 0)
            throw new NotFoundException("Logistics path not found key=" + key);

        List<LogisticsPathObject> logisticsPathObjectsList = Arrays.asList(logisticsPathObjects);
        for (LogisticsPathObject path : logisticsPathObjectsList) {
            LogisticsPath logisticsPath = new LogisticsPath();
            logisticsPath.setDescription(path.getDescription());
            logisticsPath.setEndDateTime(path.getEndDateTime());
            logisticsPath.setOperator(path.getOperator());
            logisticsPath.setProductKey(path.getProductKey());
            logisticsPath.setStartDateTime(path.getStartDateTime());

            if (path.getActionId() != null) {
                try {
                    LogisticsCheckActionObject actionObject = dataAPIClient.get("logisticscheckaction/{id}", LogisticsCheckActionObject.class, path.getActionId());
                    logisticsPath.setActionObject(actionObject);
                } catch (Exception ex) {
                    logisticsPath.setActionObject(null);
                    log.error("getLogisticsPathsOrderByStartDateTime error", ex);
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
                    log.error("getLogisticsPathsOrderByStartDateTime error", ex);
                }

                if (startPointObject != null) {
                    try {
                        OrganizationObject startPointOrgObject = dataAPIClient.get("organization/{id}", OrganizationObject.class, startPointObject.getOrgId());
                        logisticsPath.setStartCheckPointOrgObject(startPointOrgObject);
                    }
                    catch (Exception ex) {
                        logisticsPath.setStartCheckPointOrgObject(null);
                        log.error("getLogisticsPathsOrderByStartDateTime error", ex);
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
                    log.error("getLogisticsPathsOrderByStartDateTime error", ex);
                }
            } else {
                logisticsPath.setEndCheckPointObject(null);
            }

            logisticsPaths.add(logisticsPath);
        }

        return logisticsPaths;
    }
}
