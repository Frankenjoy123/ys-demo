package com.yunsoo.api.controller;

import com.yunsoo.api.util.IpUtils;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.util.ObjectIdGenerator;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "ip", method = RequestMethod.GET)
    public String echo(HttpServletRequest request) {
        return IpUtils.getIpFromRequest(request);
    }


    //id
    @RequestMapping(value = "tools/id", method = RequestMethod.GET)
    public Map<String, String> newId() {
        String id = ObjectIdGenerator.getNew();
        DateTime generatedDateTime = new DateTime(ObjectIdGenerator.getGeneratedDateFromId(id));
        Map<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("generated_datetime", DateTimeUtils.toUTCString(generatedDateTime));
        return result;
    }

    @RequestMapping(value = "tools/id/{id}", method = RequestMethod.GET)
    public Map<String, String> newId(@PathVariable String id) {
        DateTime generatedDateTime = new DateTime(ObjectIdGenerator.getGeneratedDateFromId(id));
        Map<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("generated_datetime", DateTimeUtils.toUTCString(generatedDateTime));
        return result;
    }


}
