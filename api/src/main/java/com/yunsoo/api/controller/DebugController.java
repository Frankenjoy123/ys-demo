package com.yunsoo.api.controller;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.ErrorResultCode;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.util.IdGenerator;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions: For developer debug use only.
 */
@RestController
@RequestMapping(value = "/debug")
public class DebugController {

    @Value("${yunsoo.debug}")
    private Boolean debug;

    @Autowired(required = false)
    private RestClient dataAPIClient;

    @Autowired(required = false)
    private RestClient processorClient;


    @RequestMapping(value = "")
    public Map<String, Object> info() {
        Map<String, Object> result = new HashMap<>();

        //common info
        result.put("debug", debug);

        //clients info
        Map<String, Object> clients = new HashMap<>();
        clients.put("dataAPIClient", dataAPIClient.getBaseURL());
        clients.put("processorClient", processorClient.getBaseURL());

        result.put("clients", clients);

        return result;
    }


    //id
    @RequestMapping(value = "tools/id")
    public Map<String, String> newId() {
        String id = IdGenerator.getNew();
        DateTime generatedDateTime = new DateTime(IdGenerator.getGeneratedDateFromId(id));
        Map<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("generatedDateTime", DateTimeUtils.toString(generatedDateTime));
        return result;
    }

    @RequestMapping(value = "tools/id/{id}")
    public Map<String, String> newId(@PathVariable String id) {
        DateTime generatedDateTime = new DateTime(IdGenerator.getGeneratedDateFromId(id));
        Map<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("generatedDateTime", DateTimeUtils.toString(generatedDateTime));
        return result;
    }


    @RequestMapping(value = "error/{code}")
    public void throwError(@PathVariable(value = "code") int code) throws RestErrorResultException {
        HttpStatus status;
        RestErrorResultException apiEx;
        try {
            status = HttpStatus.valueOf(code);
        } catch (IllegalArgumentException ex) {
            throw new NotFoundException("Error");
        }
        if (status.is4xxClientError() || status.is5xxServerError()) {
            throw new RestErrorResultException(status, new ErrorResult(ErrorResultCode.DEBUG, "Debug"));
        }
        throw new NotFoundException("Error");
    }

}
