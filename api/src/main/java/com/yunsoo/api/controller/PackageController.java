package com.yunsoo.api.controller;

import com.yunsoo.api.dto.basic.Package;
import com.yunsoo.common.data.object.PackageBoundObject;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotAcceptableException;
import com.yunsoo.common.web.exception.NotFoundException;
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

//import org.joda.time.DateTime;

/**
 * Created by Qiyong Yu on 2015/3/8.
 */
@RestController
@RequestMapping("/package")
public class PackageController {

    private RestClient dataAPIClient;

    @Autowired
    PackageController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }


    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public com.yunsoo.api.dto.basic.Package getDetailByKey(@PathVariable(value = "key") String key) {

        Package productPackage;
        productPackage = dataAPIClient.get("package/{key}", Package.class, key);
        if (productPackage == null) {
            throw new NotFoundException("没有包装信息");
        } else {
            return productPackage;
        }
    }

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean bind(@RequestBody PackageBoundObject data) throws Exception {
        boolean result = dataAPIClient.post("package/bind", data, Boolean.class);
        return result;
    }

    @RequestMapping(value = "/batch/bind", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean batchBind(@RequestBody PackageBoundObject[] data) throws Exception {
        boolean result = dataAPIClient.post("package/batch/bind", data, Boolean.class);
        return result;
    }

    @RequestMapping(value = "/revoke/{key}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean revokePackage(@PathVariable(value = "key") String key) {
        dataAPIClient.delete("package/revoke/{key}", key);
        return true;
    }


    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public Boolean UploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {


        Iterator<String> itr = request.getFileNames();
        MultipartFile file = request.getFile(itr.next());

        try {
            InputStreamReader in = new InputStreamReader(file.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            // StringBuilder content = new StringBuilder();
            String line = null;
            List<PackageBoundObject> objects = new ArrayList<PackageBoundObject>();
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isEmpty(line.replaceAll("\\r\\n", ""))) {
                    continue;
                }
                objects.add(toPackageBoundObject(line));
            }
            reader.close();

            boolean batchResult = dataAPIClient.post("/package/batch/bind", objects, Boolean.class);
            if (!batchResult) {
                throw new NotAcceptableException("数据中有重复的码，请检查。");
            }
            return batchResult;
        } catch (NotAcceptableException ex) {
            throw ex;
        } catch (Exception e) {
            throw new InternalServerErrorException("未知错误，请确保没有重复打包的情况");
        }
    }

    private static PackageBoundObject toPackageBoundObject(String line) {
        String[] dataArray = line.split(",");
        //TODO let these items pass.
        if (dataArray.length < 2) {
            throw new NotAcceptableException("错误的数据录入");
        }
        List<String> subKeys = new ArrayList<String>();
        for (int i = 2; i < dataArray.length; i++) {
            subKeys.add(dataArray[i].trim());
        }

        DateTime datetime = DateTimeUtils.parse(dataArray[0]);
        PackageBoundObject object = new PackageBoundObject(0, dataArray[1].trim(), subKeys, datetime);
        return object;


    }


}
