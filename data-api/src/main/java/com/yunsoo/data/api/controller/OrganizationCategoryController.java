package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OrganizationCategoryObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
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

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationCategoryObject save(@RequestBody OrganizationCategoryObject categoryObject){
        List<OrganizationCategoryEntity> existEntity = repository.findByOrgIdAndName(categoryObject.getOrgId(), categoryObject.getName().trim());
        if(existEntity.size() > 0)
            throw new ConflictException("same name exist for org category: " + categoryObject.getName());

        categoryObject.setId(null);
        categoryObject.setName(categoryObject.getName().trim());
        OrganizationCategoryEntity entity = toOrganizationCategoryEntity(categoryObject);
        repository.save(entity);
        return toOrganizationCategoryObject(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id")String id,  @RequestBody OrganizationCategoryObject categoryObject){
        OrganizationCategoryEntity entity = repository.findOne(id);
        if(entity == null)
            throw new NotFoundException("product category not found with id:" + id);

        List<OrganizationCategoryEntity> existEntity = repository.findByOrgIdAndName(categoryObject.getOrgId(), categoryObject.getName().trim());
        if(existEntity.size() == 1 && !existEntity.get(0).getId().equals(entity.getId()))
            throw new ConflictException("same name exist for org category: " + categoryObject.getName());

        categoryObject.setName(categoryObject.getName().trim());
        repository.save(toOrganizationCategoryEntity(categoryObject));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id")String id){
        OrganizationCategoryEntity entity = repository.findOne(id);
        if(entity == null)
            throw new NotFoundException("product category not found with id:" + id);
        repository.delete(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateList(@RequestBody List<OrganizationCategoryObject> objectList){
        if(objectList == null)
            throw new BadRequestException("Parameter could not be null");
        List<OrganizationCategoryEntity> existsObjList = repository.findByOrgId(objectList.get(0).getOrgId());
        List<OrganizationCategoryEntity> entityList = objectList.stream().map(this::toOrganizationCategoryEntity).collect(Collectors.toList());
        existsObjList.forEach(item->{
            boolean delete = true;

                for(OrganizationCategoryEntity entity : entityList){
                    if(entity.getId()!=null && entity.getId().equals(item.getId())){
                        delete = false;
                        break;
                    }
                }

                if(delete)
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
