package com.yunsoo.common.data.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-06-23
 * Descriptions:
 */
public class ProductPackageMessage implements Serializable {

    public static final String PAYLOAD_TYPE = "product_package";

    @JsonProperty("task_file_id")
    private String taskFileId;

    @JsonProperty("continue_offset")
    private Integer continueOffset;

    @JsonProperty("total_time")
    private Long totalTime;

    public String getTaskFileId() {
        return taskFileId;
    }

    public void setTaskFileId(String taskFileId) {
        this.taskFileId = taskFileId;
    }

    public Integer getContinueOffset() {
        return continueOffset;
    }

    public void setContinueOffset(Integer continueOffset) {
        this.continueOffset = continueOffset;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }
}
