package com.yunsoo.dataapi.dto;

/**
 * Created by Zhe on 2015/2/12.
 */
public class LogisticsDto {
    private String time;
    private String location;
    private String digest;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
