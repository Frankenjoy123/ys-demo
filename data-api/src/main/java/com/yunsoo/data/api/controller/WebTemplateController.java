package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.WebTemplateObject;
import com.yunsoo.data.service.entity.WebTemplateEntity;
import com.yunsoo.data.service.repository.WebTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Admin on 7/20/2016.
 */
@RestController
@RequestMapping("webTemplate")
public class WebTemplateController {

    @Autowired
    private WebTemplateRepository webTemplateRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<WebTemplateObject> getList(@RequestParam(value = "type_code", required = false) String typeCode,
                                           @RequestParam(value = "restriction", required = false) String restriction) {

        List<WebTemplateEntity> webTemplateEntities = webTemplateRepository.findByTypeCodeAndRestriction(typeCode, restriction);

        return webTemplateEntities.stream().map(this::toWebTemplateObject).collect(Collectors.toList());
    }

    private WebTemplateObject toWebTemplateObject(WebTemplateEntity entity) {
        if (entity == null) {
            return null;
        }

        WebTemplateObject object = new WebTemplateObject();
        object.setName(entity.getName());
        object.setVersion(entity.getVersion());
        object.setDescription(entity.getDescription());
        object.setRestriction(entity.getRestriction());
        object.setTypeCode(entity.getTypeCode());
        object.setCreatedDateTime(entity.getCreatedDateTime());

        return object;
    }

    private WebTemplateEntity toWebTemplateEntity(WebTemplateObject object) {
        if (object == null) {
            return null;
        }

        WebTemplateEntity entity = new WebTemplateEntity();
        entity.setName(object.getName());
        entity.setVersion(object.getVersion());
        entity.setDescription(object.getDescription());
        entity.setRestriction(object.getRestriction());
        entity.setTypeCode(object.getTypeCode());
        entity.setCreatedDateTime(object.getCreatedDateTime());

        return entity;
    }
}
