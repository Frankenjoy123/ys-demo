package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Dake Wang on 2016/2/4.
 *
 *
 * {
 *
 *     date:[...],
 *     data:{pv:[...], uv[...]}
 *
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanAnalysisReport {

    @JsonProperty("date")
    private String[] date;

    @JsonProperty("data")
    private PVUV data;


    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public PVUV getData() {
        return data;
    }

    public void setData(PVUV data) {
        this.data = data;
    }

    public final static class PVUV
    {
        @JsonProperty("pv")
        private int[] pv;

        @JsonProperty("uv")
        private int[] uv;

        @JsonProperty("first_scan")
        private int[] firstScan;


        public int[] getPv() {
            return pv;
        }

        public void setPv(int[] pv) {
            this.pv = pv;
        }

        public int[] getUv() {
            return uv;
        }

        public void setUv(int[] uv) {
            this.uv = uv;
        }

        public int[] getFirstScan() {
            return firstScan;
        }

        public void setFirstScan(int[] firstScan) {
            this.firstScan = firstScan;
        }
    }


}





