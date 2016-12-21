package com.yunsoo.key.service.impl;

import com.yunsoo.common.util.SerialNoGenerator;
import com.yunsoo.key.dao.entity.KeySerialNoEntity;
import com.yunsoo.key.dao.repository.KeySerialNoRepository;
import com.yunsoo.key.dto.KeySerialNo;
import com.yunsoo.key.service.KeySerialNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-12-21
 * Descriptions:
 */
@Service
public class KeySerialNoServiceImpl implements KeySerialNoService {

    @Autowired
    private KeySerialNoRepository keySerialNoRepository;

    @Override
    public KeySerialNo getByOrgId(String orgId) {
        return toKeySerialNo(findByOrgId(orgId));
    }

    @Override
    public List<KeySerialNo> getByOrgIdIn(List<String> orgIds) {
        return keySerialNoRepository.findByOrgIdIn(orgIds).stream().map(this::toKeySerialNo).collect(Collectors.toList());
    }

    @Override
    public KeySerialNo save(KeySerialNo keySerialNo) {
        String orgId = keySerialNo.getOrgId();
        Integer serialLength = keySerialNo.getSerialLength();
        Integer offset = keySerialNo.getOffset();
        if (StringUtils.isEmpty(orgId)
                || serialLength == null
                || offset == null
                || serialLength < 0
                || offset < 0
                || offset > Math.pow(10, serialLength) - 1) {
            return null;
        }
        KeySerialNoEntity entity = findByOrgId(orgId);
        if (entity == null) {
            entity = new KeySerialNoEntity();
        }
        entity.setOrgId(orgId);
        entity.setSerialLength(serialLength);
        entity.setOffset(offset);
        entity.setPrefix(keySerialNo.getPrefix());
        entity.setSuffix(keySerialNo.getSuffix());
        return toKeySerialNo(keySerialNoRepository.save(entity));
    }

    @Override
    public void patchUpdateKeySerialNo(KeySerialNo keySerialNo) {
        String orgId = keySerialNo.getOrgId();
        Integer serialLength = keySerialNo.getSerialLength();
        Integer offset = keySerialNo.getOffset();
        String prefix = keySerialNo.getPrefix();
        String suffix = keySerialNo.getSuffix();
        KeySerialNoEntity entity = findByOrgId(orgId);
        if (entity == null) {
            return;
        }
        if (serialLength != null && serialLength > 0) {
            entity.setSerialLength(serialLength);
        } else {
            serialLength = entity.getSerialLength();
        }
        if (offset != null && offset > 0) {
            entity.setOffset(offset);
        } else {
            offset = entity.getOffset();
        }
        if (offset > Math.pow(10, serialLength) - 1) {
            return;
        }
        if (prefix != null) {
            entity.setPrefix(prefix);
        }
        if (suffix != null) {
            entity.setSuffix(suffix);
        }
        keySerialNoRepository.save(entity);
    }

    @Transactional
    @Override
    public String getKeySerialNoPattern(String orgId, int count) {
        KeySerialNoEntity entity = findByOrgId(orgId);
        if (entity == null) {
            return null;
        }
        try {
            int offset = entity.getOffset();
            int serialLength = entity.getSerialLength();
            int end = offset + count - 1;
            if (end > Math.pow(10, serialLength) - 1) {
                serialLength++;
                entity.setSerialLength(serialLength);
            }
            SerialNoGenerator serialNoGenerator = new SerialNoGenerator(
                    entity.getPrefix(),
                    entity.getSuffix(),
                    serialLength,
                    offset,
                    end);
            entity.setOffset(end + 1);
            return serialNoGenerator.getPattern();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private KeySerialNoEntity findByOrgId(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return null;
        }
        List<KeySerialNoEntity> entities = keySerialNoRepository.findByOrgId(orgId);
        if (entities.size() == 0) {
            return null;
        }
        return entities.get(0);
    }

    private KeySerialNo toKeySerialNo(KeySerialNoEntity entity) {
        if (entity == null) {
            return null;
        }
        KeySerialNo obj = new KeySerialNo();
        obj.setOrgId(entity.getOrgId());
        obj.setSerialLength(entity.getSerialLength());
        obj.setOffset(entity.getOffset());
        obj.setPrefix(entity.getPrefix());
        obj.setSuffix(entity.getSuffix());
        return obj;
    }
}
