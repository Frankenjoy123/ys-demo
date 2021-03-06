package com.yunsoo.api.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.auth.dto.Organization;
import com.yunsoo.api.auth.service.AuthOrganizationService;
import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.file.service.FileService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.data.object.OrganizationConfigObject;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.ResourceInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-04-14
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class OrganizationConfigDomain {

    @Autowired
    private FileService fileService;

    @Autowired
    private OrganizationBrandDomain organizationBrandDomain;

    @Autowired
    private AuthOrganizationService authOrganizationService;


    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Log log = LogFactory.getLog(this.getClass());

    /**
     * include current organization
     * if it's config for brand, then it extends from carrier config
     *
     * @param orgId      if it's null, it return default config
     * @param publicOnly true|false
     * @param prefix     nullable
     * @return
     */
    public Map<String, Object> getConfig(String orgId, boolean publicOnly, String prefix) {
        Map<String, Object> configItems = new HashMap<>();
        String orgName = authOrganizationService.getNameById(orgId);

        OrganizationConfigObject configObject;
        if (orgName == null) {
            configObject = getDefaultConfigObject();
        } else {
            configObject = getConfigObject(orgId);

            if (configObject == null) configObject = new OrganizationConfigObject();

            //extend carrier config if it's brand
            OrgBrandObject orgBrandObject = organizationBrandDomain.getOrgBrandObjectById(orgId);
            if (orgBrandObject != null && !StringUtils.isEmpty(orgBrandObject.getCarrierId())) {
                extend(configObject, getConfigObject(orgBrandObject.getCarrierId()));
            }
        }
        Map<String, OrganizationConfigObject.Item> items = configObject.getItems();
        if (items == null || items.size() == 0) return configItems;
        items.keySet().forEach(k -> {
            if (prefix != null && !k.startsWith(prefix)) return;

            OrganizationConfigObject.Item item = items.get(k);
            if (publicOnly && !"public".equals(item.getAccess())) return;

            configItems.put(k, items.get(k).getValue());
        });
        if (orgName != null) {
            Organization org = new Organization();
            org.setId(orgId);
            org.setName(orgName);
            configItems.put("organization", org);
        }

        configItems.keySet().forEach(key -> {
            if (key.equals("webchat.app_secret"))
                configItems.put(key, encode((String) configItems.get(key)));
            else if (key.equals(("webchat.private_key")))
                configItems.put(key, encode((String) configItems.get(key)));


        });

        return configItems;
    }

    public void saveConfig(String orgId, Map<String, Object> configItems) {
        if (orgId == null || configItems == null || configItems.size() == 0) return;

        OrganizationConfigObject configObject = getConfigObject(orgId);
        if (configObject == null) configObject = new OrganizationConfigObject();
        if (configObject.getVersion() == null) configObject.setVersion(OrganizationConfigObject.VERSION);
        if (configObject.getItems() == null) configObject.setItems(new HashMap<>());

        Map<String, OrganizationConfigObject.Item> items = configObject.getItems();
        configItems.keySet().forEach(k -> {
            Object v = configItems.get(k);
            if (v == null) {
                items.remove(k);
            } else {
                OrganizationConfigObject.Item item = items.get(k);
                if (item == null) {
                    item = new OrganizationConfigObject.Item();
                    items.put(k, item);
                }
                item.setValue(v);
            }
        });


        configObject.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
        configObject.setModifiedDateTime(DateTime.now());
        saveConfigObject(orgId, configObject);
    }


    private void extend(OrganizationConfigObject target, OrganizationConfigObject source) {
        if (target == null || source == null) return;
        if (target.getItems() == null) target.setItems(new HashMap<>());

        if (source.getVersion() != null) target.setVersion(source.getVersion());
        if (source.getModifiedAccountId() != null) target.setModifiedAccountId(source.getModifiedAccountId());
        if (source.getModifiedDateTime() != null) target.setModifiedDateTime(source.getModifiedDateTime());

        Map<String, OrganizationConfigObject.Item> sourceItems = source.getItems();
        Map<String, OrganizationConfigObject.Item> targetItems = target.getItems();
        if (sourceItems != null) {
            sourceItems.keySet().forEach(k -> {
                OrganizationConfigObject.Item item = sourceItems.get(k);
                if (item != null && (targetItems.get(k) == null || !("public".equals(item.getAccess()) || "protected".equals(item.getAccess()))))
                    targetItems.put(k, item);
            });
        }
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

    private void saveConfigObject(String orgId, OrganizationConfigObject configObject) {
        String path = getConfigFilePath(orgId);
        if (path == null) return;
        byte[] buffer;
        try {
            buffer = objectMapper.writeValueAsBytes(configObject);
            fileService.putFile(path, new ResourceInputStream(new ByteArrayInputStream(buffer), buffer.length, MediaType.APPLICATION_JSON_UTF8_VALUE));
        } catch (JsonProcessingException e) {
            log.error("organization config read exception " + StringFormatter.formatMap("path", path), e);
        }
    }

    private OrganizationConfigObject getDefaultConfigObject() {
        OrganizationConfigObject defaultConfigObject = new OrganizationConfigObject();
        defaultConfigObject.setVersion(OrganizationConfigObject.VERSION);
        defaultConfigObject.setDescriptions("value of items can be any json object, descriptors can be [public|protected|private]");
        defaultConfigObject.setItems(new HashMap<>());
        return defaultConfigObject;
    }

    private String getConfigFilePath(String orgId) {
        return ObjectIdGenerator.validate(orgId) ? String.format("organization/%s/config.json", orgId) : null;
    }

    private String encode(String value) {

        if (value != null) {
            int length = value.length();
            return value.substring(0, 3) + "******" + value.substring(length - 3, length);
        } else
            return null;

    }

}
