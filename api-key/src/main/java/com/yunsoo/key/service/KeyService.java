package com.yunsoo.key.service;

import com.yunsoo.key.dto.Key;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public interface KeyService {

    Key get(String key);

    void setDisabled(String key, Boolean disable);

}
