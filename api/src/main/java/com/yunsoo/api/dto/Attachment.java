package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:   yan
 * Created on:   3/22/2016
 * Descriptions:
 */
public class Attachment {

    @JsonProperty("id")
    private String id;

    @JsonProperty("original_file_name")
    private String originalFileName;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }


    public Attachment() {
    }

    public Attachment(AttachmentObject obj) {
        if (obj != null) {
            setId(obj.getId());
            setOriginalFileName(obj.getOriginalFileName());
            setCreatedDateTime(obj.getCreatedDateTime());
            setModifiedDateTime(obj.getModifiedDateTime());
        }

    }

    public AttachmentObject toAttachmentObject(Attachment attachment) {
        if (attachment == null) {
            return null;
        }
        AttachmentObject obj = new AttachmentObject();
        obj.setId(attachment.getId());
        obj.setOriginalFileName(attachment.getOriginalFileName());
        obj.setCreatedDateTime(attachment.getCreatedDateTime());
        obj.setModifiedDateTime(attachment.getModifiedDateTime());
        return obj;
    }
}
