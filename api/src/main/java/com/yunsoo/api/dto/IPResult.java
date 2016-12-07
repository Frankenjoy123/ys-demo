package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 7/11/2016.
 */
public class IPResult {

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
