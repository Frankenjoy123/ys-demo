package com.yunsoo.api.payment;

import com.yunsoo.common.util.HashUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   Haitao
 * Created on:   2016/2/26
 * Descriptions:
 */
public final class AlipayParameters {

    private Map<String, String> parameters = new HashMap<>();
    private String key;

    /**
     * @param service Interface name，eg：create_direct_pay_by_user
     * @param partner partner ID
     */
    public AlipayParameters(String service, String partner, String key) {
        Assert.hasText(service, "service must not be null or empty");
        Assert.hasText(partner, "partner must not be null or empty");

        parameters.put(ParameterNames.SERVICE, service);
        parameters.put(ParameterNames.PARTNER, partner);
        this.key = key;

        //default parameters
        parameters.put(ParameterNames.INPUT_CHARSET, "utf-8");
        parameters.put(ParameterNames.SIGN_TYPE, SignType.MD5);
    }


    public AlipayParameters put(String name, String value) {
        Assert.hasText(name, "name must not be null or empty");
        parameters.put(name, value);
        return this;
    }

    public String get(String name) {
        Assert.hasText(name, "name must not be null or empty");
        return parameters.get(name);
    }

    /**
     * sign parameters before url encoding
     *
     * @return MD5 signed hex string
     */
    public String getSign() {
        String raw = join(getSortedNamesForSign(), parameters);
        try {
            return HashUtils.md5HexString(raw + key, parameters.get(ParameterNames.INPUT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(ParameterNames.INPUT_CHARSET + " invalid", e);
        }
    }

    public boolean verify() {
        return false;//todo
    }

    public Map<String, String> toMap() {
        parameters.put(ParameterNames.SIGN, getSign());
        Map<String, String> result = new HashMap<>();
        result.putAll(parameters);
        return result;
    }

    public String toQueryString() {
        parameters.put(ParameterNames.SIGN, getSign());
        return join(getSortedNames(), toMap());
    }


    private List<String> getSortedNames() {
        List<String> names = new ArrayList<>(parameters.keySet());
        names.sort(null);
        return names;
    }

    private List<String> getSortedNamesForSign() {
        return getSortedNames().stream()
                .filter(i -> !ParameterNames.SIGN_TYPE.equals(i) && !ParameterNames.SIGN.equals(i))
                .collect(Collectors.toList());
    }

    private String join(List<String> names, Map<String, String> paramsMap) {
        List<String> params = new ArrayList<>();
        for (String name : names) {
            String value = paramsMap.get(name);
            if (value != null) {
                params.add(name + "=" + value);
            }
        }
        return StringUtils.collectionToDelimitedString(params, "&");
    }

}