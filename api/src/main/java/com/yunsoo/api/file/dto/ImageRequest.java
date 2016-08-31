package com.yunsoo.api.file.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by:   Lijian
 * Created on:   2016-01-22
 * Descriptions:
 */
public class ImageRequest {

    @NotBlank(message = "data must not be null or empty")
    @JsonProperty("data")
    private String data; // prefix: data:image/png;base64,

    @JsonProperty("options")
    private Options options;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public static class Options {

        private Integer x;
        private Integer y;
        private Integer width;
        private Integer height;

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getY() {
            return y;
        }

        public void setY(Integer y) {
            this.y = y;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }

}
