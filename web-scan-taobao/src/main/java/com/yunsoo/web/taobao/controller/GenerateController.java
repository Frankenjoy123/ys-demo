
package com.yunsoo.web.taobao.controller;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.WirelessBuntingAntifakeimageGenerateRequest;
import com.taobao.api.request.WirelessBuntingSecurityshorturlGenerateRequest;
import com.taobao.api.response.WirelessBuntingAntifakeimageGenerateResponse;
import com.taobao.api.response.WirelessBuntingSecurityshorturlGenerateResponse;
import com.yunsoo.web.taobao.domain.TokenDomain;
import com.yunsoo.web.taobao.util.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yan on 1/25/2016.
 */

@Controller
@RequestMapping(value = "generate")
public class GenerateController {

    private static Log log = LogFactory.getLog(GenerateController.class);
    private int max = 50;

    @Value("${taobao.api}")
    private String url;

    @Value("${taobao.appkey}")
    private String appkey;

    @Value("${taobao.appsecret}")
    private String secret;

    @Value("${taobao.redirect_url}")
    private String redirectUrl;

    @Value("${taobao.token_url}")
    private String tokenUrl;

    @Value("${taobao.auth_url}")
    private String authUrl;

    @RequestMapping("/generate")
    @ResponseBody
    //type=1 to generate url, type=2 to generate png
    public ResponseEntity generateQRCode(@RequestParam(value = "session", required = false) String sessionKey,
                                         @RequestParam(value = "batchId") String batchId, @RequestParam(value = "password") String password,
                                         @RequestParam(value = "number") int number, @RequestParam(value = "index") int index,
                                         @RequestParam(value = "type") int type) throws ApiException {

        String path = "d:\\qrcode\\" + batchId + "_" + DateTime.now().getMillis() + ".txt";
        List<String> productIds = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            productIds.add(String.valueOf(i + index));
        }

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        for (int i = 0; i < number / max; i++) {
            List<String> subIds = productIds.subList(i * max, (i + 1) * max);
            if (type == 2)
                callTBGenerateAPIWithAnti(subIds, client, batchId, password, path);
            else if (type == 1)
                callTBGenerateAPI(subIds, client, batchId, password, path);
        }
        if (number % max > 0) {
            List<String> subIds = productIds.subList((number / max) * max, number);
            if (type == 2)
                callTBGenerateAPIWithAnti(subIds, client, batchId, password, path);
            else if (type == 1)
                callTBGenerateAPI(subIds, client, batchId, password, path);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    private void callTBGenerateAPIWithAnti(List<String> productIds, TaobaoClient client, String batchId, String password, String path) throws ApiException {

        WirelessBuntingAntifakeimageGenerateRequest req = new WirelessBuntingAntifakeimageGenerateRequest();
        req.setBatchId(batchId);
        req.setPassword(password);
        req.setLongUrls("?id=" + join(",?id=", productIds));
        req.setProductIds(join(",", productIds));
        req.setQrType("4");
        req.setImageUrl("http://img.alicdn.com/tfscom/LB1Viv8LpXXXXakXXXXSutbFXXX.jpg");
        WirelessBuntingAntifakeimageGenerateResponse response = client.execute(req);
        if (!StringUtils.hasText(response.getErrorCode())) {
            saveContentToFile(join(",", response.getImageUrls()) + ",", path);
            log.info("generate taobao code successfully with batch: " + batchId + ", id begin: " + productIds.get(0));
        } else
            log.error("generate taobao short url failed, batchId: " + batchId + ", id begin: " + productIds.get(0)
                    + "message: " + response.getBody());
    }


    private void callTBGenerateAPI(List<String> productIds, TaobaoClient client, String batchId, String password, String path) throws ApiException {

        WirelessBuntingSecurityshorturlGenerateRequest req = new WirelessBuntingSecurityshorturlGenerateRequest();
        req.setBatchId(batchId);
        req.setPassword(password);
        req.setLongUrls("?id=" + join(",?id=", productIds));
        req.setProductIds(join(",", productIds));
        WirelessBuntingSecurityshorturlGenerateResponse response = client.execute(req);
        if (!StringUtils.hasText(response.getErrorCode())) {
            saveContentToFile(response.getShorturls().substring(1, response.getShorturls().length() - 1) + ",", path);
            log.info("generate taobao code successfully with batch: " + batchId + ", id begin: " + productIds.get(0));
        } else
            log.error("generate taobao short url failed, batchId: " + batchId + ", id begin: " + productIds.get(0)
                    + "message: " + response.getBody());
    }

    @RequestMapping("/auth")
    @ResponseBody
    public ResponseEntity auth(@RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "error_description", required = false) String errorDesc, HttpServletRequest request) {

        if (StringUtils.hasText(code)) {

            Map<String, String> props = new HashMap<String, String>();
            props.put("grant_type", "authorization_code");
            props.put("code", code);
            props.put("client_id", appkey);
            props.put("client_secret", secret);
            props.put("redirect_uri", redirectUrl);
            props.put("view", "web");
            String s = "";

            try {
                s = WebUtils.doPost(tokenUrl, props, 30000, 30000);
                TokenDomain token = ObjectUtils.toObject(s, TokenDomain.class);
                request.getSession().setAttribute("token", token);
                return new ResponseEntity(HttpStatus.OK);

            } catch (IOException e) {

                e.printStackTrace();
                return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity("visit taobao error, error: " + error + ", description: " + errorDesc, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/authorize")
    public String authorize() {
        String authorizeUrl = authUrl + "?response_type=code&client_id=" + appkey + "&redirect_uri=" + redirectUrl + "&state=1212&view=web";
        return "redirect:" + authorizeUrl;
    }

    private boolean saveContentToFile(String content, String path) {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.write(content);
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String join(String seperator, List<String> list){
        StringBuilder sb = new StringBuilder();
        for(String item : list){
            sb.append(item);
            sb.append(seperator);
        }

        String result = sb.toString();

        return  result.substring(0, result.length() - seperator.length());
    }
}
