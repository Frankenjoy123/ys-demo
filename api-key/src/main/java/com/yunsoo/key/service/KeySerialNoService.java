package com.yunsoo.key.service;

import com.yunsoo.key.dto.KeySerialNo;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-12-21
 * Descriptions:
 */
public interface KeySerialNoService {

    KeySerialNo getByOrgId(String orgId);

    List<KeySerialNo> getByOrgIdIn(List<String> orgIds);

    KeySerialNo save(KeySerialNo keySerialNo);

    void patchUpdateKeySerialNo(KeySerialNo keySerialNo);

    String getKeySerialNoPattern(String orgId, int count);

}
