package com.yunsoo.api.controller;

import com.yunsoo.api.domain.OrganizationCategoryDomain;
import com.yunsoo.api.dto.OrganizationCategory;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.OrganizationCategoryObject;
import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 5/24/2016.
 */
@RestController
@RequestMapping("orgcategory")
public class OrganizationCategoryController {

    @Autowired
    private OrganizationCategoryDomain domain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrganizationCategory> getByFilter(@RequestParam(value = "org_id", required = false) String orgId){
        orgId = AuthUtils.fixOrgId(orgId);
        List<OrganizationCategoryObject> objectList = domain.getCategoriesByOrgId(orgId);
        return objectList.stream().map(OrganizationCategory::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveOrganizationCategory(@RequestBody List<OrganizationCategory> categoryList){
        if(categoryList == null)
            throw new BadRequestException("could not update organization category list without content");
        String orgId = AuthUtils.fixOrgId(null);
        domain.saveList(orgId, categoryList);
    }

}
