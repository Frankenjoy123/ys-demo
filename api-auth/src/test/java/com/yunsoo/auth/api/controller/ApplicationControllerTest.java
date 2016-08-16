package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.Application;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by min on 8/2/16.
 */
public class ApplicationControllerTest extends TestBase {

    private static List<String> appIds;
    private static String appId;

    @Before
    public void createApplications() {

        if (appIds == null) {
            appIds = batchCreateApp();
            appId = appIds.get(2);
        }
    }

    private List<String> batchCreateApp() {
        return IntStream.range(0, 4).parallel().mapToObj(i -> {
            return restClient.postAsync("application", createApp("TestApp" + i), Application.class);
        }).map(f -> {
                    try {
                        Application a = f.get();
                        System.out.println(a + " " + a.getName());
                        return a.getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        ).collect(Collectors.toList());
    }

    private Application getById(String id) {
        return restClient.get("application/{id}", Application.class, id);
    }

    private Application createApp(String name) {
        Application app = new Application();
        app.setName(name);
        app.setDescription("Test Application is a great company!");
        app.setCreatedDateTime(DateTime.now());
        app.setTypeCode("Android app");
        app.setVersion("Version 1.0");
        return app;
    }

    @Test
    public void testGetById() throws Exception {
        Application application = getById(appId);
        assertEquals(application.getName(), "TestApp2");
    }

    @Test(expected = NotFoundException.class)
    public void testGetById_404_notExistedId() {
        getById(appId + "xx");
    }

    @Test(expected = HttpMessageNotReadableException.class)
    public void testGetById_404_emptyId() {
        getById("");
    }

    @Test(expected = NotFoundException.class)
    public void testGetById_404_idSubString() {
        getById(appId.substring(0, appId.length() - 2));
    }

    @Test
    public void testGetList() throws Exception {
        List<Application> list = restClient.get("application", new ParameterizedTypeReference<List<Application>>() {
        });
        assert list.size() >= 4;
    }

    @Test
    public void testGetList_withPage() throws Exception {
        restClient.postAsync("application", createApp("TestApp"), Application.class);
        Pageable pageable = new PageRequest(0, 5, Sort.Direction.DESC, "createdDateTime");
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();
        List<Application> list = restClient.get("application"+query, new ParameterizedTypeReference<List<Application>>() {
        });
        assert list.size() == 5;
        assertEquals(list.get(0).getName(), "TestApp");
    }

    @Test
    public void testGetList_withPage_orderByName() throws Exception {
        Pageable pageable = new PageRequest(1, 2, Sort.Direction.DESC, "name");
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();
        List<Application> list = restClient.get("application"+query, new ParameterizedTypeReference<List<Application>>() {
        });
        assert list.size() == 2;
        assertEquals(list.get(0).getName(), "TestApp1");
    }

    @Test
    public void testGetList_withPage_wrongPageNumber() throws Exception {
        Pageable pageable = new PageRequest(3, 4, Sort.Direction.DESC, "name");
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();
        List<Application> list = restClient.get("application"+query, new ParameterizedTypeReference<List<Application>>() {
        });
        assert list.size() == 0;
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_emptyTypeCode() throws Exception {
        Application app = createApp("TestApp");
        app.setTypeCode("");
        restClient.post("application", app, Application.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_nullTypeCode() throws Exception {
        Application app = createApp("TestApp");
        app.setTypeCode(null);
        restClient.post("application", app, Application.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_emptyName() throws Exception {
        Application app = createApp("TestApp");
        app.setName("");
        restClient.post("application", app, Application.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_nullName() throws Exception {
        Application app = createApp("TestApp");
        app.setName(null);
        restClient.post("application", app, Application.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_emptyVersion() throws Exception {
        Application app = createApp("TestApp");
        app.setVersion("");
        restClient.post("application", app, Application.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_nullVersion() throws Exception {
        Application app = createApp("TestApp");
        app.setVersion(null);
        restClient.post("application", app, Application.class);
    }

    @Test
    public void testPatchUpdate_nothingChanged() throws Exception {
        Application app = getById(appId);
        restClient.patch("application/{id}", app, appId);
        assertEquals(getById(appId).getTypeCode(), "Android app");
    }

    @Test
    public void testPatchUpdate_typeCode() throws Exception {
        Application app = getById(appId);
        assertEquals(app.getTypeCode(), "Android app");
        app.setTypeCode("Android app 123");
        restClient.patch("application/{id}", app, appId);
        assertEquals(getById(appId).getTypeCode(), "Android app 123");
    }

    @Test
    public void testPatchUpdate_nullTypeCode() throws Exception {
        Application app = getById(appId);
        assertEquals(app.getTypeCode(), "Android app");
        app.setTypeCode(null);
        restClient.patch("application/{id}", app, appId);
        assertEquals(getById(appId).getTypeCode(), "Android app");
    }

    @Test
    public void testPatchUpdate_name() throws Exception {
        assertEquals(getById(appIds.get(3)).getName(), "TestApp3");
        Application app = createApp("forPatchUpdate");
        restClient.patch("application/{id}", app, appId);
        app = getById(appId);
        assertEquals(app.getName(), "forPatchUpdate");
    }

    @Test
    public void testPatchUpdate_nullName() throws Exception {
        Application app = createApp(null);
        restClient.patch("application/{id}", app, appId);
        app = getById(appId);
        assertEquals(app.getName(), "TestApp2");
    }

    @Test
    public void testPatchUpdate_version() throws Exception {
        Application app = getById(appId);
        assertEquals(app.getVersion(), "Version 1.0");
        app.setVersion("Version 2.0");
        restClient.patch("application/{id}", app, appId);
        assertEquals(getById(appId).getVersion(), "Version 2.0");
    }

    @Test
    public void testPatchUpdate_nullVersion() throws Exception {
        Application app = getById(appId);
        assertEquals(app.getVersion(), "Version 1.0");
        app.setVersion(null);
        restClient.patch("application/{id}", app, appId);
        assertEquals(getById(appId).getVersion(), "Version 1.0");
    }

    @Test
    public void testPatchUpdate_createdAccountId() throws Exception {
        Application app = getById(appId);
        String accountId = app.getCreatedAccountId();
        app.setCreatedAccountId("xxx");
        restClient.patch("application/{id}", app, appId);
        assertEquals(getById(appId).getCreatedAccountId(), accountId);
    }

    @Test
    public void testPatchUpdate_nullCreatedAccountId() throws Exception {
        Application app = getById(appId);
        String accountId = app.getCreatedAccountId();
        app.setCreatedAccountId(null);
        restClient.patch("application/{id}", app, appId);
        assertEquals(getById(appId).getCreatedAccountId(), accountId);
    }

    @Test
    public void testPatchUpdate_nullModifiedDateTime() throws Exception {
        Application app = getById(appId);
        System.out.println("Modified time is "+app.getModifiedDateTime());
        app.setModifiedDateTime(null);
        restClient.patch("application/{id}", app, appId);
        System.out.println("Modified time after update is " + getById(appId).getModifiedDateTime());
    }
}