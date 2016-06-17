package com.yunsoo.api.controller;

import com.yunsoo.api.domain.DomainDirectoryDomain;
import com.yunsoo.api.dto.DomainDirectory;
import com.yunsoo.common.data.object.DomainDirectoryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-04-13
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private DomainDirectoryDomain domainDirectoryDomain;

    //region domain directory

    @RequestMapping(value = "domainDirectory", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'domain_directory_config:read')")
    public List<DomainDirectory> getAll() {
        return domainDirectoryDomain.getDomainDirectoryObjectList().stream()
                .map(DomainDirectory::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "domainDirectory", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission('*', 'org', 'domain_directory_config:write')")
    public void putOne(@RequestParam(name = "name", required = true) String name,
                       @RequestBody DomainDirectory domainDirectory) {
        DomainDirectoryObject domainDirectoryObject = domainDirectory.toDomainDirectoryObject();
        domainDirectoryObject.setName(name);
        domainDirectoryDomain.putDomainDirectoryObject(domainDirectoryObject);
    }

    @RequestMapping(value = "domainDirectory", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission('*', 'org', 'domain_directory_config:delete')")
    public void deleteOne(@RequestParam(name = "name", required = true) String name) {
        if (StringUtils.hasText(name)) {
            domainDirectoryDomain.deleteDomainDirectoryObjectByName(name);
        }
    }

    //endregion

}
