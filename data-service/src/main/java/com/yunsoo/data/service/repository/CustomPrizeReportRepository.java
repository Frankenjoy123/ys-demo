package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawPrizeReportEntity;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by admin on 2016/9/14.
 */
public interface CustomPrizeReportRepository {

    List<MktDrawPrizeReportEntity> queryMktDrawPrizeReport(String marketingId,
                                                           String accountType,
                                                           String prizeTypeCode,
                                                           String statusCode,
                                                           DateTime startTime,
                                                           DateTime endTime);

}
