package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMRUserEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yqy09_000 on 5/31/2016.
 */
public interface CustomEMRUserRepository {
    List<EMRUserEntity> findEventUsersFilterByScan(String orgId,
                                                   String productBaseId,
                                                   String province, String city,
                                                   DateTime createdDateTimeStart,
                                                   DateTime createdDateTimeEnd,
                                                   Pageable pageable);

    int  countEventUsersFilterByScan( String orgId,
                                      String productBaseId,
                                     String province,  String city,
                                     DateTime createdDateTimeStart,
                                    DateTime createdDateTimeEnd);

}
