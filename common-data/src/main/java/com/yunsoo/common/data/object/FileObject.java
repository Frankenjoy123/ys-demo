package com.yunsoo.common.data.object;

/**
 * FileObject represent File hosted in AWS S3.
 * Created by Zhe on 2015/3/2.
 */
public class FileObject {
    private byte[] data;
    private String name;
    private String suffix;
    private String contentType;
    private Long length;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}
