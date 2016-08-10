package com.yunsoo.api.controller;

import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.api.auth.service.AuthAccountService;
import com.yunsoo.api.domain.AttachmentDomain;
import com.yunsoo.api.domain.BrandApplicationDomain;
import com.yunsoo.api.domain.PaymentDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandApplicationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   yan
 * Created on:   3/17/2016
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/brandApplication")
public class BrandApplicationController {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private BrandApplicationDomain brandApplicationDomain;

    @Autowired
    private AttachmentDomain attachmentDomain;

    @Autowired
    private AuthAccountService authAccountService;

    @Autowired
    private PaymentDomain paymentDomain;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public BrandApplication getById(@PathVariable(value = "id") String id) {
        BrandApplicationObject obj = brandApplicationDomain.getBrandApplicationById(id);
        if (obj == null) {
            throw new NotFoundException("brand application not found by [id: " + id + "]");
        }
        return extend(new BrandApplication(obj));
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#carrierId, 'org', 'brand_application:read')")
    public int count(@RequestParam("carrier_id") String carrierId,
                     @RequestParam("status_code") String statusCode,
                     @RequestParam("has_payment") boolean hasPayment) {
        carrierId = AuthUtils.fixOrgId(carrierId);
        return brandApplicationDomain.count(carrierId, statusCode, hasPayment);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BrandApplication> getByFilter(
            @RequestParam(value = "carrier_id") String carrierId,
            @RequestParam(value = "brand_name", required = false) String brandName,
            @RequestParam(value = "status_code", required = false) String statusCode,
            @RequestParam(value = "has_payment", required = false) Boolean hasPayment,
            @RequestParam(value = "created_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeGE,
            @RequestParam(value = "created_datetime_le", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeLE,
            @RequestParam(value = "search_text", required = false) String searchText,
            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletResponse response) {
        if (!StringUtils.hasText(searchText)) {
            searchText = null;
        }
        Page<BrandApplicationObject> page = brandApplicationDomain.getBrandApplicationPaged(carrierId, brandName, statusCode, hasPayment, createdDateTimeGE, createdDateTimeLE, searchText, pageable);
        return PageUtils.response(response, page.map(BrandApplication::new), pageable != null);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public BrandApplication createBrandApplication(@RequestBody @Valid BrandApplication brandApplication) {
        brandApplication.setBrandName(brandApplication.getBrandName().trim());
        return new BrandApplication(brandApplicationDomain.createBrandApplication(brandApplication.toBrandApplicationObject()));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdateBrand(@PathVariable("id") String id,
                                 @RequestBody BrandApplication brandApplication) {
        BrandApplicationObject obj = brandApplication.toBrandApplicationObject();
        obj.setId(id);
        obj.setPassword(null);
        brandApplicationDomain.patchUpdateBrandApplication(obj);
    }

    @RequestMapping(value = "{id}/approve", method = RequestMethod.POST)
    //@com.yunsoo.api.aspect.OperationLog(operation = "通过审批品牌商入住申请：' + #id + ', 行业：' + #category", level = "P1")
    public BrandApplication approveBrandApplication(@PathVariable("id") String id,
                                                    @RequestParam("category_id") String categoryId) {
        BrandApplicationObject obj = brandApplicationDomain.getBrandApplicationById(id);
        if (obj == null) {
            throw new NotFoundException("brandApplication not found by id: " + id);
        }
        AuthUtils.checkPermission(obj.getCarrierId(), "brand_application", "write");
        return new BrandApplication(brandApplicationDomain.approveBrandApplication(id, categoryId));
    }

    @RequestMapping(value = "{id}/reject", method = RequestMethod.POST)
    //@com.yunsoo.api.aspect.OperationLog(operation = "拒绝通过品牌商入住申请：' + #id + ', 拒绝原因：' + #reject_reason", level = "P1")
    public BrandApplication rejectBrandApplication(@PathVariable("id") String id,
                                                   @RequestParam(name = "reject_reason", required = false) String rejectReason) {
        BrandApplicationObject obj = brandApplicationDomain.getBrandApplicationById(id);
        if (obj == null) {
            throw new NotFoundException("brandApplication not found by id: " + id);
        }
        AuthUtils.checkPermission(obj.getCarrierId(), "brand_application", "write");
        return new BrandApplication(brandApplicationDomain.rejectBrandApplication(id, rejectReason));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public BrandApplication login(@RequestBody BrandApplicationLoginRequest request) {
        //validate parameters
        if (request.getOrganization() == null || request.getIdentifier() == null) {
            log.warn(String.format("parameters are invalid [organization: %s, identifier: %s]",
                    request.getOrganization(), request.getIdentifier()));
            throw new UnauthorizedException("account is not valid");
        }

        //find account
        BrandApplicationObject obj = brandApplicationDomain.getBrandApplicationByName(request.getOrganization().trim());
        if (obj == null)
            throw new UnauthorizedException("account is not valid");
        else {
            if (!obj.getIdentifier().equals(request.getIdentifier())) {
                throw new UnauthorizedException("account is not valid");
            }

            //validate password
            if (!brandApplicationDomain.validatePassword(request.getPassword(), obj.getHashSalt(), obj.getPassword())) {
                throw new UnauthorizedException("account is not valid");
            }

            return extend(new BrandApplication(obj));
        }
    }

    @RequestMapping(value = "{id}/password", method = RequestMethod.PUT)
    public void resetPassword(@PathVariable("id") String id, @RequestBody String newPassword) {
        if (StringUtils.isEmpty(newPassword)) {
            throw new BadRequestException("password must not be null or empty");
        }
        BrandApplicationObject obj = brandApplicationDomain.getBrandApplicationById(id);
        if (obj == null) {
            throw new NotFoundException("Brand application not find with id: " + id);
        }
        AuthUtils.checkPermission(obj.getCarrierId(), "brand_application", "write");
        BrandApplicationObject newObj = new BrandApplicationObject();
        newObj.setId(obj.getId());
        newObj.setPassword(newPassword);
        brandApplicationDomain.patchUpdateBrandApplication(newObj);
    }

    @RequestMapping(value = "{id}/history", method = RequestMethod.GET)
    public List<BrandApplicationHistory> getHistoryById(@PathVariable("id") String id) {
        Map<String, Account> accountMap = new HashMap<>();
        return brandApplicationDomain.getBrandApplicationHistory(id)
                .stream()
                .map(o -> {
                    BrandApplicationHistory h = new BrandApplicationHistory(o);
                    String aId = h.getCreatedAccountId();
                    if (StringUtils.hasText(aId)) {
                        Account account = accountMap.get(aId);
                        if (account == null) {
                            account = authAccountService.getById(aId);
                            accountMap.put(aId, account);
                        }
                        h.setAccount(account);
                    }
                    return h;
                })
                .collect(Collectors.toList());
    }


    private BrandApplication extend(BrandApplication brandApplication) {
        if (StringUtils.hasText(brandApplication.getAttachment())) {
            List<AttachmentObject> attachmentObjectList = attachmentDomain.getAttachmentList(brandApplication.getAttachment());
            brandApplication.setAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
        }
        if (StringUtils.hasText(brandApplication.getInvestigatorAttachment())) {
            List<AttachmentObject> attachmentObjectList = attachmentDomain.getAttachmentList(brandApplication.getInvestigatorAttachment());
            brandApplication.setInvestigatorAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
        }
        if (StringUtils.hasText(brandApplication.getPaymentId())) {
            brandApplication.setPayment(new Payment(paymentDomain.getPaymentById(brandApplication.getPaymentId())));
        }
        return brandApplication;
    }

}
