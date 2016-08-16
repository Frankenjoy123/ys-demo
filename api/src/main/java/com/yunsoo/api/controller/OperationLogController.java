package com.yunsoo.api.controller;

import com.yunsoo.api.auth.dto.Application;
import com.yunsoo.api.auth.service.AuthApplicationService;
import com.yunsoo.api.domain.OperationLogDomain;
import com.yunsoo.api.dto.OperationLog;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.object.OperationLogObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yan on 7/12/2016.
 */
@RestController
@RequestMapping("/operation")
public class OperationLogController {

    @Autowired
    private OperationLogDomain domain;

    @Autowired
    private AuthApplicationService authApplicationService;

    @RequestMapping(method = RequestMethod.GET)
    public List<OperationLog> query(@RequestParam(value = "account_ids") List<String> accountIds,
                                    @RequestParam(value = "app_id", required = false) String appId,
                                    @RequestParam(value = "operation", required = false) String operation,
                                    @RequestParam(value = "create_datetime_start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                                    @RequestParam(value = "create_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end,
                                    @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable,
                                    HttpServletResponse response) {

        if (accountIds.size() <= 0)
            throw new BadRequestException("the parameter account_ids should have value");
        Page<OperationLogObject> operationPage = domain.query(accountIds, operation, appId, start, end, pageable);

        HashMap<String, Application> appList = new HashMap<>();
        return PageUtils.response(response, operationPage.map(OperationLog::new).map(operationLog -> {
            String applicationId = operationLog.getCreatedAppId();
            if (appList.containsKey(applicationId))
                operationLog.setCreatedAppName(appList.get(applicationId).getName());
            else {
                Application application = authApplicationService.getById(applicationId);
                if (application != null) {
                    appList.put(applicationId, application);
                    operationLog.setCreatedAppName(application.getName());
                }
            }
            return operationLog;
        }), pageable != null);
    }
}
