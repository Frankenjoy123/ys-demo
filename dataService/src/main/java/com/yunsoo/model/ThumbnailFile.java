package com.yunsoo.model;

/**
 * Object represent user's thumbnail
 * Created by Zhe on 2015/3/2.
 */
public class ThumbnailFile {
    private byte[] thumbnailData;
    private String name;
    private String suffix;
    private String contentType;

    public byte[] getThumbnailData() {
        return thumbnailData;
    }

    public void setThumbnailData(byte[] thumbnailData) {
        this.thumbnailData = thumbnailData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
