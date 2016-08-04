package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.PermissionRegion;
import com.yunsoo.auth.service.PermissionRegionService;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-29
 * Descriptions:
 */
@RestController
@RequestMapping("/permission/region")
public class PermissionRegionController {

    @Autowired
    private PermissionRegionService permissionRegionService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'permission_region:read')")
    public PermissionRegion getById(@PathVariable("id") String id) {
        return findById(id);
    }

    @RequestMapping(value = "default", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'permission_region:read')")
    public PermissionRegion getDefaultByOrgId(@RequestParam(name = "org_id", required = false) String orgId) {
        orgId = AuthUtils.fixOrgId(orgId);
        return permissionRegionService.getOrCreateDefaultPermissionRegion(orgId);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'permission_region:read')")
    public List<PermissionRegion> getList(@RequestParam(name = "org_id", required = false) String orgId,
                                          @RequestParam(name = "type_code", required = false) String typeCode) {
        orgId = AuthUtils.fixOrgId(orgId);
        return permissionRegionService.getList(orgId, typeCode);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#permissionRegion, 'permission_region:read')")
    public PermissionRegion create(@RequestBody @Valid PermissionRegion permissionRegion) {
        permissionRegion.setOrgId(AuthUtils.fixOrgId(permissionRegion.getOrgId()));
        return permissionRegionService.create(permissionRegion);
    }

    @RequestMapping(value = "{id}/restrictions", method = RequestMethod.POST)
    public List<String> addRestrictionsToRegion(@PathVariable("id") String id,
                                                @RequestBody List<String> restrictions) {
        PermissionRegion region = findById(id);
        AuthUtils.checkPermission(region.getOrgId(), "permission_region", "write");
        return permissionRegionService.addRestrictionsToRegionById(region.getId(), restrictions);
    }

    @RequestMapping(value = "default/restrictions", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#orgId, 'org', 'permission_region:write')")
    public List<String> addRestrictionsToDefaultRegion(@RequestParam(name = "org_id", required = false) String orgId,
                                                       @RequestBody List<String> restrictions) {
        orgId = AuthUtils.fixOrgId(orgId);
        PermissionRegion defaultRegion = permissionRegionService.getOrCreateDefaultPermissionRegion(orgId);
        return permissionRegionService.addRestrictionsToRegionById(defaultRegion.getId(), restrictions);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable("id") String id,
                            @RequestBody PermissionRegion permissionRegion) {
        PermissionRegion region = findById(id);
        AuthUtils.checkPermission(region.getOrgId(), "permission_region", "write");
        permissionRegion.setId(id);
        permissionRegionService.patchUpdate(permissionRegion);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        permissionRegionService.deleteById(id);
    }

    private PermissionRegion findById(String id) {
        PermissionRegion region = permissionRegionService.getById(id);
        if (region == null) {
            throw new NotFoundException("region not found by id: " + id);
        }
        return region;
    }
}
