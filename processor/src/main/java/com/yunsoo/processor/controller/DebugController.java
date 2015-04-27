package com.yunsoo.processor.controller;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.yunsoo.processor.common.LogicalQueueName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/26
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/debug")
public class DebugController {

    @Value("${yunsoo.debug}")
    private Boolean debug;

    @Value("${yunsoo.environment}")
    private String environment;

    @Autowired(required = false)
    private AWSCredentialsProvider awsCredentialsProvider;

    @Autowired(required = false)
    private RegionProvider regionProvider;

    @Autowired(required = false)
    private ResourceIdResolver resourceIdResolver;

    @RequestMapping(value = "")
    public Map<String, Object> info() {
        Map<String, Object> result = new HashMap<>();

        //common info
        result.put("debug", debug);
        result.put("environment", environment);
        if (regionProvider != null) {
            result.put("region", regionProvider.getRegion());
        }


        if (awsCredentialsProvider != null) {
            result.put("awsCredentialsProvider", awsCredentialsProvider.getClass());
        }
        if (regionProvider != null) {
            result.put("regionProvider", regionProvider.getClass());
        }
        if (resourceIdResolver != null) {
            result.put("resourceIdResolver", resourceIdResolver.getClass());
        }

        Map<String, Object> queues = new HashMap<>();
        queues.put(LogicalQueueName.PRODUCT_KEY_BATCH, resolveResourceId(LogicalQueueName.PRODUCT_KEY_BATCH));
        result.put("queues", queues);


        return result;
    }

    private String resolveResourceId(String logicalResourceId) {
        return resourceIdResolver != null ? resourceIdResolver.resolveToPhysicalResourceId(logicalResourceId) : logicalResourceId;
    }


}
