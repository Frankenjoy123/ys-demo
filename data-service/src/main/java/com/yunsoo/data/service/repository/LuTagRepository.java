package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.LookupEntity;
import com.yunsoo.data.service.entity.LuTagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * Created by:   yan
 * Created on:   9/7/2015
 * Descriptions:
 */
public interface LuTagRepository extends CrudRepository<LuTagEntity, Integer> {
    List<LuTagEntity> findAll();
}
