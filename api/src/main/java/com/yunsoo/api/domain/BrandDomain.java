package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by yan on 3/17/2016.
 */
@Component
public class BrandDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

    public BrandObject createBrand(BrandObject object) {
        object.setId(null);
        object.setCreatedDateTime(DateTime.now());
        String hashSalt = RandomUtils.generateString(8);
        String password =  HashUtils.sha1HexString(object.getPassword() + hashSalt);
        object.setHashSalt(hashSalt);
        object.setPassword(password);
        if(object.getAttachment().endsWith(","))
            object.setAttachment(object.getAttachment().substring(0, object.getAttachment().length() -1 ));
        return dataAPIClient.post("brand", object, BrandObject.class);
    }

    public void updateBrand(BrandObject object) {
        dataAPIClient.put("brand", object, BrandObject.class);
    }

    public BrandObject getBrandById(String id) {
        try {
            return dataAPIClient.get("brand/{id}", BrandObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public int count(String id, String status){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("carrier_id", id)
                .append("status", status)
                .build();
        return dataAPIClient.get("brand/count/" + query , Integer.class);
    }

    public Page<BrandObject> getBrandList(String name, String carrierId, String status, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("name", name)
                .append("status", status)
                .append("carrier_id", carrierId).append(pageable)
                .build();
        return dataAPIClient.getPaged("brand" + query, new ParameterizedTypeReference<List<BrandObject>>() {
        }, name);
    }


    public List<AttachmentObject> getAttachmentList(String attachmentIds){
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("ids", StringUtils.commaDelimitedListToStringArray(attachmentIds)).build();
        return dataAPIClient.get("brand/attachment" + query, new ParameterizedTypeReference<List<AttachmentObject>>(){});
    }

    public AttachmentObject createAttachment(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String s3FileName = "attachment/" + RandomUtils.generateStringWithDate();
        try {
            ResourceInputStream stream = new ResourceInputStream(file.getInputStream(), file.getSize(), file.getContentType());
            dataAPIClient.put("file/s3?path=" + s3FileName, stream);

            AttachmentObject object = new AttachmentObject();
            object.setS3FileName(s3FileName);
            object.setOriginalFileName(fileName);
            object.setCreatedDateTime(DateTime.now());
            AttachmentObject savedObj = dataAPIClient.post("brand/attachment", object, AttachmentObject.class);
            return savedObj;

        }
        catch (IOException e) {
            throw new InternalServerErrorException("logo upload failed for brand application");
        }
    }


    public ResourceInputStream getAttachment(String fileName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path={fileName}", fileName);
        } catch (NotFoundException ex) {
            return null;
        }

    }

    public void updateAttachment(String attachmentId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String s3FileName = "attachment/" + RandomUtils.generateStringWithDate();
        try {
            ResourceInputStream stream = new ResourceInputStream(file.getInputStream(), file.getSize(), file.getContentType());
            dataAPIClient.put("file/s3?path=" + s3FileName, stream);

            AttachmentObject object = new AttachmentObject();
            object.setS3FileName(s3FileName);
            object.setOriginalFileName(fileName);
            object.setId(attachmentId);
            object.setModifiedDateTime(DateTime.now());
            dataAPIClient.put("brand/attachment", object);
        }
        catch (IOException e) {
            throw new InternalServerErrorException("logo upload failed for brand application");
        }
    }

}
