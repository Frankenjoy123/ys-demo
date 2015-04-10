package com.yunsoo.data.api.factory;

import com.yunsoo.data.api.dto.ResultWrapper;

/**
 * Created by Zhe on 2015/2/5.
 * This class is the ResultWrapper factory.
 */
public class ResultFactory {

    public static ResultWrapper CreateResult(Boolean result) {
        ResultWrapper resultWrapper = new ResultWrapper();
        resultWrapper.setResult(String.valueOf(result));
        return resultWrapper;
    }

    public static ResultWrapper CreateResult(Integer result) {
        ResultWrapper resultWrapper = new ResultWrapper();
        resultWrapper.setResult(String.valueOf(result));
        return resultWrapper;
    }

    public static ResultWrapper CreateResult(Long result) {
        ResultWrapper resultWrapper = new ResultWrapper();
        resultWrapper.setResult(String.valueOf(result));
        return resultWrapper;
    }

    public static ResultWrapper CreateResult(Double result) {
        ResultWrapper resultWrapper = new ResultWrapper();
        resultWrapper.setResult(String.valueOf(result));
        return resultWrapper;
    }
}
