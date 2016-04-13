package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.common.data.object.DomainDirectoryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-04-12
 * Descriptions:
 */
@Component
public class DomainDirectoryDomain {

    @Autowired
    private DataAPIClient dataAPIClient;

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).DOMAIN_DIRECTORY.toString(), 'map')")
    public Map<String, DomainDirectoryObject> getDomainDirectoryObjects() {
        Map<String, DomainDirectoryObject> map = new HashMap<>();
        List<DomainDirectoryObject> domainDirectoryObjects = dataAPIClient.get("domainDirectory", new ParameterizedTypeReference<List<DomainDirectoryObject>>() {
        });
        domainDirectoryObjects.stream().forEach(o -> {
            map.put(o.getName(), o);
        });
        return map;
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
