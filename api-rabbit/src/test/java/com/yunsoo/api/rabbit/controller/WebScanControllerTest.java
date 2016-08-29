package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Application;
import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.dto.MktDraw;
import com.yunsoo.api.rabbit.dto.MktDrawRule;
import com.yunsoo.api.rabbit.dto.WebScanRequest;
import com.yunsoo.api.rabbit.dto.WebScanResponse;
import com.yunsoo.api.rabbit.file.service.FileService;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.web.client.AsyncRestClient;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
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
            return result.subList(260, 270);
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

    @SuppressWarnings("unchecked")
    @Test
    public void testPostKeyScan() throws Exception {

        List<String> productKeys = readProductKeyFile();
        List<String> prizedKeys = new ArrayList<>();

        Map<String, HashMap> map = new HashMap<>();

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

            mktDraw.setUserId(Constants.Ids.ANONYMOUS_USER_ID);
            mktDraw.setYsId(ysId);
            mktDraw.setProductBaseId(productBaseId);
            mktDraw.setProductKey(productKey);
            mktDraw.setScanRecordId(scanRecordId);
            mktDraw.setMarketingId(marketingId);

            try {
                MktDrawRule rule = restClient.post("marketing/drawPrize/{id}/random", mktDraw, MktDrawRule.class, marketingId);
                String ruleId = rule.getId();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String drawRuleIdDate = dateFormat.format(date);

                HashMap<String, String> dictionary = (HashMap<String, String>)map.get(ruleId);
                if (dictionary == null) {
                    dictionary = new HashMap();
                }
                String keyList = dictionary.getOrDefault("productKeyList", "") + productKey + ", ";
                dictionary.put("productKeyList", keyList);

                dictionary.put("comments", String.format("%-14s", rule.getComments()));
                dictionary.put("device",String.format("%-7s",(isAndroid? "Android" : "iOS")));
                dictionary.put("createtime", drawRuleIdDate);

                Integer count = Integer.parseInt(dictionary.getOrDefault("count", "0")) + 1;
                System.out.println("count is " + count);
                dictionary.put("count", Integer.toString(count));
                map.put(ruleId, dictionary);

            } catch (Exception e) {
                if (e instanceof ConflictException) {
                    if (((ConflictException) e).getHttpStatus().equals(HttpStatus.CONFLICT)){
                        prizedKeys.add(productKey);
//                        System.out.println("key already prized: "+productKey);
                    }
                } else if (e instanceof NullPointerException) {
                    System.out.println("null pointer exception");
                }
            }
        });

        StringBuilder stringBuilder = new StringBuilder();
        String fileName = new SimpleDateFormat("MM-dd hh-mm-ss'.log'").format(new Date());

        try (FileOutputStream fos = new FileOutputStream(fileName);
             BufferedOutputStream bf = new BufferedOutputStream(fos)) {

            int index = 0;
            stringBuilder.append("  " + String.format("%-8s","device") + String.format("%-20s","ruleid") + String.format("%-17s","comments")
            + String.format("%-8s", "count") + String.format("%-20s", "date") + "productkeys" + "\n");

            for (Map.Entry<String, HashMap> entry : map.entrySet())
            {
                index++;
                stringBuilder.append(index + " ");
                HashMap theMap = entry.getValue();
                stringBuilder.append(theMap.get("device") + " ");
                stringBuilder.append(entry.getKey() + " ");
                stringBuilder.append(theMap.get("comments") + " ");
                stringBuilder.append(String.format("%-6s", theMap.get("count")));
                stringBuilder.append(theMap.get("createtime") + " ");
                stringBuilder.append(theMap.get("productKeyList") + " ");

                stringBuilder.append("\n");
            }


            if (prizedKeys.size() > 0) {
                stringBuilder.append("prizedKeys :\n" + prizedKeys);
            }

            bf.write(stringBuilder.toString().getBytes("utf-8"));
            bf.flush();
        } catch (Exception e) {
            System.out.println("save drawResult failed at" + e.getLocalizedMessage());

        }
        System.out.println("result is: \n" + stringBuilder.toString());
    }

}