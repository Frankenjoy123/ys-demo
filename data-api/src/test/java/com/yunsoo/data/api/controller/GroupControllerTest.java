package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.api.Application;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class GroupControllerTest {

    @Value("${local.server.port}")
    private int port;

    private RestClient dataAPIClient;

    @Before
    public void before() {
        dataAPIClient = new RestClient("http://localhost:" + port, new RestResponseErrorHandler());
    }

    @Test
    public void test_All() {
        GroupObject groupObject = new GroupObject();
        groupObject.setName("[UnitTest]测试组名");
        groupObject.setDescription("[UnitTest]测试描述");
        groupObject.setOrgId("2k0r1l55i2rs5544wz5"); //云溯科技
        groupObject.setCreatedAccountId("2kadmvn8uh248k5k7wa"); //Lijian
        groupObject.setCreatedDateTime(DateTime.now());
        groupObject = dataAPIClient.post("group", groupObject, GroupObject.class);
        String id = groupObject.getId();
        System.out.println("group created: [id: " + id + "]");

        groupObject = dataAPIClient.get("group/{id}", GroupObject.class, groupObject.getId());

        List<GroupObject> groupObjects = dataAPIClient.get("group?org_id={orgId}", new ParameterizedTypeReference<List<GroupObject>>() {
        }, groupObject.getOrgId());
        assert groupObjects.stream().filter(o -> o.getId().equals(id)).count() > 0 : "get by org_id failed";

        groupObject.setName("[UnitTest]测试组名1");
        dataAPIClient.patch("group/{id}", groupObject, id);
        groupObject = dataAPIClient.get("group/{id}", GroupObject.class, id);
        assert groupObject.getName().equals("[UnitTest]测试组名1") : "patch update failed";

        dataAPIClient.delete("group/{id}", id);

        try {
            dataAPIClient.get("group/{id}", GroupObject.class, id);
            assert false : "delete failed";
        } catch (NotFoundException ex) {
            System.out.println("group deleted: [id: " + id + "]");
        }

    }

}
