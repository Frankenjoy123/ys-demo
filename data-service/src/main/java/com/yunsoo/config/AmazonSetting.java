package com.yunsoo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Zhe on 2015/3/20.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "AmazonSettings")
public class AmazonSetting {

    private String s3_basebucket;
    private String s3_userbaseurl;
    private String s3_productbaseurl;
    private String s3_product_default_image_url;
    private String s3_product_key_batch_path;
    private String s3_message_image_url;
    private String debug;

    public String getS3_basebucket() {
        return s3_basebucket;
    }

    public void setS3_basebucket(String s3_basebucket) {
        this.s3_basebucket = s3_basebucket;
    }

    public String getS3_userbaseurl() {
        return s3_userbaseurl;
    }

    public void setS3_userbaseurl(String s3_userbaseurl) {
        this.s3_userbaseurl = s3_userbaseurl;
    }

    public String getS3_productbaseurl() {
        return s3_productbaseurl;
    }

    public void setS3_productbaseurl(String s3_productbaseurl) {
        this.s3_productbaseurl = s3_productbaseurl;
    }

    public String getS3_product_default_image_url() {
        return s3_product_default_image_url;
    }

    public void setS3_product_default_image_url(String s3_product_default_image_url) {
        this.s3_product_default_image_url = s3_product_default_image_url;
    }

    public String getS3_product_key_batch_path() {
        return s3_product_key_batch_path;
    }

    public void setS3_product_key_batch_path(String s3_product_key_batch_path) {
        this.s3_product_key_batch_path = s3_product_key_batch_path;
    }

    public String getS3_message_image_url() {
        return s3_message_image_url;
    }

    public void setS3_message_image_url(String s3_message_image_url) {
        this.s3_message_image_url = s3_message_image_url;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}
