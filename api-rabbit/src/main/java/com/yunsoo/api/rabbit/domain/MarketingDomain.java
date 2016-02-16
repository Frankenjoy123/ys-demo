package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import com.yunsoo.common.data.object.MktDrawRecordObject;
import com.yunsoo.common.web.client.RestClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by  : Haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */

@Component
public class MarketingDomain {

    @Autowired
    private RestClient dataAPIClient;

    public MktDrawRecordObject createMktDrawRecord(MktDrawRecordObject mktDrawRecordObject) {
        mktDrawRecordObject.setId(null);
        mktDrawRecordObject.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("marketing/draw", mktDrawRecordObject, MktDrawRecordObject.class);
    }

    public MktDrawPrizeObject createMktDrawPrize(MktDrawPrizeObject mktDrawPrizeObject) {
        mktDrawPrizeObject.setCreatedDateTime(DateTime.now());
        mktDrawPrizeObject.setStatusCode(LookupCodes.MktDrawPrizeStatus.CREATED);
        return dataAPIClient.post("marketing/drawPrize", mktDrawPrizeObject, MktDrawPrizeObject.class);
    }


}
