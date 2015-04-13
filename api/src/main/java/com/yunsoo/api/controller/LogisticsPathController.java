package com.yunsoo.api.controller;

import com.yunsoo.api.domain.LogisticsDomain;
import com.yunsoo.api.dto.LogisticsPath;
import com.yunsoo.common.data.object.LogisticsBatchPathObject;
import com.yunsoo.common.data.object.LogisticsPathObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotAcceptableException;
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
    private LogisticsDomain logisticsDomain;

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public List<LogisticsPath> get(@PathVariable(value = "key") String key) {
        return logisticsDomain.getLogisticsPathsOrderByStartDate(key);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LogisticsPathObject logisticsPathObject) {
        dataAPIClient.post("logisticspath/create", logisticsPathObject, Long.class);
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public Boolean UploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {

        Iterator<String> itr = request.getFileNames();
        MultipartFile file = request.getFile(itr.next());

        try {
            InputStreamReader in = new InputStreamReader(file.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            LogisticsBatchPathObject logisticsBatchPathObject = new LogisticsBatchPathObject();
            List<String> allKeys = new ArrayList<String>();

            String line = null;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isEmpty(line.replaceAll("\\r\\n", ""))) {
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

            logisticsBatchPathObject.setProductKey(allKeys);

            //从上传的文件中获取以下信息
            logisticsBatchPathObject.setAction_id(1);
            logisticsBatchPathObject.setStartCheckPoint(1);

            dataAPIClient.post("/logisticspath/batchcreate", logisticsBatchPathObject, Long.class);

            return true;
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (Exception e) {
            throw new NotAcceptableException("数据解析失败，请检查文件。");
        }
    }
}
