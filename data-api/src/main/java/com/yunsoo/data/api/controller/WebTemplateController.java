package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.WebTemplateObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.WebTemplateEntity;
import com.yunsoo.data.service.repository.WebTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Admin
 * Created on:   7/20/2016
 * Descriptions:
 */
@RestController
@RequestMapping("webTemplate")
public class WebTemplateController {

    @Autowired
    private WebTemplateRepository webTemplateRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public WebTemplateObject getById(@PathVariable(value = "id") String id) {
        WebTemplateEntity entity = webTemplateRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("web template not found");
        }
        return toWebTemplateObject(entity);
    }

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

        WebTemplateObject obj = new WebTemplateObject();
        obj.setId(entity.getId());
        obj.setName(entity.getName());
        obj.setVersion(entity.getVersion());
        obj.setTypeCode(entity.getTypeCode());
        obj.setDescription(entity.getDescription());
        obj.setRestriction(entity.getRestriction());
        obj.setCreatedDateTime(entity.getCreatedDateTime());

        return obj;
    }

    private WebTemplateEntity toWebTemplateEntity(WebTemplateObject obj) {
        if (obj == null) {
            return null;
        }

        WebTemplateEntity entity = new WebTemplateEntity();
        entity.setId(obj.getId());
        entity.setName(obj.getName());
        entity.setVersion(obj.getVersion());
        entity.setTypeCode(obj.getTypeCode());
        entity.setDescription(obj.getDescription());
        entity.setRestriction(obj.getRestriction());
        entity.setCreatedDateTime(obj.getCreatedDateTime());

        return entity;
    }
}
