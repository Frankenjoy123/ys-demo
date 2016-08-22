package com.yunsoo.api.rabbit.domain;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.rabbit.file.service.FileService;
import com.yunsoo.common.data.object.OrganizationConfigObject;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.ResourceInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-04-14
 * Descriptions:
 */
@Component
public class OrganizationConfigDomain {

    @Autowired
    private FileService fileService;

    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Log log = LogFactory.getLog(this.getClass());

    /**
     * include current organization
     * if it's config for brand, then it extends from carrier config
     *
     * @param orgId      if it's null, it return default config
     * @param publicOnly true|false
     * @return
     */
    public Map<String, Object> getConfig(String orgId, boolean publicOnly) {
        Map<String, Object> configItems = new HashMap<>();
        OrganizationConfigObject configObject = getConfigObject(orgId);
        Map<String, OrganizationConfigObject.Item> items = configObject != null ? configObject.getItems() : null;
        if (items == null || items.size() == 0) return configItems;
        items.keySet().forEach(k -> {
            OrganizationConfigObject.Item item = items.get(k);
            if (publicOnly && !"public".equals(item.getAccess())) return;

            configItems.put(k, items.get(k).getValue());
        });
        return configItems;
    }


    private OrganizationConfigObject getConfigObject(String orgId) {
        String path = getConfigFilePath(orgId);
        ResourceInputStream resourceInputStream = fileService.getFile(path);
        if (resourceInputStream == null) return null;
        try {
            return objectMapper.readValue(StreamUtils.copyToByteArray(resourceInputStream), OrganizationConfigObject.class);
        } catch (IOException e) {
            log.error("organization config read exception " + StringFormatter.formatMap("path", path), e);
            return null;
        }
    }


    private String getConfigFilePath(String orgId) {
        return ObjectIdGenerator.validate(orgId) ? String.format("organization/%s/config.json", orgId) : null;
    }

}
