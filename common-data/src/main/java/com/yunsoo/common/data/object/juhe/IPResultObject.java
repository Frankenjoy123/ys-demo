package com.yunsoo.common.data.object.juhe;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 7/11/2016.
 */
public class IPResultObject {
    private String reason;

    @JsonProperty("error_code")
    private int errorCode;

    private IPObject result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public IPObject getResult() {
        return result;
    }

    public void setResult(IPObject result) {
        this.result = result;
    }

    public class IPObject {
        private String area;
        private String location;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
