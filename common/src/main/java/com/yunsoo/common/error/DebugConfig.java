package com.yunsoo.common.error;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public class DebugConfig {

    private boolean debugEnabled;

    public DebugConfig(boolean debugEnabled){
        this.debugEnabled = debugEnabled;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}
