package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.OrgTransactionDetailEntity;
import com.yunsoo.data.service.repository.OrgTransactionDetailRepository;
import com.yunsoo.data.service.service.contract.OrgTransactionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
//import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/4/16.
 */
@RestController
@RequestMapping("/org-transaction-detail")
public class OrgTransactionDetailController {

    @Autowired
    private OrgTransactionDetailRepository orgTransactionDetailRepository;

    @RequestMapping(value = "/{orgid}", method = RequestMethod.GET)
    public List<OrgTransactionDetail> getTransactionDetails(@PathVariable(value = "orgid") String orgId,
                                                            @RequestParam(value = "orderid") Long orderId,
                                                            @RequestParam(value = "index", defaultValue = "0") Integer index,
                                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<OrgTransactionDetail> orgTransactionDetailList;
        if (orderId == null) {
            orgTransactionDetailList = OrgTransactionDetail.FromEntityList(orgTransactionDetailRepository.findByOrgId(orgId, new PageRequest(index, size)));
        } else {
            if (orderId < 0) throw new BadRequestException("orderId必须为不小于0的值！");
            orgTransactionDetailList = OrgTransactionDetail.FromEntityList(orgTransactionDetailRepository.findByOrgIdAndOrderId(orgId, orderId, new PageRequest(index, size)));
        }
        return orgTransactionDetailList;
    }

//    @RequestMapping(value = "/b/{orgid}", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<OrgTransactionDetail>> getOrgTransactionDetail(Pageable pageable,
//                                                                                    PagedResourcesAssembler assembler,
//                                                                                    @PathVariable(value = "orgid") String orgId) {
//        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("id不能为空！");
//        Page<OrgTransactionDetailEntity> orgTrans = orgTransactionDetailRepository.findByOrgId(orgId, pageable);
//        return new ResponseEntity<>(assembler.toResource(orgTrans), HttpStatus.OK);
//    }

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
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void patchUpdate(@RequestBody OrgTransactionDetail orgTransactionDetail) {
        if (!orgTransactionDetailRepository.exists(orgTransactionDetail.getId())) {
            throw new NotFoundException("找不到Transaction Detail！ id=" + orgTransactionDetail.getId());
        }
        OrgTransactionDetailEntity entity = orgTransactionDetailRepository.save(OrgTransactionDetail.ToEntity(orgTransactionDetail));
    }
}
