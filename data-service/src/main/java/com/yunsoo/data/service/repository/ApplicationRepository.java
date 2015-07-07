package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ApplicationEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Zhe on 2015/6/15.
 */
public interface ApplicationRepository extends FindOneAndSaveRepository<ApplicationEntity, String> {

    Page<ApplicationEntity> findById(String id, Pageable pageable);

    Page<ApplicationEntity> findByIdAndStatusCodeIn(String id, List<String> statusCodes, Pageable pageable);

    Page<ApplicationEntity> findByTypeCodeAndStatusCodeIn(String type_code, List<String> statusCodes, Pageable pageable);

    Page<ApplicationEntity> findByIdAndTypeCodeAndStatusCodeIn(String id, String type_code, List<String> statusCodes, Pageable pageable);


}
