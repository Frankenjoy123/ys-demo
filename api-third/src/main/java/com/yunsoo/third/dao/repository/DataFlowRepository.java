package com.yunsoo.third.dao.repository;

import com.yunsoo.third.dao.entity.ThirdJuheDataFlowEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yan on 6/14/2016.
 */
public interface DataFlowRepository extends CrudRepository<ThirdJuheDataFlowEntity, Integer> {

    ThirdJuheDataFlowEntity findByTypeAndDataFlow(String type, int flow);
}
