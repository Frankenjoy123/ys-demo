package com.yunsoo.auth.service;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dao.entity.ApplicationEntity;
import com.yunsoo.auth.dao.repository.ApplicationRepository;
import com.yunsoo.auth.dto.Application;
import com.yunsoo.common.web.client.Page;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-07-11
 * Descriptions:
 */
@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;


    public Application getById(String appId) {
        if (StringUtils.isEmpty(appId)) {
            return null;
        }
        return toApplication(applicationRepository.findOne(appId));
    }

    public Page<Application> getAll(Pageable pageable) {
        return PageUtils.convert(applicationRepository.findAll(pageable)).map(this::toApplication);
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
