package com.yunsoo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * DataService Settings.
 * Created by Zhe on 2015/3/20.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "dataServiceSetting")
public class DataServiceSetting {
    //message and user status ID
    private Integer message_created_status_id;
    private Integer message_approved_status_id;
    private Integer message_delete_status_id;
    private Integer user_created_status_id;
    private Integer user_delete_status_id;

    public Integer getMessage_created_status_id() {
        return message_created_status_id;
    }

    public void setMessage_created_status_id(Integer message_created_status_id) {
        this.message_created_status_id = message_created_status_id;
    }

    public Integer getMessage_approved_status_id() {
        return message_approved_status_id;
    }

    public void setMessage_approved_status_id(Integer message_approved_status_id) {
        this.message_approved_status_id = message_approved_status_id;
    }

    public Integer getMessage_delete_status_id() {
        return message_delete_status_id;
    }

    public void setMessage_delete_status_id(Integer message_delete_status_id) {
        this.message_delete_status_id = message_delete_status_id;
    }

    public Integer getUser_created_status_id() {
        return user_created_status_id;
    }

    public void setUser_created_status_id(Integer user_created_status_id) {
        this.user_created_status_id = user_created_status_id;
    }

    public Integer getUser_delete_status_id() {
        return user_delete_status_id;
    }

    public void setUser_delete_status_id(Integer user_delete_status_id) {
        this.user_delete_status_id = user_delete_status_id;
    }
}
