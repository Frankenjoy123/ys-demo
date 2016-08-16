package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AttachmentDomain;
import com.yunsoo.api.domain.OrganizationBrandDomain;
import com.yunsoo.api.dto.Attachment;
import com.yunsoo.api.dto.OrganizationBrand;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-08
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/organization/brand")
public class OrganizationBrandController {

    @Autowired
    private OrganizationBrandDomain organizationBrandDomain;

    @Autowired
    private AttachmentDomain attachmentDomain;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'org', 'organization_brand:read')")
    public OrganizationBrand getBrandById(@PathVariable(value = "id") String id) {
        OrganizationBrand organizationBrand = organizationBrandDomain.getOrganizationBrandById(id);
        if (organizationBrand == null) {
            throw new NotFoundException("brand not found by id: " + id);
        }

        if (StringUtils.hasText(organizationBrand.getAttachment())) {
            List<AttachmentObject> attachmentObjectList = attachmentDomain.getAttachmentList(organizationBrand.getAttachment());
            organizationBrand.setAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
        }

        return organizationBrand;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrganizationBrand> query(
            @RequestParam(value = "carrier_id") String carrierId,
            @RequestParam(value = "status_code", required = false) String statusCode,
            @RequestParam(value = "org_name", required = false) String orgName,
            @RequestParam(value = "category_id", required = false) String categoryId,
            @RequestParam(value = "created_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeGE,
            @RequestParam(value = "created_datetime_le", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeLE,
            @RequestParam(value = "search_text", required = false) String searchText,
            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletResponse response) {
        Page<OrganizationBrand> page = organizationBrandDomain.getOrganizationBrandPaged(carrierId, statusCode, orgName, categoryId, createdDateTimeGE, createdDateTimeLE, searchText, pageable);
        return PageUtils.response(response, page, pageable != null);
    }


    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Long countBrand(@RequestParam(value = "carrier_id") String carrierId) {
        return organizationBrandDomain.countOrgBrandByCarrierId(carrierId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission('current', 'org', 'organization_brand:create')")
    public OrganizationBrand createOrganizationBrand(@RequestBody @Valid OrganizationBrand organizationBrand) {
        return organizationBrandDomain.createOrganizationBrand(organizationBrand);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_brand:write')")
    public void patchBrand(@PathVariable("id") String id, @RequestBody OrganizationBrand organizationBrand) {
        organizationBrand.setId(id);
        organizationBrandDomain.patchUpdateOrganizationBrand(organizationBrand);
    }


}
