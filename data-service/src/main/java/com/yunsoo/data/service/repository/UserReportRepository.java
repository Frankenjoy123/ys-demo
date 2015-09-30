package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserReportEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by yan on 9/28/2015.
 */
public interface UserReportRepository extends FindOneAndSaveRepository<UserReportEntity, String> {

    Page<UserReportEntity> findByProductBaseIdIn(List<String> productBaseIds, Pageable pageable);

    Page<UserReportEntity> findByUserId(String userId, Pageable pageable);
}
