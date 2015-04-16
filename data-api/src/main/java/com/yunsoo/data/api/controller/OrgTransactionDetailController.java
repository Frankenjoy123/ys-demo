package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.data.service.entity.OrgTransactionDetailEntity;
import com.yunsoo.data.service.repository.OrgTransactionDetailRepository;
import com.yunsoo.data.service.service.contract.OrgTransactionDetail;
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
@RequestMapping("/org-transaction-detail")
public class OrgTransactionDetailController {

    @Autowired
    private OrgTransactionDetailRepository orgTransactionDetailRepository;

    @RequestMapping(value = "/org/{id}/orderId/{orderId}", method = RequestMethod.GET)
    public List<OrgTransactionDetail> getTransactionDetails(@PathVariable(value = "id") String id,
                                                            @PathVariable(value = "orderId") Long orderId,
                                                            @PathParam(value = "index") Integer index,
                                                            @PathParam(value = "size") Integer size) {

        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (orderId == null || orderId < 0) throw new BadRequestException("orderId必须为不小于0的值！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<OrgTransactionDetail> orgTransactionDetailList = OrgTransactionDetail.FromEntityList(orgTransactionDetailRepository.findByOrgIdAndOrderId(id, orderId, new PageRequest(index, size)));
        return orgTransactionDetailList;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@RequestBody OrgTransactionDetail orgTransactionDetail) {
        OrgTransactionDetailEntity newEntity = orgTransactionDetailRepository.save(OrgTransactionDetail.ToEntity(orgTransactionDetail));
        return newEntity.getId();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void batchCreate(@RequestBody Iterable<OrgTransactionDetail> orgTransactionDetails) {
        Iterable<OrgTransactionDetailEntity> orgTransactionDetailEntities = orgTransactionDetailRepository.save(OrgTransactionDetail.ToEntityList(orgTransactionDetails));
//        return newEntity.getId();
    }
}
