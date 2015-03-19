package com.yunsoo.dataapi.dto;

/**
 * Created by Zhe on 2015/2/5.
 * This wrapper aims to encapsulate result so our API controller can return result in unified JSON format.
 */
public class ResultWrapper {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
