package com.yunsoo.processor.domain;

import com.yunsoo.common.web.client.Page;
import com.yunsoo.processor.config.ProcessorConfigProperties;
import com.yunsoo.processor.dao.entity.LogEntity;
import com.yunsoo.processor.dao.repository.LogRepository;
import com.yunsoo.processor.domain.object.LogObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-04-29
 * Descriptions:
 */
@Component
public class LogDomain {

    private static final String LEVEL_INFO = "info";
    private static final String LEVEL_WARNING = "warning";
    private static final String LEVEL_ERROR = "error";

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ProcessorConfigProperties config;

    public Page<LogObject> getLogByFilter(String eventName,
                                          String level,
                                          String identifier,
                                          String identifierName,
                                          DateTime createdDateTimeStart,
                                          DateTime createdDateTimeEnd,
                                          Pageable pageable) {
        org.springframework.data.domain.Page<LogEntity> entities = logRepository.findByFilter(
                eventName,
                level,
                identifier,
                identifierName,
                createdDateTimeStart,
                createdDateTimeEnd,
                pageable);
        return new Page<>(
                entities.getContent().stream().map(this::toLogObject).collect(Collectors.toList()),
                entities.getNumber(),
                entities.getTotalPages());
    }

    public void logInfo(String eventName, String details, String identifier, String identifierName) {
        saveLog(eventName, LEVEL_INFO, details, identifier, identifierName);
    }

    public void logWarning(String eventName, String details, String identifier, String identifierName) {
        saveLog(eventName, LEVEL_WARNING, details, identifier, identifierName);
    }

    public void logError(String eventName, String details, String identifier, String identifierName) {
        saveLog(eventName, LEVEL_ERROR, details, identifier, identifierName);
    }

    private void saveLog(String eventName, String level, String details, String identifier, String identifierName) {
        LogObject logObject = new LogObject();
        logObject.setEventName(eventName);
        logObject.setLevel(level);
        if (identifier != null && identifierName != null) {
            logObject.setIdentifier(identifier);
            logObject.setIdentifierName(identifierName);
        }
        logObject.setDetails(details);
        saveLog(logObject);
    }

    private void saveLog(LogObject logObject) {
        logObject.setId(null);
        logObject.setProcessorName(config.getName());
        logObject.setCreatedDateTime(DateTime.now());
        try {
            logRepository.save(toLogEntity(logObject));
        } catch (Exception e) {
            log.error("save log failed", e);
        }
    }

    private LogEntity toLogEntity(LogObject obj) {
        if (obj == null) {
            return null;
        }
        LogEntity entity = new LogEntity();
        entity.setId(obj.getId());
        entity.setProcessorName(obj.getProcessorName());
        entity.setEventName(obj.getEventName());
        entity.setLevel(obj.getLevel());
        entity.setIdentifier(obj.getIdentifier());
        entity.setIdentifierName(obj.getIdentifierName());
        entity.setDetails(obj.getDetails());
        entity.setCreatedDateTime(obj.getCreatedDateTime());
        return entity;
    }

    private LogObject toLogObject(LogEntity entity) {
        if (entity == null) {
            return null;
        }
        LogObject obj = new LogObject();
        obj.setId(entity.getId());
        obj.setProcessorName(entity.getProcessorName());
        obj.setEventName(entity.getEventName());
        obj.setLevel(entity.getLevel());
        obj.setIdentifier(entity.getIdentifier());
        obj.setIdentifierName(entity.getIdentifierName());
        obj.setDetails(entity.getDetails());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        return obj;
    }
}
