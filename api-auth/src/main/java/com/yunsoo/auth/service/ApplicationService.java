package com.yunsoo.auth.service;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dao.entity.ApplicationEntity;
import com.yunsoo.auth.dao.entity.ApplicationVersionEntity;
import com.yunsoo.auth.dao.repository.ApplicationRepository;
import com.yunsoo.auth.dao.repository.ApplicationVersionRepository;
import com.yunsoo.auth.dto.Application;
import com.yunsoo.auth.dto.ApplicationVersion;
import com.yunsoo.common.web.client.Page;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-11
 * Descriptions:
 */
@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationVersionRepository applicationVersionRepository;

    public Application getById(String appId) {
        if (StringUtils.isEmpty(appId)) {
            return null;
        }
        return toApplication(applicationRepository.findOne(appId));
    }

    public ApplicationVersion getCurrentAppVersion(String appId){
        if (StringUtils.isEmpty(appId)){
            return null;
        }
        return ApplicationVersionEntity.toApplicationVersion(applicationVersionRepository.query(appId));
    }

    public Page<Application> getList(String typeCode, Pageable pageable) {
        return PageUtils.convert(StringUtils.isEmpty(typeCode)
                ? applicationRepository.findAll(pageable)
                : applicationRepository.findByTypeCode(typeCode, pageable))
                .map(this::toApplication);
    }

    public List<Application> getListByIds(String typeCode, List<String> ids){
        return  applicationRepository.findByTypeCodeAndIdIn(typeCode, ids).stream().map((this::toApplication)).collect(Collectors.toList());
    }

    public Application create(Application application) {
        ApplicationEntity entity = new ApplicationEntity();
        entity.setName(application.getName());
        entity.setVersion(application.getVersion());
        entity.setStatusCode(application.getStatusCode());
        entity.setTypeCode(application.getTypeCode());
        entity.setDescription(application.getDescription());
        entity.setSystemVersion(application.getSystemVersion());
        entity.setPermanentTokenExpiresMinutes(application.getPermanentTokenExpiresMinutes());
        entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        entity.setCreatedDateTime(DateTime.now());
        return toApplication(applicationRepository.save(entity));
    }

    public void patchUpdate(Application application) {
        if (application == null || StringUtils.isEmpty(application.getId())) {
            return;
        }
        ApplicationEntity entity = applicationRepository.findOne(application.getId());
        if (entity != null) {
            if (application.getName() != null) entity.setName(application.getName());
            if (application.getVersion() != null) entity.setVersion(application.getVersion());
            if (application.getStatusCode() != null) entity.setStatusCode(application.getStatusCode());
            if (application.getTypeCode() != null) entity.setTypeCode(application.getTypeCode());
            if (application.getDescription() != null) entity.setDescription(application.getDescription());
            if (application.getSystemVersion() != null) entity.setSystemVersion(application.getSystemVersion());
            if (application.getPermanentTokenExpiresMinutes() != null)
                entity.setPermanentTokenExpiresMinutes(application.getPermanentTokenExpiresMinutes());
            entity.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setModifiedDateTime(DateTime.now());
            applicationRepository.save(entity);
        }
    }

    private Application toApplication(ApplicationEntity entity) {
        if (entity == null) {
            return null;
        }
        Application application = new Application();
        application.setId(entity.getId());
        application.setName(entity.getName());
        application.setVersion(entity.getVersion());
        application.setStatusCode(entity.getStatusCode());
        application.setTypeCode(entity.getTypeCode());
        application.setDescription(entity.getDescription());
        application.setSystemVersion(entity.getSystemVersion());
        application.setPermanentTokenExpiresMinutes(entity.getPermanentTokenExpiresMinutes());
        application.setCreatedAccountId(entity.getCreatedAccountId());
        application.setCreatedDateTime(entity.getCreatedDateTime());
        application.setModifiedAccountId(entity.getModifiedAccountId());
        application.setModifiedDateTime(entity.getModifiedDateTime());
        return application;
    }

}
