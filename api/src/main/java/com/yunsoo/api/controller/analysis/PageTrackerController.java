package com.yunsoo.api.controller.analysis;

import com.yunsoo.api.client.DataApiClient;
import com.yunsoo.api.di.dto.PageTrackInfo;
import com.yunsoo.api.di.service.PageTrackService;
import com.yunsoo.api.util.IpUtils;
import com.yunsoo.common.data.object.juhe.IPResultObject;
import com.yunsoo.common.web.exception.BadRequestException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by yqy09_000 on 2016/11/16.
 */
@RestController
@RequestMapping("/analysis/track")
public class PageTrackerController {

    private Log log = LogFactory.getLog(this.getClass());
    private static final String YS_ID = "ys_id";
    private static final String CURR_URL = "c_url";
    private static final String ACTION = "action";
    private static final String PAGE_ID = "page_id";
    private static final String[] LU_PROVINCES = new String[]{"黑龙江", "海南",
            "福建",
            "河南",
            "上海",
            "江西",
            "广东",
            "山东",
            "宁夏",
            "甘肃",
            "山西",
            "云南",
            "辽宁",
            "浙江",
            "内蒙古",
            "新疆",
            "四川",
            "安徽",
            "河北",
            "贵州",
            "北京",
            "广西",
            "湖北",
            "江苏",
            "台湾",
            "吉林",
            "陕西",
            "天津",
            "湖南",
            "西藏",
            "青海",
            "澳门",
            "重庆",
            "香港"
    };

    private static final String[] LU_SUPER_CITIES = new String[]{
            "上海",
            "北京",
            "天津",
            "重庆"
    };

    private static final String[] LU_VERIFIED_DOMAIN = new String[]{
            "yunsu.co", "localhost"
    };

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    protected PageTrackService pageTrackService;

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public void track(HttpServletRequest request, HttpServletResponse response) {

        String ip = IpUtils.getIpFromRequest(request);
        String userAgent = request.getHeader("User-Agent");
        Map<String, String[]> requestData = request.getParameterMap();
        String ysId = getEventData(requestData, YS_ID);
        String url = getEventData(requestData, CURR_URL);
        String action = getEventData(requestData, ACTION);
        String pageId = getEventData(requestData, PAGE_ID);

        if (StringUtils.isEmpty(url)) {
            throw new BadRequestException("c_url不能为空");
        }

       boolean domainVerified =  Arrays.asList(LU_VERIFIED_DOMAIN).stream().filter(url::contains).count() > 0;
        if (!domainVerified) {
            throw new BadRequestException("域名非法");
        }

        String actionData = getActionData(requestData);
        String province = null;
        String city = null;
        String address = null;

        try {
            IPResultObject ipResultObject = dataApiClient.get("juhe/ip?ip={ip}", IPResultObject.class, ip);
            if (ipResultObject.getErrorCode() == 0) {
                String addr = ipResultObject.getResult().getArea();
                address = addr;
                Optional<String> provinceTmp = Arrays.asList(LU_PROVINCES).stream().filter(addr::contains).findFirst();
                if (provinceTmp.isPresent()) {

                    String provinceValue = provinceTmp.get();
                    Optional<String> superCityTmp = Arrays.asList(LU_SUPER_CITIES).stream().filter(provinceValue::contains).findFirst();
                    if (superCityTmp.isPresent()) {
                        province = provinceValue;
                        city = provinceValue + "市";
                    } else {
                        province = provinceValue;
                        city = addr.replaceFirst("^" + province + "省?", "");
                    }
                }
            }
        }
        catch (Exception ignored)
        {
        }


        PageTrackInfo trackInfo = new PageTrackInfo();
        trackInfo.setAction(action);
        trackInfo.setActionData(actionData);
        trackInfo.setAddress(address);
        trackInfo.setProvince(province);
        trackInfo.setCity(city);
        trackInfo.setIp(ip);
        trackInfo.setUrl(url);
        trackInfo.setUserAgent(userAgent);
        trackInfo.setYsId(ysId);
        trackInfo.setPageId(pageId);

        pageTrackService.trackPageView(trackInfo);
    }

    private String getActionData(Map<String, String[]> data) {
        String[] fieldDBRequired = new String[]{YS_ID, CURR_URL, ACTION, PAGE_ID};
        List<String> actionDataList = data.entrySet().stream().filter(m -> {
            String key = m.getKey();
            return !Arrays.asList(fieldDBRequired).contains(key);
        }).map(kv -> {
            return kv.getKey() + "=" + kv.getValue()[0];
        }).collect(Collectors.toList());

        return String.join(",", actionDataList);
    }

    private static String getEventData(Map<String, String[]> parametersData, String key) {
        String value = "";
        String[] parameterData = parametersData.get(key);
        if (parameterData != null && parameterData.length > 0) {
            value = parameterData[0];
        }
        return value;
    }
}
