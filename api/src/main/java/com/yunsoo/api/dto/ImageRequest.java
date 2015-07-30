package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/30
 * Descriptions:
 */
public class ImageRequest {

    @NotNull(message = "data must not be null")
    @JsonProperty("data")
    private String data;

    @JsonProperty("range2x1")
    private Range range2x1;

    @JsonProperty("range1x1")
    private Range range1x1;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Range getRange2x1() {
        return range2x1;
    }

    public void setRange2x1(Range range2x1) {
        this.range2x1 = range2x1;
    }

    public Range getRange1x1() {
        return range1x1;
    }

    public void setRange1x1(Range range1x1) {
        this.range1x1 = range1x1;
    }

    public static class Range {
        private int x;
        private int y;
        private int width;
        private int height;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

}
