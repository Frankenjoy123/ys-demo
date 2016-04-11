package com.yunsoo.api.controller;

import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.AccountLoginRequest;
import com.yunsoo.api.dto.Attachment;
import com.yunsoo.api.dto.Brand;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 3/17/2016.
 */
@RestController
@RequestMapping(value="/brand")
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
    private PermissionAllocationDomain permissionAllocationDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Brand getById(@PathVariable(value = "id") String id) {
        BrandObject object = brandDomain.getBrandById(id);
        if(object == null)
            throw new NotFoundException("brand application not found by [id: " + id + "]");

        Brand returnObject = new Brand(object);

        if(StringUtils.hasText(object.getAttachment())) {
            List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(object.getAttachment());
            returnObject.setAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
        }

        return returnObject;
    }


    @RequestMapping(value = "count/created", method = RequestMethod.GET)
    public int count(@RequestParam(value = "carrier_id") String id) {
        return brandDomain.count(id, LookupCodes.BrandApplicationStatus.CREATED);
    }

    @RequestMapping(value = "{id}/approve", method = RequestMethod.PUT)
    public boolean approveBrandApplication(@PathVariable("id") String id){
        try {
            String currentAccountId = AuthUtils.getCurrentAccount().getId();
            BrandObject object = brandDomain.getBrandById(id);

            object.setCreatedAccountId(currentAccountId);
            object.setTypeCode(LookupCodes.OrgType.MANUFACTURER);
            object.setStatusCode(LookupCodes.OrgStatus.AVAILABLE);
            BrandObject createdBrand = organizationDomain.createBrand(object);

            permissionDomain.putOrgRestrictionToDefaultPermissionRegion(object.getCarrierId(), createdBrand.getId());

            AccountObject accountObject = new AccountObject();
            accountObject.setEmail(object.getEmail());
            accountObject.setIdentifier(object.getIdentifier());
            accountObject.setFirstName(object.getContactName());
            accountObject.setLastName(object.getName());
            accountObject.setPassword(object.getPassword());
            accountObject.setHashSalt(object.getHashSalt());
            accountObject.setPhone(object.getContactMobile());
            accountObject.setOrgId(createdBrand.getId());
            accountObject.setCreatedAccountId(currentAccountId);
            AccountObject createdAccount = accountDomain.createAccount(accountObject);
            permissionAllocationDomain.allocateAdminPermissionToAccount(createdAccount.getId());

            object.setStatusCode(LookupCodes.BrandApplicationStatus.APPROVED);
            brandDomain.updateBrand(object);

            return true;
        }catch (Exception ex) {
            log.error("approve brand application error, id is: " + id, ex);
            return false;
        }
    }

    @RequestMapping(value = "{id}/reject", method = RequestMethod.PUT)
    public boolean rejectBrandApplication(@PathVariable("id") String id, @RequestParam(name="comments", required = false) String comments) {
        try {
            BrandObject object = brandDomain.getBrandById(id);
            object.setStatusCode(LookupCodes.BrandApplicationStatus.REJECTED);
            if(StringUtils.hasText(comments))
                object.setComments(comments);
            brandDomain.updateBrand(object);
            return  true;
        }
        catch (Exception ex){
            log.error("reject brand application error, id: " + id, ex);
            return false;
        }

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Brand createBrand(@RequestBody Brand brand) {

        Page<BrandObject> existingBrandList = brandDomain.getBrandList(brand.getName().trim(),null,null,null);
        if(existingBrandList.getContent().size() == 0) {

            BrandObject object = brand.toBrand(brand);
            Brand returnObj = new Brand(brandDomain.createBrand(object));
            return returnObj;
        }
        else
            throw new ConflictException("same brand name application existed");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Brand> getByFilter(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "carrier_id", required = false) String carrierId,
            @RequestParam(value = "status", required = false) String status,
            Pageable pageable,

            HttpServletResponse response) {

        Page<BrandObject> brandPage = brandDomain.getBrandList(name, carrierId, status, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", brandPage.toContentRange());
        }
        return brandPage.map(Brand::new).getContent();

    }

    @RequestMapping(value = "attachment", method = RequestMethod.POST)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:write')")
    public Attachment saveBrandAttachment(@RequestParam(value = "file") MultipartFile attachment) {
        if(attachment == null)
            throw new NotFoundException("no file uploaded!");
        AttachmentObject attachmentObject = brandDomain.createAttachment(attachment);
        return new Attachment(attachmentObject);


    }

    @RequestMapping(value = "attachment/{id}", method = RequestMethod.POST)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:write')")
    public void updateBrandAttachment(@PathVariable(value = "id") String id,
                                    @RequestParam(value = "file") MultipartFile attachment) {
        if(attachment == null)
            throw new NotFoundException("no file uploaded!");
        brandDomain.updateAttachment(id, attachment);
    }

    @RequestMapping(value = "attachment/{id}", method = RequestMethod.GET)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:write')")
    public ResponseEntity<?> getBrandAttachment(@PathVariable(value = "id") String id) throws UnsupportedEncodingException {
        List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(id);
        if(attachmentObjectList.size()<=0)
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
        builder.header("Content-Disposition","filename=" + URLEncoder.encode(currentObj.getOriginalFileName(), "UTF-8") );
        return builder.body(new InputStreamResource(resourceInputStream));
    }
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Brand login(@RequestBody AccountLoginRequest account){
        //validate parameters
        if (account.getAccountId() == null && (account.getOrganization() == null || account.getIdentifier() == null)) {
            log.warn(String.format("parameters are invalid [accountId: %s, organization: %s, identifier: %s]",
                    account.getAccountId(), account.getOrganization(), account.getIdentifier()));
            throw new UnauthorizedException("account is not valid");
        }

        //find account
        List<BrandObject> existingBrandList = brandDomain.getBrandList(account.getOrganization().trim(), null,null,null).getContent();
        if(existingBrandList.size() == 0)
            throw new UnauthorizedException("account is not valid");
        else{
            BrandObject brand = existingBrandList.get(0);
            if(!brand.getIdentifier().equals(account.getIdentifier()))
                throw new UnauthorizedException("account is not valid");

            //validate password
            if (!accountDomain.validatePassword(account.getPassword(), brand.getHashSalt(), brand.getPassword())) {
                throw new UnauthorizedException("account is not valid");
            }

            Brand returnObject = new Brand(brand);

            if(StringUtils.hasText(brand.getAttachment())) {
                List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(brand.getAttachment());
                returnObject.setAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
            }

            return returnObject;

        }



    }

}
