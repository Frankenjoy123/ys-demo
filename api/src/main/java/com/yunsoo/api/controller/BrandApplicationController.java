package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yan on 3/17/2016.
 */
@RestController
@RequestMapping(value = "/brand")
public class BrandApplicationController {

    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private BrandDomain brandDomain;

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private PermissionDomain permissionDomain;

    @Autowired
    private PaymentDomain paymentDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Brand getById(@PathVariable(value = "id") String id) {
        BrandObject object = brandDomain.getBrandById(id);
        if (object == null)
            throw new NotFoundException("brand application not found by [id: " + id + "]");

        Brand returnObject = new Brand(object);

        if (StringUtils.hasText(object.getAttachment())) {
            List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(object.getAttachment());
            returnObject.setAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
        }

        if (StringUtils.hasText(object.getInvestigatorAttachment())) {
            List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(object.getInvestigatorAttachment());
            returnObject.setInvestigatorAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
        }

        if (StringUtils.hasText(object.getPaymentId())) {
            returnObject.setPayment(new Payment(paymentDomain.getPaymentById(object.getPaymentId())));
        }

        return returnObject;
    }


    @RequestMapping(value = "count", method = RequestMethod.GET)
    public int count(@RequestParam(value = "carrier_id") String id, @RequestParam("status") String status, @RequestParam(value = "paid") boolean paid) {
        return brandDomain.count(id, status, paid);
    }

    @RequestMapping(value = "{id}/approve", method = RequestMethod.PUT)
    //  @PreAuthorize("hasPermission('*', 'org', 'brand:write')")
    //@com.yunsoo.api.aspect.OperationLog(operation = "通过审批品牌商入住申请：' + #id + ', 行业：' + #category", level = "P1")
    public boolean approveBrandApplication(@PathVariable("id") String id, @RequestParam("category") String category) {

        try {
            String currentAccountId = AuthUtils.getCurrentAccount().getId();
            BrandObject object = brandDomain.getBrandById(id);
            DateTime applicationDatetime = object.getCreatedDateTime();
            object.setCreatedAccountId(currentAccountId);
            object.setTypeCode(LookupCodes.OrgType.BRAND);
            object.setStatusCode(LookupCodes.OrgStatus.AVAILABLE);
            object.setCategoryId(category);
            BrandObject createdBrand = organizationDomain.createBrand(object);

            organizationDomain.saveOrgLogo(createdBrand.getId(), organizationDomain.getLogoImage(id, "image-128x128"));

            permissionDomain.putOrgRestrictionToDefaultPermissionRegion(object.getCarrierId(), createdBrand.getId());

            AccountObject accountObject = new AccountObject();
            accountObject.setEmail(object.getEmail());
            accountObject.setIdentifier(object.getIdentifier());
            accountObject.setFirstName(object.getContactName());
            accountObject.setLastName("");
            accountObject.setPassword(object.getPassword());
            accountObject.setHashSalt(object.getHashSalt());
            accountObject.setPhone(object.getContactMobile());
            accountObject.setOrgId(createdBrand.getId());
            accountObject.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);
            AccountObject createdAccount = accountDomain.createAccount(accountObject, false);
            //permissionAllocationDomain.allocateAdminPermissionOnCurrentOrgToAccount(createdAccount.getId());

            object.setId(id);
            object.setCreatedDateTime(applicationDatetime);
            object.setStatusCode(LookupCodes.BrandApplicationStatus.APPROVED);
            brandDomain.updateBrand(object);

            return true;
        } catch (Exception ex) {
            log.error("approve brand application error, id is: " + id, ex);
            return false;
        }
    }

    @RequestMapping(value = "{id}/reject", method = RequestMethod.PUT)
    //@com.yunsoo.api.aspect.OperationLog(operation = "拒绝通过品牌商入住申请：' + #id + ', 拒绝原因：' + #reject_reason", level = "P1")
    public boolean rejectBrandApplication(@PathVariable("id") String id, @RequestParam(name = "reject_reason", required = false) String reject_reason) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        try {
            BrandObject object = brandDomain.getBrandById(id);
            object.setStatusCode(LookupCodes.BrandApplicationStatus.REJECTED);
            if (StringUtils.hasText(reject_reason))
                object.setRejectReason(reject_reason);
            object.setCreatedAccountId(currentAccountId);
            brandDomain.updateBrand(object);
            return true;
        } catch (Exception ex) {
            log.error("reject brand application error, id: " + id, ex);
            return false;
        }

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Brand createBrand(@RequestBody Brand brand) {
        OrganizationObject existingOrg = organizationDomain.getOrganizationByName(brand.getName().trim());
        if (existingOrg == null) {

            Page<BrandObject> existingBrandList = brandDomain.getBrandList(brand.getName().trim(), null, null, null, null, null, null, null);
            if (existingBrandList.getContent().size() == 0) {
                String currentAccountId = null;
                try {
                    currentAccountId = AuthUtils.getCurrentAccount().getId();
                } catch (UnauthorizedException ex) {

                }

                BrandObject object = brand.toBrand(brand);
                object.setCreatedAccountId(currentAccountId);
                Brand returnObj = new Brand(brandDomain.createBrand(object));
                return returnObj;
            } else
                throw new ConflictException("same brand name application existed");
        } else
            throw new ConflictException("same brand name application existed");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateBrand(@PathVariable("id") String id, @RequestParam(value = "carrier", required = false) boolean inCarrier, @RequestBody Brand brand) {
        BrandObject existingBrand = brandDomain.getBrandById(id);
        if (existingBrand.getStatusCode().equals(LookupCodes.BrandApplicationStatus.APPROVED))
            throw new BadRequestException("could not update with status approved");
        if (existingBrand != null) {
            String currentAccountId = null;
            try {
                currentAccountId = AuthUtils.getCurrentAccount().getId();
            } catch (UnauthorizedException ex) {

            }

            existingBrand.setCreatedAccountId(currentAccountId);
            existingBrand.setBusinessSphere(brand.getBusinessSphere());
            existingBrand.setBusinessLicenseStart(brand.getBusinessLicenseStart());
            existingBrand.setBusinessLicenseEnd(brand.getBusinessLicenseEnd());
            existingBrand.setBusinessLicenseNumber(brand.getBusinessLicenseNumber());
            existingBrand.setDescription(brand.getDescription());
            existingBrand.setContactName(brand.getContactName());
            existingBrand.setContactMobile(brand.getContactMobile());
            existingBrand.setEmail(brand.getEmail());
            existingBrand.setComments(brand.getComments());
            existingBrand.setAttachment(brand.getAttachment());
            if (StringUtils.hasText(brand.getAttachment()) && brand.getAttachment().endsWith(","))
                existingBrand.setAttachment(brand.getAttachment().substring(0, brand.getAttachment().length() - 1));
            existingBrand.setInvestigatorComments(brand.getInvestigatorComments());
            existingBrand.setRejectReason(brand.getRejectReason());

            if (StringUtils.hasText(brand.getInvestigatorAttachment()) && brand.getInvestigatorAttachment().endsWith(","))
                existingBrand.setInvestigatorAttachment(brand.getInvestigatorAttachment().substring(0, brand.getInvestigatorAttachment().length() - 1));
            if (existingBrand.getStatusCode().equals(LookupCodes.BrandApplicationStatus.REJECTED) && !inCarrier) {
                if (StringUtils.hasText(existingBrand.getPaymentId()))
                    existingBrand.setStatusCode(LookupCodes.BrandApplicationStatus.APPROVED);
                else
                    existingBrand.setStatusCode(LookupCodes.BrandApplicationStatus.CREATED);
            }

            brandDomain.updateBrand(existingBrand);
        } else
            throw new NotFoundException("No such brand found");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Brand> getByFilter(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "carrier_id", required = false) String carrierId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "has_payment", required = false) Boolean hasPayment,
            @RequestParam(value = "search_text", required = false) String searchText,
            @RequestParam(value = "start_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime startTime,
            @RequestParam(value = "end_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime endTime,
            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable, HttpServletResponse response) {
        if (!StringUtils.hasText(searchText))
            searchText = null;
        Page<BrandObject> brandPage = brandDomain.getBrandList(name, carrierId, status, hasPayment, searchText, startTime, endTime, pageable);
        return PageUtils.response(response, brandPage.map(Brand::new), pageable != null);
    }

    @RequestMapping(value = "attachment", method = RequestMethod.POST)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:write')")
    public Attachment saveBrandAttachment(@RequestParam(value = "file") MultipartFile attachment) {
        if (attachment == null)
            throw new NotFoundException("no file uploaded!");
        AttachmentObject attachmentObject = brandDomain.createAttachment(attachment);
        return new Attachment(attachmentObject);


    }

    @RequestMapping(value = "attachment/{id}", method = RequestMethod.POST)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:write')")
    public void updateBrandAttachment(@PathVariable(value = "id") String id,
                                      @RequestParam(value = "file") MultipartFile attachment) {
        if (attachment == null)
            throw new NotFoundException("no file uploaded!");
        brandDomain.updateAttachment(id, attachment);
    }

    @RequestMapping(value = "attachment/{id}", method = RequestMethod.GET)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:write')")
    public ResponseEntity<?> getBrandAttachment(@PathVariable(value = "id") String id) throws UnsupportedEncodingException {
        List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(id);
        if (attachmentObjectList.size() <= 0)
            throw new NotFoundException("no attachment exited!");

        AttachmentObject currentObj = attachmentObjectList.get(0);

        ResourceInputStream resourceInputStream = brandDomain.getAttachment(currentObj.getS3FileName());
        if (resourceInputStream == null) {
            throw new NotFoundException("logo not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        builder.header("Content-Disposition", "attachment;filename=" + URLEncoder.encode(currentObj.getOriginalFileName(), "UTF-8"));
        return builder.body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "attachment", method = RequestMethod.GET)
    public List<Attachment> getAttachmentList(@RequestParam(value = "ids") List<String> attachmentIds) {
        return
                brandDomain.getAttachmentList(attachmentIds).stream().map(Attachment::new).collect(Collectors.toList());
    }


    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Brand login(@RequestBody AccountLoginRequest account, @RequestParam(name = "summarize", required = false) boolean summarize) {
        //validate parameters
        if (account.getAccountId() == null && (account.getOrganization() == null || account.getIdentifier() == null)) {
            log.warn(String.format("parameters are invalid [accountId: %s, organization: %s, identifier: %s]",
                    account.getAccountId(), account.getOrganization(), account.getIdentifier()));
            throw new UnauthorizedException("account is not valid");
        }

        //find account
        List<BrandObject> existingBrandList = brandDomain.getBrandList(account.getOrganization().trim(), null, null, null, null, null, null, null).getContent();
        if (existingBrandList.size() == 0)
            throw new UnauthorizedException("account is not valid");
        else {
            BrandObject brand = existingBrandList.get(0);
            if (!brand.getIdentifier().equals(account.getIdentifier()))
                throw new UnauthorizedException("account is not valid");

            //validate password
            if (!accountDomain.validatePassword(account.getPassword(), brand.getHashSalt(), brand.getPassword())) {
                throw new UnauthorizedException("account is not valid");
            }

            Brand returnObject = new Brand(brand);
            if (!summarize) {
                if (StringUtils.hasText(brand.getAttachment())) {
                    List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(brand.getAttachment());
                    returnObject.setAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
                }

                if (StringUtils.hasText(brand.getInvestigatorAttachment())) {
                    List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(brand.getInvestigatorAttachment());
                    returnObject.setInvestigatorAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
                }
            }
            return returnObject;

        }


    }

    @RequestMapping(value = "{id}/history", method = RequestMethod.GET)
    public List<BrandApplicationHistory> getHistoryById(@PathVariable("id") String id) {
        List<BrandApplicationHistory> historyList = brandDomain.getBrandApplicationHistory(id).stream().map(BrandApplicationHistory::new).collect(Collectors.toList());
        Map<String, Account> accountMap = new HashMap<>();
        historyList.forEach(history -> {
                    if (StringUtils.hasText(history.getCreatedAccountId())) {
                        if (accountMap.containsKey(history.getCreatedAccountId()))
                            history.setAccount(accountMap.get(history.getCreatedAccountId()));
                        else {
                            Account account = new Account(accountDomain.getById(history.getCreatedAccountId()));
                            history.setAccount(account);
                            accountMap.put(history.getCreatedAccountId(), account);
                        }
                    }
                }

        );

        return historyList;
    }

    @RequestMapping(value = "{id}/resetpassword", method = RequestMethod.PATCH)
    public void resetPassword(@PathVariable("id") String applicationId, @RequestBody String newPassword) {
        BrandObject object = brandDomain.getBrandById(applicationId);
        if (object == null)
            throw new NotFoundException("Brand application not find with id: " + applicationId);
        String currentAccountId = null;
        try {
            currentAccountId = AuthUtils.getCurrentAccount().getId();
        } catch (UnauthorizedException ex) {

        }

        String hashSalt = RandomUtils.generateString(8);
        String password = HashUtils.sha1HexString(newPassword + hashSalt);
        object.setCreatedAccountId(currentAccountId);
        object.setPassword(password);
        object.setHashSalt(hashSalt);

        brandDomain.updateBrand(object);

    }
}
