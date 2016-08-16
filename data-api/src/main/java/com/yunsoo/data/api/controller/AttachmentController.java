package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.AttachmentEntity;
import com.yunsoo.data.service.repository.AttachmentRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-05
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public AttachmentObject getAttachmentById(@PathVariable("id") String id) {
        AttachmentEntity entity = attachmentRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("attachment not found by id: " + id);
        }
        return toAttachmentObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AttachmentObject> getAttachments(@RequestParam("ids") List<String> ids) {
        List<AttachmentEntity> entities = attachmentRepository.findByIdIn(ids);
        return entities.stream().map(this::toAttachmentObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public AttachmentObject createAttachment(@RequestBody AttachmentObject attachment) {
        AttachmentEntity entity = toAttachmentEntity(attachment);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        attachmentRepository.save(entity);
        return toAttachmentObject(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public AttachmentObject updateAttachment(@PathVariable("id") String id,
                                             @RequestBody AttachmentObject attachment) {
        AttachmentEntity entity = attachmentRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("attachment not found by id: " + id);
        }
        entity.setOriginalFileName(attachment.getOriginalFileName());
        entity.setS3FileName("");
        entity.setModifiedDateTime(DateTime.now());
        attachmentRepository.save(entity);
        return toAttachmentObject(entity);
    }


    private AttachmentEntity toAttachmentEntity(AttachmentObject obj) {
        if (obj == null) {
            return null;
        }
        AttachmentEntity entity = new AttachmentEntity();
        entity.setId(obj.getId());
        entity.setOriginalFileName(obj.getOriginalFileName());
        entity.setS3FileName("");
        entity.setCreatedDateTime(obj.getCreatedDateTime());
        entity.setModifiedDateTime(obj.getModifiedDateTime());
        return entity;
    }

    private AttachmentObject toAttachmentObject(AttachmentEntity entity) {
        if (entity == null)
            return null;

        AttachmentObject obj = new AttachmentObject();
        obj.setId(entity.getId());
        obj.setOriginalFileName(entity.getOriginalFileName());
        obj.setS3FileName(entity.getS3FileName());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        obj.setModifiedDateTime(entity.getModifiedDateTime());
        return obj;
    }
}
