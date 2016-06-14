package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDataFlowEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yan on 6/14/2016.
 */
public interface MktDataFlowRepository extends CrudRepository<MktDataFlowEntity, Integer> {

    MktDataFlowEntity findByTypeAndDataFlow(String type, int flow);
}
