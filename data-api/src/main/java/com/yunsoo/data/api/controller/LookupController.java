package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.LookupEntity;
import com.yunsoo.data.service.repository.LookupCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 9/7/2015.
 */
@RestController
@RequestMapping("/lookup")
public class LookupController {

    @Autowired
    private LookupCodeRepository repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LookupObject> getByFilter(@RequestParam(value = "type_code", required = false) String typeCode,
                                          @RequestParam(value = "active", required = false) Boolean active,
                                          Pageable pageable,
                                          HttpServletResponse response){
        List<LookupObject> lookupObjectList = new ArrayList<>();
        Iterable<LookupEntity> entityList;
        if( typeCode!=null && active != null )
            entityList = repository.findByTypeCodeAndActive(typeCode, active);
        else if(typeCode != null)
            entityList = repository.findByTypeCode(typeCode);
        else if(active != null )
            entityList = repository.findByActive(active, pageable);
        else
            entityList = repository.findAll(pageable);

        entityList.forEach(item-> lookupObjectList.add(toLookupObject(item)));
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(((Page<LookupEntity>) entityList).getNumber(), ((Page<LookupEntity>) entityList).getTotalPages()));
        }
        return lookupObjectList;
    }
    @RequestMapping(value = "/typeCodes", method = RequestMethod.GET)
    public List<String> getTypeCodes(){
        return repository.findDistinctTypCode();
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void save(@RequestBody LookupObject lookupObject){
        repository.save(toLookupEntity(lookupObject));
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody LookupObject lookupObject){
        repository.delete(toLookupEntity(lookupObject));
    }


    private LookupObject toLookupObject(LookupEntity entity){
        if(entity == null)
            return null;
        LookupObject obj = new LookupObject();
        obj.setCode(entity.getCode());
        obj.setActive(entity.isActive());
        obj.setDescription(entity.getDescription());
        obj.setName(entity.getName());
        obj.setTypeCode(entity.getTypeCode());
        return obj;
    }

    private LookupEntity toLookupEntity(LookupObject lookupObject){
        LookupEntity entity = new LookupEntity();
        entity.setName(lookupObject.getName());
        entity.setDescription(lookupObject.getDescription());
        entity.setCode(lookupObject.getCode());
        if(lookupObject.isActive() == null)
            entity.setActive(true);
        else
            entity.setActive(lookupObject.isActive());
        entity.setTypeCode(lookupObject.getTypeCode());
        return entity;
    }
}
