package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserPointEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
public interface UserPointRepository extends FindOneAndSaveRepository<UserPointEntity, String> {

    @Query("select o from #{#entityName} o where " +
            "(:pointGE is null or o.point >= :pointGE) " +
            "and (:pointLE is null or o.point <= :pointLE) " +
            "and (:lastSignInDatetimeGE is null or o.lastSignInDateTime >= :lastSignInDatetimeGE) " +
            "and (:lastSignInDatetimeLE is null or o.lastSignInDateTime <= :lastSignInDatetimeLE) " +
            "and (:lastSignInContinuousDaysGE is null or o.lastSignInContinuousDays >= :lastSignInContinuousDaysGE) " +
            "and (:lastSignInContinuousDaysLE is null or o.lastSignInContinuousDays <= :lastSignInContinuousDaysLE)")
    Page<UserPointEntity> query(@Param("pointGE") Integer pointGE,
                                @Param("pointLE") Integer pointLE,
                                @Param("lastSignInDatetimeGE") String lastSignInDatetimeGE,
                                @Param("lastSignInDatetimeLE") String lastSignInDatetimeLE,
                                @Param("lastSignInContinuousDaysGE") Integer lastSignInContinuousDaysGE,
                                @Param("lastSignInContinuousDaysLE") Integer lastSignInContinuousDaysLE,
                                Pageable pageable);
}
