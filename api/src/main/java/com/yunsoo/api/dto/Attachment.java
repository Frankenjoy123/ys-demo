package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.AttachmentObject;
import org.joda.time.DateTime;

/**
 * Created by yan on 3/22/2016.
 */
public class Attachment {
    @JsonProperty("id")
    private String id;

    @JsonProperty("original_file_name")
    private String originalFileName;

    @JsonProperty("s3_file_name")
    private String s3FileName;

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

    public String getS3FileName() {
        return s3FileName;
    }

    public void setS3FileName(String s3FileName) {
        this.s3FileName = s3FileName;
    }

    public Attachment(){}

    public Attachment(AttachmentObject attachment){
        if(attachment != null) {
            setId(attachment.getId());
            setOriginalFileName(attachment.getOriginalFileName());
            setS3FileName(attachment.getS3FileName());
            setCreatedDateTime(attachment.getCreatedDateTime());
            setModifiedDateTime(attachment.getModifiedDateTime());
        }

    }

    public AttachmentObject toAttachmentObject(Attachment attachment){
        if(attachment == null)
            return null;

        AttachmentObject attachmentObj = new AttachmentObject();
        attachmentObj.setId(attachment.getId());
        attachmentObj.setOriginalFileName(attachment.getOriginalFileName());
        attachmentObj.setS3FileName(attachment.getS3FileName());
        attachmentObj.setCreatedDateTime(attachment.getCreatedDateTime());
        attachmentObj.setModifiedDateTime(attachment.getModifiedDateTime());
        return attachmentObj;

    }
}