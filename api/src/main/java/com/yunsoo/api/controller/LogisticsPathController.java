package com.yunsoo.api.controller;

import com.yunsoo.api.domain.LogisticsDomain;
import com.yunsoo.api.domain.ProductFileDomain;
import com.yunsoo.api.dto.LogisticsPath;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.LogisticsBatchPathObject;
import com.yunsoo.common.data.object.LogisticsPathObject;
import com.yunsoo.common.data.object.ProductFileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotAcceptableException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jerry on 3/16/2015.
 */
@RestController
@RequestMapping("/logistics")
public class LogisticsPathController {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private ProductFileDomain productFileDomain;

    @Autowired
    private LogisticsDomain logisticsDomain;


    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public List<LogisticsPath> get(@PathVariable(value = "key") String key) {
        return logisticsDomain.getLogisticsPathsOrderByStartDateTime(key);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LogisticsPathObject logisticsPathObject) {

        String createdBy = AuthUtils.getCurrentAccount().getId();
        logisticsPathObject.setOperator(createdBy);

        dataAPIClient.post("logisticspath/create", logisticsPathObject, Long.class);
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public Boolean UploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();
        MultipartFile file = request.getFile(itr.next());

        String createdBy = AuthUtils.getCurrentAccount().getId();

        ProductFileObject productFileObject = new ProductFileObject();
        productFileObject.setFileName(file.getOriginalFilename());
        productFileObject.setCreateDate(DateTime.now());
        productFileObject.setCreateBy(createdBy);
        productFileObject.setFileType(2);

        try {
            InputStreamReader in = new InputStreamReader(file.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            LogisticsBatchPathObject logisticsBatchPathObject = new LogisticsBatchPathObject();
            List<String> allKeys = new ArrayList<String>();

            String deviceCode = "";
            String actionId = "";

            String line = null;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isEmpty(line.replaceAll("\\r\\n", ""))) {
                    continue;
                }

                if (line.replaceAll("\\r\\n", "").contains("DeviceCode")) {
                    String[] code = line.split(":");
                    deviceCode = code[1].trim();
                    continue;
                }

                if (line.replaceAll("\\r\\n", "").contains("ActionId")) {
                    String[] action = line.split(":");
                    actionId = action[1].trim();
                    continue;
                }

                String[] dataArray = line.split(",");
                if (dataArray.length < 1) {
                    throw new NotAcceptableException("错误的数据录入");
                }

                for (int i = 1; i < dataArray.length; i++) {
                    allKeys.add(dataArray[i].trim());
                }
            }
            reader.close();

            logisticsBatchPathObject.setProductKeys(allKeys);

            //从上传的文件中获取以下信息
            logisticsBatchPathObject.setActionId(actionId);
            logisticsBatchPathObject.setStartCheckPoint("0");

            dataAPIClient.post("/logisticspath/batch", logisticsBatchPathObject, Long.class);

            productFileObject.setStatus(1);
            productFileDomain.createProductFile(productFileObject);

            return true;
        } catch (InternalServerErrorException e) {
            productFileObject.setStatus(2);
            productFileDomain.createProductFile(productFileObject);

            throw new InternalServerErrorException(e.getMessage());
        } catch (Exception e) {
            productFileObject.setStatus(2);
            productFileDomain.createProductFile(productFileObject);

            throw new NotAcceptableException("数据解析失败，请检查文件。");
        }
    }
}
