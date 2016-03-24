package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.BrandDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.Attachment;
import com.yunsoo.api.dto.Brand;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private OrganizationDomain organizationDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Brand getById(@PathVariable(value = "id") String id) {
        BrandObject object = brandDomain.getBrandById(id);
        if(object == null)
            throw new NotFoundException("brand application not found by [id: " + id + "]");
        return new Brand(object);
    }

    @RequestMapping(value = "{id}/approve", method = RequestMethod.PUT)
    public boolean approveBrandApplication(@PathVariable("id") String id){
        try {
            String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
            BrandObject object = brandDomain.getBrandById(id);

            object.setCreatedAccountId(currentAccountId);
            object.setTypeCode(LookupCodes.OrgType.MANUFACTURER);
            object.setStatusCode(LookupCodes.OrgStatus.AVAILABLE);
            BrandObject createdBrand = organizationDomain.createBrand(object);

            AccountObject accountObject = new AccountObject();
            accountObject.setEmail(object.getEmail());
            accountObject.setIdentifier("admin");
            accountObject.setFirstName(object.getContactName());
            accountObject.setLastName(object.getName());
            accountObject.setPassword("admin");
            accountObject.setPhone(object.getContactMobile());
            accountObject.setOrgId(createdBrand.getId());
            accountObject.setCreatedAccountId(currentAccountId);
            accountDomain.createAccount(accountObject);

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
        BrandObject object = brand.toBrand(brand);
        Brand returnObj = new Brand(brandDomain.createBrand(object));
        return returnObj;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Brand> getByFilter(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "carrier_id", required = false) String carrierId,
            Pageable pageable,

            HttpServletResponse response) {

        Page<BrandObject> brandPage = brandDomain.getBrandList(name, carrierId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", brandPage.toContentRange());
        }
        return brandPage.map(Brand::new).getContent();

    }

    @RequestMapping(value = "attachment", method = RequestMethod.POST)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:modify')")
    public Attachment saveBrandAttachment(@RequestParam(value = "file") MultipartFile attachment) {
        if(attachment == null)
            throw new NotFoundException("no file uploaded!");
        AttachmentObject attachmentObject = brandDomain.createAttachment(attachment);
        return new Attachment(attachmentObject);


    }

    @RequestMapping(value = "attachment/{id}", method = RequestMethod.POST)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:modify')")
    public void updateBrandAttachment(@PathVariable(value = "id") String id,
                                    @RequestParam(value = "file") MultipartFile attachment) {
        if(attachment == null)
            throw new NotFoundException("no file uploaded!");
        brandDomain.updateAttachment(id, attachment);
    }

    @RequestMapping(value = "attachment/{id}", method = RequestMethod.GET)
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:modify')")
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

}
