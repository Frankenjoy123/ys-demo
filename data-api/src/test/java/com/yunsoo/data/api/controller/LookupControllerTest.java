package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.api.ControllerTestBase;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-02-16
 * Descriptions:
 */
public class LookupControllerTest extends ControllerTestBase {

    @Test
    public void test_getByFilter() {
        List<LookupObject> lookupObjects = dataAPIClient.get("lookup?type_code={type_code}&active={active}", new ParameterizedTypeReference<List<LookupObject>>() {
        }, "account_status", true);

        assert lookupObjects.size() > 0;
    }

    @Test
    public void test_getByTypeCode() {
        List<LookupObject> lookupObjects = dataAPIClient.get("lookup/{type_code}?active={active}", new ParameterizedTypeReference<List<LookupObject>>() {
        }, "account_status", true);

        assert lookupObjects.size() > 0;
    }

    @Test
    public void test_getTypeCode() {
        List<String> lookupObjects = dataAPIClient.get("lookup/typeCode", new ParameterizedTypeReference<List<String>>() {
        });

        assert lookupObjects.size() > 0;
    }

    @Test
    public void test_getByTypeCodeAndCode() {
        LookupObject lookupObject = dataAPIClient.get("lookup/{type_code}/{code}", LookupObject.class, "account_status", "activated");
        assert lookupObject != null;
    }

    @Test(expected = NotFoundException.class)
    public void test_getByTypeCodeAndCode_404() {
        dataAPIClient.get("lookup/{type_code}/{code}", LookupObject.class, "account_status", "xxx");
    }

    @Test
    public void test_put() {
        LookupObject lookupObject = dataAPIClient.get("lookup/{type_code}/{code}", LookupObject.class, "account_status", "activated");
        assert lookupObject != null;
        dataAPIClient.put("lookup/{type_code}/{code}",lookupObject, "account_status", "activated");
    }

}
