package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.MessageEntity;
import com.yunsoo.data.service.repository.MessageRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

//import org.apache.http.HttpStatus;

/**
 * Created by Zhe on 2015/1/26.
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    //get message by id
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public MessageObject getById(@PathVariable(value = "id") String id) {
        MessageEntity entity = messageRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("message not found by [id:" + id + "]");
        }
        return toMessageObject(entity);
    }

    //query
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<MessageObject> getByFilter(@RequestParam(value = "org_id") String orgId,
                                           Pageable pageable,
                                           HttpServletResponse response) {
        Page<MessageEntity> entityPage = messageRepository.findByOrgId(orgId, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.getContent().stream()
                .map(this::toMessageObject)
                .collect(Collectors.toList());
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageObject create(@RequestBody MessageObject messageObject) {
        MessageEntity entity = toMessageEntity(messageObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        MessageEntity newEntity = messageRepository.save(entity);
        return toMessageObject(newEntity);
    }

    //update
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void update(@PathVariable(value = "id") String id,
                       @RequestBody MessageObject messageObject) {
        MessageEntity oldentity = messageRepository.findOne(id);
        if (oldentity != null) {
            MessageEntity entity = toMessageEntity(messageObject);
            messageRepository.save(entity);
        }
    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        MessageEntity entity = messageRepository.findOne(id);
        if (entity != null) {
            messageRepository.delete(id);
        }
    }

    @RequestMapping(value = "/count/on", method = RequestMethod.GET)
    public Long getNewMessagesByMessageId(
            @RequestParam(value = "org_id") String orgId,
            @RequestParam(value = "type_code_in", required = false) List<String> typeCodeIn,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {
        // int countMessage = messageService.countMessage(typeCodeIn, statusCodeIn, orgId, postShowTime);

        if (typeCodeIn == null) {
            if (statusCodeIn == null) {
                return messageRepository.countByOrgId(orgId);
            } else {
                return messageRepository.countByOrgIdAndStatusCodeIn(orgId, statusCodeIn);
            }
        } else {
            if (statusCodeIn == null) {
                return messageRepository.countByOrgIdAndTypeCodeIn(orgId, typeCodeIn);
            } else {
                return messageRepository.countByOrgIdAndTypeCodeInAndStatusCodeIn(orgId, typeCodeIn, statusCodeIn);
            }
        }
        //  return countMessage;
    }

    private MessageObject toMessageObject(MessageEntity entity) {
        if (entity == null) {
            return null;
        }
        MessageObject object = new MessageObject();
        object.setId(entity.getId());
        object.setOrgId(entity.getOrgId());
        object.setTitle(entity.getTitle());
        object.setStatusCode(entity.getStatusCode());
        object.setTypeCode(entity.getTypeCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private MessageEntity toMessageEntity(MessageObject object) {
        if (object == null) {
            return null;
        }
        MessageEntity entity = new MessageEntity();
        entity.setId(object.getId());
        entity.setOrgId(object.getOrgId());
        entity.setTitle(object.getTitle());
        entity.setStatusCode(object.getStatusCode());
        entity.setTypeCode(object.getTypeCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }
}
