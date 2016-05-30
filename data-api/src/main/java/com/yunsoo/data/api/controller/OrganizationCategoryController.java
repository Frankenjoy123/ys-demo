package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OrganizationCategoryObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.OrganizationCategoryEntity;
import com.yunsoo.data.service.repository.OrganizationCategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 5/24/2016.
 */
@RestController
@RequestMapping("orgcategory")
public class OrganizationCategoryController {

    @Autowired
    private OrganizationCategoryRepository repository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public  OrganizationCategoryObject getById(@PathVariable("id") String id){
        OrganizationCategoryObject object = toOrganizationCategoryObject(repository.findOne(id));
        if(object == null)
            throw new NotFoundException("product category not found with id:" + id);
        else
            return object;

    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrganizationCategoryObject> getByFilter(@RequestParam(value = "org_id", required = false)String id){
        List<OrganizationCategoryEntity> entities = repository.findByOrgId(id);
        if(entities ==null || entities.size()==0)
            entities = repository.findByOrgId(null);
         return entities.stream().map(this::toOrganizationCategoryObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody List<OrganizationCategoryObject> objectList){
        if(objectList == null)
            throw new BadRequestException("Parameter could not be null");
        List<OrganizationCategoryEntity> existsObjList = repository.findByOrgId(objectList.get(0).getOrgId());
        List<OrganizationCategoryEntity> entityList = objectList.stream().map(this::toOrganizationCategoryEntity).collect(Collectors.toList());
        existsObjList.forEach(item->{
            if(!entityList.contains(item))
                repository.delete(item);
        });

        repository.save(entityList);
    }


    public OrganizationCategoryObject toOrganizationCategoryObject(OrganizationCategoryEntity entity){
        if(entity == null)
            return null;

        OrganizationCategoryObject object = new OrganizationCategoryObject();
        BeanUtils.copyProperties(entity, object);
        return  object;
    }

    public OrganizationCategoryEntity toOrganizationCategoryEntity(OrganizationCategoryObject object){
        if(object == null)
            return null;

        OrganizationCategoryEntity entity = new OrganizationCategoryEntity();
        BeanUtils.copyProperties(object, entity);
        return  entity;
    }

}
