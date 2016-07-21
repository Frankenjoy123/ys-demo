package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.client.DataApiClient1;
import com.yunsoo.common.data.object.DomainDirectoryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-04-12
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class DomainDirectoryDomain {

    @Autowired
    private DataApiClient1 dataApiClient;

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).DOMAIN_DIRECTORY.toString(), 'map')")
    public Map<String, DomainDirectoryObject> getDomainDirectoryObjectMap() {
        Map<String, DomainDirectoryObject> map = new HashMap<>();
        getDomainDirectoryObjectList().stream().forEach(o -> {
            map.put(o.getName(), o);
        });
        return map;
    }

    public List<DomainDirectoryObject> getDomainDirectoryObjectList() {
        return dataApiClient.get("domainDirectory", new ParameterizedTypeReference<List<DomainDirectoryObject>>() {
        });
    }

    @CacheEvict(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).DOMAIN_DIRECTORY.toString(), 'map')")
    public void putDomainDirectoryObject(DomainDirectoryObject domainDirectoryObject) {
        if (StringUtils.hasText(domainDirectoryObject.getName())) {
            dataApiClient.put("domainDirectory?name={name}", domainDirectoryObject, domainDirectoryObject.getName());
        }
    }

    @CacheEvict(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).DOMAIN_DIRECTORY.toString(), 'map')")
    public void deleteDomainDirectoryObjectByName(String name) {
        if (StringUtils.hasText(name)) {
            dataApiClient.delete("domainDirectory?name={name}", name);
        }
    }

    public DomainDirectoryObject search(Map<String, DomainDirectoryObject> map, String domainName) {
        if (domainName == null) {
            return null;
        }
        DomainDirectoryObject domain = map.get(domainName);
        if (domain != null) {
            return domain;
        }
        String[] names = domainName.split("\\.");
        for (int i = 0; i < names.length; i++) {
            StringBuilder sb = new StringBuilder("*");
            for (int j = i + 1; j < names.length; j++) {
                sb.append(".").append(names[j]);
            }
            domain = map.get(sb.toString());
            if (domain != null) {
                return domain;
            }
        }
        return null;
    }

}
