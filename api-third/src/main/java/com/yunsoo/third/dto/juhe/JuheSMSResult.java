package com.yunsoo.third.dto.juhe;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 6/13/2016.
 */
public class JuheSMSResult {

    private String reason;

    @JsonProperty("error_code")
    private int errorCode;

    private SMSObject result;

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

    public SMSObject getResult() {
        return result;
    }

    public void setResult(SMSObject result) {
        this.result = result;
    }

    public class SMSObject {
        private String count;
        private String fee;
        private String sid;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }
    }

}
