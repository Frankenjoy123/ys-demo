package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Application;
import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.dto.*;
import com.yunsoo.api.rabbit.file.service.FileService;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.web.client.AsyncRestClient;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by min on 8/26/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebIntegrationTest("server.port=0")
public class WebScanControllerTest {


    @Value("${local.server.port}")
    public int port;

    @Autowired
    FileService fileService;

    protected static AsyncRestClient restClient;


    protected static final String filePath = "organization/2k0r1l55i2rs5544wz5/product_key_batch/2msavp1xsq3z1o50cbo/keys.pks";
    protected static final String marketingId = "2msb69lkn0qotkzm3ay";
    protected static final String iPhone =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";
    protected static final String Android =
            "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36";

    protected String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName().split("\\..")[0];
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> readProductKeyFile() {
        try {
            List<String> result = new ArrayList<>();
            ResourceInputStream resourceInputStream = fileService.getFile(filePath);
            byte[] content = YSFile.read(resourceInputStream).getContent();
            String contentStr = new String(content, StandardCharsets.UTF_8);
            String[] lines = contentStr.split("\r\n");
            for (String line : lines) {
                if (line.length() > 0) {
                    result.add(Arrays.asList(StringUtils.commaDelimitedListToStringArray(line)).get(0));
                }
            }
            return result.subList(50, 54);
        } catch (NotFoundException | IOException ignored) {
        }
        return null;
    }

    @Before
    public void initRestClient() {
        if (restClient == null) {
            System.out.println("initializing restClient");
            restClient = new AsyncRestClient("http://localhost:" + port);
        }
    }

    private Boolean setHeaders() {
        Random random = new Random();
        String agent = random.nextBoolean() ? iPhone : Android;
        restClient.setPreRequestCallback(request -> {
            HttpHeaders httpHeaders = request.getHeaders();
            httpHeaders.set(com.yunsoo.common.web.Constants.HttpHeaderName.APP_ID, "AuthUnitTest");
            httpHeaders.set(com.yunsoo.common.web.Constants.HttpHeaderName.DEVICE_ID, getHostName());
            httpHeaders.set("User-Agent",agent);
        });
        return agent.equals(Android);
    }

    @Test
    public void testPostKeyScan() throws Exception {

        List<String> productKeys = readProductKeyFile();
        Map<String, Integer> map = new HashMap<>();

        productKeys.stream().forEach(productKey -> {
            WebScanRequest request = new WebScanRequest();
            request.setUserId(Constants.Ids.ANONYMOUS_USER_ID);
            request.setAddress("hang zhou");

            Boolean isAndroid = setHeaders();

            WebScanResponse.ScanRecord scanRecord = restClient.post("webScan/{0}", request, WebScanResponse.ScanRecord.class, productKey);
            String scanRecordId = scanRecord.getId();
            String ysId = scanRecord.getYsid();
            String productBaseId = scanRecord.getProductBaseId();

            MktDraw mktDraw = new MktDraw();
            MktDrawRecord drawRecord = new MktDrawRecord();
            MktDrawPrize drawPrize = new MktDrawPrize();

            drawRecord.setUserId(Constants.Ids.ANONYMOUS_USER_ID);
            drawRecord.setYsid(ysId);
            drawRecord.setProductBaseId(productBaseId);
            drawRecord.setProductKey(productKey);
            drawRecord.setScanRecordId(scanRecordId);
            drawRecord.setMarketingId(marketingId);

            drawPrize.setProductKey(productKey);
            drawPrize.setYsid(ysId);
            drawPrize.setScanRecordId(scanRecordId);
            drawPrize.setMarketingId(marketingId);

            mktDraw.setPrize(drawPrize);
            mktDraw.setRecord(drawRecord);

            MktDrawRule rule = restClient.post("marketing/drawPrize/{id}/random", mktDraw, MktDrawRule.class, marketingId);
            String ruleId = (isAndroid? "Android" : "iOS") + "-" + rule.getId();
            System.out.println("rule id is " + rule.getId());

            map.put(ruleId, map.getOrDefault(ruleId, 0) + 1);
        });

        StringBuilder stringBuilder = new StringBuilder();
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH-mm-ss") ;

        try (FileOutputStream fos = new FileOutputStream(dateFormat.format(date) + ".log");
             BufferedOutputStream bf = new BufferedOutputStream(fos)) {
            map.forEach((key, value) -> stringBuilder.append(key + ":" + value + "\n"));
            bf.write(stringBuilder.toString().getBytes("utf-8"));
            bf.flush();
        } catch (Exception e) {
            System.out.println("save drawResult failed at" + e.getLocalizedMessage());

        }
        System.out.println("result is: \n" + stringBuilder.toString());


    }

}