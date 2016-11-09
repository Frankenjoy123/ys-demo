package com.yunsoo.di.dao.repository;


import com.yunsoo.di.dao.entity.EMREventEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EMREventRepository  {


    List<EMREventEntity> findByFilter(String orgId,  String userId,  String ysId,
                                       DateTime eventDateTimeStart,
                                       DateTime eventDateTimeEnd, Pageable pageable);

    int countEventByFilter(String orgId,  String userId,  String ysId,
                                      DateTime eventDateTimeStart,
                                      DateTime eventDateTimeEnd);

}
