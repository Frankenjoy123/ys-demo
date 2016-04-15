package com.yunsoo.api.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.DomainDirectoryObject;
import com.yunsoo.common.data.object.OrganizationConfigObject;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.ResourceInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-04-14
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class OrganizationConfigDomain {

    @Autowired
    private DomainDirectoryDomain domainDirectoryDomain;

    @Autowired
    private FileDomain fileDomain;

    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Log log = LogFactory.getLog(this.getClass());


    public OrganizationConfigObject getOrgConfigByDomainName(String domainName) {
        DomainDirectoryObject domain = domainDirectoryDomain.search(domainDirectoryDomain.getDomainDirectoryObjectMap(), domainName);
        String orgId = null;
        if (domain != null) {
            orgId = domain.getOrgId();
        }
        return getOrgConfigByOrgId(orgId);
    }

    public OrganizationConfigObject getOrgConfigByOrgId(String orgId) {
        if (orgId == null) {
            return null;
        }
        ResourceInputStream resourceInputStream = fileDomain.getFile(getConfigFilePath(orgId));
        if (resourceInputStream == null) {
            return null;
        }
        try {
            byte[] buffer = StreamUtils.copyToByteArray(resourceInputStream);
            OrganizationConfigObject.Version version = objectMapper.readValue(buffer, OrganizationConfigObject.Version.class);
            if (version != null) {
                switch (version.getVersion()) {
                    case OrganizationConfigObject.VERSION:
                        return objectMapper.readValue(buffer, OrganizationConfigObject.class);
                    default:
                        return null;
                }
            }
        } catch (IOException e) {
            log.error("organization config read exception " + StringFormatter.formatMap("orgId", orgId), e);
        }
        return null;
    }

    public void saveOrgConfig(String orgId, OrganizationConfigObject configObject) {
        try {
            configObject.setVersion(OrganizationConfigObject.VERSION);
            configObject.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            configObject.setModifiedDateTime(DateTime.now());
            byte[] buffer = objectMapper.writeValueAsBytes(configObject);
            fileDomain.putFile(getConfigFilePath(orgId), new ResourceInputStream(new ByteArrayInputStream(buffer), buffer.length, MediaType.APPLICATION_JSON_VALUE));
        } catch (JsonProcessingException e) {
            log.error("organization config save exception " + StringFormatter.formatMap("orgId", orgId), e);
        }
    }

    private String getConfigFilePath(String orgId) {
        return String.format("organization/%s/config.json", orgId);
    }

}
