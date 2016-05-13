package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dake Wang on 2016/5/12.
 * TODO 定义地域报表结构
 * <p>
 * <p>
 * {
 * *
 * <p>
 * data:[
 * {name:xxx, value:12, city[]},...
 * }]
 * <p>
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketUserAnalysisReport {

    @JsonProperty("location_report")
    private MarketUserLocationReport locationReport;

    @JsonProperty("gender_report")
    private MarketUserCategoryAndValueReport genderReport;

    @JsonProperty("device_report")
    private MarketUserCategoryAndValueReport deviceReport;

    @JsonProperty("usage_report")
    private MarketUserCategoryAndValueReport usageReport;

    @JsonProperty("area_report")
    private MarketUserCategoryAndValueReport areaReport;


    public MarketUserLocationReport getLocationReport() {
        return locationReport;
    }

    public void setLocationReport(MarketUserLocationReport locationReport) {
        this.locationReport = locationReport;
    }

    public MarketUserCategoryAndValueReport getGenderReport() {
        return genderReport;
    }

    public void setGenderReport(MarketUserCategoryAndValueReport genderReport) {
        this.genderReport = genderReport;
    }

    public MarketUserCategoryAndValueReport getDeviceReport() {
        return deviceReport;
    }

    public void setDeviceReport(MarketUserCategoryAndValueReport deviceReport) {
        this.deviceReport = deviceReport;
    }

    public MarketUserCategoryAndValueReport getUsageReport() {
        return usageReport;
    }

    public void setUsageReport(MarketUserCategoryAndValueReport usageReport) {
        this.usageReport = usageReport;
    }

    public MarketUserCategoryAndValueReport getAreaReport() {
        return areaReport;
    }

    public void setAreaReport(MarketUserCategoryAndValueReport areaReport) {
        this.areaReport = areaReport;
    }
}





