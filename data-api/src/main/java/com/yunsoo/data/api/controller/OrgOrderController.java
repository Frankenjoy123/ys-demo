package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.data.service.entity.OrgOrderEntity;
import com.yunsoo.data.service.repository.OrgOrderRepository;
import com.yunsoo.data.service.service.contract.OrgOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by Zhe on 2015/4/16.
 */
@RestController
@RequestMapping("/orgorder")
public class OrgOrderController {
    @Autowired
    private OrgOrderRepository orgOrderRepository;

    @RequestMapping(value = "/org/{id}/active/{active}", method = RequestMethod.GET)
    public List<OrgOrder> getOrgOrdersByOrgId(@PathVariable(value = "id") String id,
                                              @PathVariable(value = "active") Boolean active,
                                              @PathParam(value = "index") Integer index,
                                              @PathParam(value = "size") Integer size) {

        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        if (active == null) {
            active = true;
        } //default is true
        List<OrgOrder> orgOrderList = OrgOrder.FromEntityList(orgOrderRepository.findByOrgIdAndActive(id, true, new PageRequest(index, size)));
        return orgOrderList;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@RequestBody OrgOrder orgOrder) {
        OrgOrderEntity newEntity = orgOrderRepository.save(OrgOrder.ToEntity(orgOrder));
        return newEntity.getId();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void batchCreate(@RequestBody Iterable<OrgOrder> orgOrders) {
        Iterable<OrgOrderEntity> orgOrderEntities = orgOrderRepository.save(OrgOrder.ToEntityList(orgOrders));
//        return newEntity.getId();
    }

}
