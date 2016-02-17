package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.LookupEntity;
import com.yunsoo.data.service.repository.LookupCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   yan
 * Created on:   9/7/2015
 * Descriptions:
 */
@RestController
@RequestMapping("/lookup")
public class LookupController {

    @Autowired
    private LookupCodeRepository lookupCodeRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LookupObject> getByFilter(@RequestParam(value = "type_code", required = false) String typeCode,
                                          @RequestParam(value = "active", required = false) Boolean active) {
        return searchLookup(typeCode, active).stream().map(this::toLookupObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "{typeCode}", method = RequestMethod.GET)
    public List<LookupObject> getByTypeCode(@PathVariable(value = "typeCode") String typeCode,
                                            @RequestParam(value = "active", required = false) Boolean active) {
        return searchLookup(typeCode, active).stream().map(this::toLookupObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "typeCode", method = RequestMethod.GET)
    public List<String> getTypeCodes() {
        return lookupCodeRepository.findDistinctTypeCode();
    }

    @RequestMapping(value = "{typeCode}/{code}", method = RequestMethod.GET)
    public LookupObject getByTypeCodeAndCode(@PathVariable(value = "typeCode") String typeCode,
                                             @PathVariable(value = "code") String code) {
        LookupEntity entity = lookupCodeRepository.findOne(new LookupEntity.LookupPK(typeCode, code));
        if (entity == null) {
            throw new NotFoundException("lookup item not found");
        }
        return toLookupObject(entity);
    }

    @RequestMapping(value = "{typeCode}/{code}", method = RequestMethod.PUT)
    public void update(@PathVariable(value = "typeCode") String typeCode,
                       @PathVariable(value = "code") String code,
                       @RequestBody LookupObject lookupObject) {
        LookupEntity entity = lookupCodeRepository.findOne(new LookupEntity.LookupPK(typeCode, code));
        if (entity == null) {
            throw new NotFoundException("lookup item not found");
        }
        if (lookupObject.getName() != null) {
            entity.setName(lookupObject.getName());
        }
        if (lookupObject.getDescription() != null) {
            entity.setDescription(lookupObject.getDescription());
        }
        if (lookupObject.getActive() != null) {
            entity.setActive(lookupObject.getActive());
        }
        lookupCodeRepository.save(entity);
    }

    private List<LookupEntity> searchLookup(String typeCode, Boolean active) {
        List<LookupEntity> entityList;
        if (typeCode != null && active != null) {
            entityList = lookupCodeRepository.findByTypeCodeAndActive(typeCode, active);
        } else if (typeCode != null) {
            entityList = lookupCodeRepository.findByTypeCode(typeCode);
        } else if (active != null) {
            entityList = lookupCodeRepository.findByActive(active);
        } else {
            entityList = lookupCodeRepository.findAll();
        }
        return entityList;
    }

    private LookupObject toLookupObject(LookupEntity entity) {
        if (entity == null) {
            return null;
        }
        LookupObject obj = new LookupObject();
        obj.setTypeCode(entity.getTypeCode());
        obj.setCode(entity.getCode());
        obj.setName(entity.getName());
        obj.setDescription(entity.getDescription());
        obj.setActive(entity.isActive());
        return obj;
    }

//    private LookupEntity toLookupEntity(LookupObject obj) {
//        if (obj == null) {
//            return null;
//        }
//        LookupEntity entity = new LookupEntity();
//        entity.setTypeCode(obj.getTypeCode());
//        entity.setCode(obj.getCode());
//        entity.setName(obj.getName());
//        entity.setDescription(obj.getDescription());
//        if (obj.isActive() == null) {
//            entity.setActive(true);
//        } else {
//            entity.setActive(obj.isActive());
//        }
//        return entity;
//    }

}
