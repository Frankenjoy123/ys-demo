package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by admin on 2015/7/27.
 */
public class ProductBaseImage {
    @JsonProperty("productBaseId")
    private String productBaseId;

    @JsonProperty("imageContent")
    private String imageContent;

    @JsonProperty("imageRect")
    private Image imageRect;

    @JsonProperty("imageSquare")
    private Image imageSquare;

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public Image getImageRect() {
        return imageRect;
    }

    public void setImageRect(Image imageRect) {
        this.imageRect = imageRect;
    }

    public Image getImageSquare() {
        return imageSquare;
    }

    public void setImageSquare(Image imageSquare) {
        this.imageSquare = imageSquare;
    }

    public static class Image {
        private int initX;
        private int initY;
        private int width;
        private int height;

        public int getInitX() {
            return initX;
        }

        public void setInitX(int initX) {
            this.initX = initX;
        }

        public int getInitY() {
            return initY;
        }

        public void setInitY(int initY) {
            this.initY = initY;
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
