package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.DomainDirectoryObject;
import com.yunsoo.data.service.entity.DomainDirectoryEntity;
import com.yunsoo.data.service.repository.DomainDirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-04-12
 * Descriptions:
 */
@RestController
@RequestMapping("/domainDirectory")
public class DomainDirectoryController {

    @Autowired
    private DomainDirectoryRepository domainDirectoryRepository;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DomainDirectoryObject> getAll() {
        return domainDirectoryRepository.findAll().stream()
                .map(this::toDomainDirectoryObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void putOne(@RequestParam(name = "name", required = true) String name,
                       @RequestBody DomainDirectoryObject obj) {
        if (StringUtils.hasText(name)) {
            DomainDirectoryEntity entity = toDomainDirectoryEntity(obj);
            entity.setName(name);
            domainDirectoryRepository.save(entity);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deleteOne(@RequestParam(name = "name", required = true) String name) {
        if (StringUtils.hasText(name)) {
            domainDirectoryRepository.delete(name);
        }
    }


    private DomainDirectoryObject toDomainDirectoryObject(DomainDirectoryEntity entity) {
        if (entity == null) {
            return null;
        }
        DomainDirectoryObject obj = new DomainDirectoryObject();
        obj.setName(entity.getName());
        obj.setDescription(entity.getDescription());
        obj.setOrgId(entity.getOrgId());
        return obj;
    }

    private DomainDirectoryEntity toDomainDirectoryEntity(DomainDirectoryObject obj) {
        if (obj == null) {
            return null;
        }
        DomainDirectoryEntity entity = new DomainDirectoryEntity();
        entity.setName(obj.getName());
        entity.setDescription(obj.getDescription());
        entity.setOrgId(obj.getOrgId());
        return entity;
    }

}
