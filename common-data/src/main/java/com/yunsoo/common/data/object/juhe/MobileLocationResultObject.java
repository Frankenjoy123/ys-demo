package com.yunsoo.common.data.object.juhe;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 6/14/2016.
 */
public class MobileLocationResultObject {
    private String reason;

    @JsonProperty("error_code")
    private int errorCode;

    private LocationResultObject result;

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

    public LocationResultObject getResult() {
        return result;
    }

    public void setResult(LocationResultObject result) {
        this.result = result;
    }

    public class LocationResultObject{
        private String province;
        private String city;
        @JsonProperty("areacode")
        private String areaCode;
        private String zip;
        private String company;
        private String card;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }
    }
}
