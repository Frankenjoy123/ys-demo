package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AttachmentDomain;
import com.yunsoo.api.dto.Attachment;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-09
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentDomain attachmentDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getBrandAttachment(@PathVariable(value = "id") String id) throws UnsupportedEncodingException {
        AttachmentObject attachmentObject = attachmentDomain.getAttachmentById(id);
        if (attachmentObject == null) {
            throw new NotFoundException("attachment not found");
        }
        ResourceInputStream resourceInputStream = attachmentDomain.getAttachmentFileByIdAndPath(attachmentObject.getId(), attachmentObject.getS3FileName());
        if (resourceInputStream == null) {
            throw new NotFoundException("attachment not found");
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        builder.header("Content-Disposition", "attachment;filename=" + URLEncoder.encode(attachmentObject.getOriginalFileName(), "UTF-8"));
        return builder.body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void updateBrandAttachment(@PathVariable(value = "id") String id,
                                      @RequestParam(value = "file") MultipartFile attachment) {
        AttachmentObject attachmentObject = attachmentDomain.getAttachmentById(id);
        if (attachmentObject == null) {
            throw new NotFoundException("attachment not found");
        }
        attachmentDomain.updateAttachment(id, attachment);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Attachment> getAttachmentList(@RequestParam(value = "ids") List<String> attachmentIds) {
        return attachmentDomain.getAttachmentList(attachmentIds).stream().map(Attachment::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Attachment createBrandAttachment(@RequestParam(value = "file") MultipartFile attachment) {
        AttachmentObject attachmentObject = attachmentDomain.createAttachment(attachment);
        return new Attachment(attachmentObject);
    }

}
