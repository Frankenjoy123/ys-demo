package com.yunsoo.data.service.service.contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Hope on 2015/3/7.
 */
public class PackageBoundContract {
    private List<String> keys;
    private long operator;
    private String packageKey;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime created_date;

    public PackageBoundContract() {
    }

    public PackageBoundContract(String packageKey, List<String> keys, long operator, DateTime created_date) {
        this.packageKey = packageKey;
        this.keys = keys;
        this.operator = operator;
        this.created_date = created_date;
    }

    public String getPackageKey() {
        return packageKey;
    }

    public List<String> getKeys() {
        return keys;
    }

    public long getOperator() {
        return operator;
    }

    public void setPackageKey(String packageKey) {
        this.packageKey = packageKey;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public DateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(DateTime created_date) {
        this.created_date = created_date;
    }
}
