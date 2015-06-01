package com.yunsoo.api.controller;

import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.util.IdGenerator;
import org.joda.time.DateTime;
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

    //id
    @RequestMapping(value = "tools/id")
    public Map<String, String> newId() {
        String id = IdGenerator.getNew();
        DateTime generatedDateTime = new DateTime(IdGenerator.getGeneratedDateFromId(id));
        Map<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("generated_datetime", DateTimeUtils.toUTCString(generatedDateTime));
        return result;
    }

    @RequestMapping(value = "tools/id/{id}")
    public Map<String, String> newId(@PathVariable String id) {
        DateTime generatedDateTime = new DateTime(IdGenerator.getGeneratedDateFromId(id));
        Map<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("generated_datetime", DateTimeUtils.toUTCString(generatedDateTime));
        return result;
    }


}
