package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataApiClient;
import com.yunsoo.api.file.service.FileService;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-09
 * Descriptions:
 */
@Component
public class AttachmentDomain {

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    private FileService fileService;


    public AttachmentObject getAttachmentById(String attachmentId) {
        if (StringUtils.isEmpty(attachmentId)) {
            return null;
        }
        try {
            return dataApiClient.get("attachment/{0}", AttachmentObject.class, attachmentId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public List<AttachmentObject> getAttachmentList(String attachmentIds) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("ids", StringUtils.commaDelimitedListToStringArray(attachmentIds)).build();
        return dataApiClient.get("attachment" + query, new ParameterizedTypeReference<List<AttachmentObject>>() {
        });
    }

    public List<AttachmentObject> getAttachmentList(List<String> attachmentIds) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("ids", attachmentIds).build();
        return dataApiClient.get("attachment" + query, new ParameterizedTypeReference<List<AttachmentObject>>() {
        });
    }

    public AttachmentObject createAttachment(MultipartFile file) {
        AttachmentObject obj = new AttachmentObject();
        obj.setOriginalFileName(file.getOriginalFilename());
        obj.setCreatedDateTime(DateTime.now());
        obj = dataApiClient.post("attachment", obj, AttachmentObject.class);

        String path = formatPath(obj.getId());
        try {
            ResourceInputStream stream = new ResourceInputStream(file.getInputStream(), file.getSize(), file.getContentType());
            fileService.putFile(path, stream);
        } catch (IOException e) {
            throw new InternalServerErrorException("upload attachment failed");
        }
        return obj;
    }


    public void updateAttachment(String attachmentId, MultipartFile file) {
        AttachmentObject obj = getAttachmentById(attachmentId);
        if (obj == null) {
            throw new NotFoundException("attachment not found by id: " + attachmentId);
        }

        String path = formatPath(obj.getId());
        try {
            ResourceInputStream stream = new ResourceInputStream(file.getInputStream(), file.getSize(), file.getContentType());
            fileService.putFile(path, stream);
        } catch (IOException e) {
            throw new InternalServerErrorException("upload attachment failed");
        }
        obj.setOriginalFileName(file.getOriginalFilename());
        obj.setModifiedDateTime(DateTime.now());
        dataApiClient.put("attachment/{0}", obj, attachmentId);
    }

    public ResourceInputStream getAttachmentFileById(String attachmentId) {
        String path = formatPath(attachmentId);
        if (path == null) {
            return null;
        }
        return fileService.getFile(path);
    }

    //tobe deleted on next release
    public ResourceInputStream getAttachmentFileByIdAndPath(String attachmentId, String oldPath) {
        if (StringUtils.isEmpty(oldPath)) {
            return getAttachmentFileById(attachmentId);
        }
        ResourceInputStream file = fileService.getFile(oldPath);
        if (file != null) {
            fileService.putFile(formatPath(attachmentId), file);
            AttachmentObject obj = getAttachmentById(attachmentId);
            if (obj != null) {
                obj.setS3FileName(null);
                dataApiClient.put("attachment/{0}", obj, attachmentId);
            }
        }
        return file;
    }

    private String formatPath(String attachmentId) {
        if (!StringUtils.hasText(attachmentId)) {
            return null;
        }
        return String.format("attachment/%s", attachmentId);
    }

}
