package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/12
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class OrganizationDomain {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataApiClient;

    private static final String ORG_LOGO_IMAGE_128X128 = "image-128x128";

    private static final String ORG_LOGO_IMAGE_200X200 = "image-200x200";


    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).ORGANIZATION.toString(), #id)")
    public OrganizationObject getOrganizationById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        try {
            return dataApiClient.get("organization/{id}", OrganizationObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public BrandObject getBrandById(String id) {
        try {
            return dataApiClient.get("organization/brand/{id}", BrandObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public int countBrand(String id, String status) {
        if (StringUtils.hasText(status))
            return dataApiClient.get("organization/{id}/brand/count?status={status}", Integer.class, id, status);
        else
            return dataApiClient.get("organization/{id}/brand/count", Integer.class, id);
    }

    @CacheEvict(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).ORGANIZATION.toString(), #id)")
    public void updateOrganizationStatus(String id, String status) {
        OrganizationObject org = getOrganizationById(id);
        org.setStatusCode(status);
        dataApiClient.put("organization/{id}", org, id);
    }

    @CacheEvict(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).ORGANIZATION.toString(), #id)")
    public void patchBrand(String id, BrandObject brand) {
        dataApiClient.patch("organization/brand/{id}", brand, id);
    }

    public OrganizationObject getOrganizationByName(String name) {
        List<OrganizationObject> objects = dataApiClient.get("organization?name={name}", new ParameterizedTypeReference<List<OrganizationObject>>() {
        }, name);
        return objects.size() == 0 ? null : objects.get(0);
    }

    public Page<OrganizationObject> getOrganizationList(Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("organization" + query, new ParameterizedTypeReference<List<OrganizationObject>>() {
        });
    }

    public Page<BrandObject> getOrgBrandList(String orgId, String orgName, String orgStatus, String searchText, DateTime startTime, DateTime endTime, String categoryId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("name", orgName).append("status", orgStatus).append("search_text", searchText)
                .append("start_datetime", startTime).append("end_datetime", endTime)
                .append("category_id", categoryId)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("organization/{id}/brand" + query, new ParameterizedTypeReference<List<BrandObject>>() {
        }, orgId);
    }

    public Page<BrandObject> getOrgBrandListByName(String orgId, String orgName, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("name", orgName)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("organization/{id}/brand/name" + query, new ParameterizedTypeReference<List<BrandObject>>() {
        }, orgId);
    }


    public List<String> getBrandIdsForCarrier(String carrierId) {
        return dataApiClient.get("organization/{id}/brandIds", new ParameterizedTypeReference<List<String>>() {
        }, carrierId);
    }

    public OrganizationObject createOrganization(OrganizationObject object) {
        object.setId(null);
        object.setCreatedDateTime(DateTime.now());
        return dataApiClient.post("organization", object, OrganizationObject.class);
    }

    public BrandObject createBrand(BrandObject object) {
        OrganizationObject existingOrg = getOrganizationByName(object.getName().trim());
        if(existingOrg!=null)
            throw new ConflictException("same brand name application existed");
        else {
            object.setId(null);
            object.setCreatedDateTime(DateTime.now());
            object.setTypeCode(LookupCodes.OrgType.BRAND);
            object.setStatusCode(LookupCodes.OrgStatus.AVAILABLE);
            if (StringUtils.hasText(object.getAttachment()) && object.getAttachment().endsWith(","))
                object.setAttachment(object.getAttachment().substring(0, object.getAttachment().length() - 1));
            return dataApiClient.post("organization/brand", object, BrandObject.class);
        }
    }

    public ResourceInputStream getLogoImage(String orgId, String imageName) {
        try {
            return dataApiClient.getResourceInputStream("file/s3?path=organization/{orgId}/logo/{imageName}", orgId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }

    }

    public void saveOrgLogo(String orgId, byte[] imageDataBytes) {
        saveOrgLogo(orgId, new ByteArrayInputStream(imageDataBytes));
    }

    public void saveOrgLogo(String orgId, InputStream logoStream) {
        String logoImage128x128 = ORG_LOGO_IMAGE_128X128;
        String logoImage200x200 = ORG_LOGO_IMAGE_200X200;
        try {
            //128x128
            ImageProcessor imageProcessor = new ImageProcessor().read(logoStream);
            ByteArrayOutputStream logo128x128OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(128, 128).write(logo128x128OutputStream, "image/png");
            dataApiClient.put("file/s3?path=organization/{orgId}/logo/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(logo128x128OutputStream.toByteArray()), logo128x128OutputStream.size(), "image/png"),
                    orgId, logoImage128x128);
            //200x200
            ByteArrayOutputStream logo200x200OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(200, 200).write(logo200x200OutputStream, "image/png");
            dataApiClient.put("file/s3?path=organization/{orgId}/logo/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(logo200x200OutputStream.toByteArray()), logo200x200OutputStream.size(), "image/png"),
                    orgId, logoImage200x200);
        } catch (IOException e) {
            throw new InternalServerErrorException("logo upload failed [orgId: " + orgId + "]");
        }
    }

    public void saveOrgLogo(String orgId, String imageName, byte[]imageDataBytes){
        try {
            ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));
            ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
            imageProcessor.write(imageOutputStream, "image/png");
            dataApiClient.put("file/s3?path=organization/{orgId}/logo/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(imageOutputStream.toByteArray()), imageOutputStream.size(), "image/png"),
                    orgId, imageName);
            log.info(String.format("image saved [imageName: %s]", imageName));


        } catch (IOException e) {
            throw new InternalServerErrorException("logo upload failed [orgId: " + orgId + "], imageName: " + imageName);
        }

    }


    public void saveWebChatKey(String orgId, MultipartFile file, String fileName) {
        String s3FileName = "organization/{orgId}/webchat/" + fileName;
        try {
            ResourceInputStream stream = new ResourceInputStream(file.getInputStream(), file.getSize(), file.getContentType());
            dataApiClient.put("file/s3?path=" + s3FileName, stream, orgId);
        }
        catch (IOException e) {
            throw new InternalServerErrorException("webchat key upload failed for organization: " + orgId);
        }
    }

}
