package com.yunsoo.api.controller;

import com.yunsoo.api.domain.WebTemplateDomain;
import com.yunsoo.api.dto.WebTemplate;
import com.yunsoo.common.data.object.WebTemplateObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Admin
 * Created on:   7/20/2016
 * Descriptions:
 */
@RestController
@RequestMapping("/webTemplate")
public class WebTemplateController {

    @Autowired
    private WebTemplateDomain webTemplateDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<WebTemplate> getList(@RequestParam(value = "type_code", required = false) String typeCode,
                                     @RequestParam(value = "restriction", required = false) String restriction) {

        List<WebTemplateObject> webTemplateObjectList = webTemplateDomain.getWebTemplateList(typeCode, restriction);

        return webTemplateObjectList.stream()
                .map(WebTemplate::new)
                .collect(Collectors.toList());
    }
}
