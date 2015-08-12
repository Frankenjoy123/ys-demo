package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.config.AWSConfigProperties;
import com.yunsoo.data.service.entity.MessageEntity;
import com.yunsoo.data.service.repository.MessageRepository;
import com.yunsoo.data.service.service.MessageService;
import com.yunsoo.data.service.service.contract.Message;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private final MessageService messageService;

    @Autowired
    private AWSConfigProperties awsConfigProperties;

    @Autowired
    MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //Push unread messages to user.
    @RequestMapping(value = "/pushto/{userid}/type/{typeid}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageObject>> getNewMessagesByUserId(@PathVariable(value = "userid") String id,
                                                                      @PathVariable(value = "type") String type,
                                                                      @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<Message> messageList = messageService.getMessagesByFilter(type, LookupCodes.MessageStatus.APPROVED, null, true, null, pageIndex, pageSize); //push approved message only
        List<MessageObject> messageObjectList = this.FromMessageList(messageList);
        return new ResponseEntity<List<MessageObject>>(messageObjectList, HttpStatus.OK);
    }


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
    @RequestMapping(value = "/getUnread", method = RequestMethod.GET)
    public ResponseEntity<List<MessageObject>> getUnreadMessagesBy(@RequestParam(value = "userid", required = true) String userId,
                                                                   @RequestParam(value = "orgid", required = true) String orgId,
                                                                   @RequestParam(value = "lastreadmessageid", required = true) Long lastReadMessageId) {
        List<Message> messageList = messageService.getUnreadMessages(userId, orgId, lastReadMessageId);
        List<MessageObject> messageObjectList = this.FromMessageList(messageList);
        return new ResponseEntity<List<MessageObject>>(messageObjectList, HttpStatus.OK);
    }

    @RequestMapping(value = "/count/on", method = RequestMethod.GET)
    public int getNewMessagesByMessageId(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "type_code_in", required = false) List<String> typeCodeIn,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
            @RequestParam(value = "post_show_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime postShowTime) {
        int countMessage = messageService.countMessage(typeCodeIn, statusCodeIn, orgId, postShowTime);
        return countMessage;
    }


    @RequestMapping(value = "/{id}/{imagekey}", method = RequestMethod.GET)
    public ResponseEntity getThumbnail(@PathVariable(value = "id") String id, @PathVariable(value = "imagekey") String imagekey) {

        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("ImageKey不能为空！");
        S3Object s3Object;
        try {
            s3Object = messageService.getMessageImage(awsConfigProperties.getS3().getBucketName(), "message/" + id + "/" + imagekey);
            if (s3Object == null) throw new NotFoundException("找不到图片!");

            FileObject fileObject = new FileObject();
            fileObject.setContentType(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);

        } catch (IOException ex) {
            //to-do: log
            throw new InternalServerErrorException("图片获取出错！");
        }
    }

    private MessageObject FromMessage(Message message) {
        MessageObject messageObject = new MessageObject();
        BeanUtils.copyProperties(message, messageObject);
        return messageObject;
    }

    private Message ToMessage(MessageObject messageObject) {
        Message message = new Message();
        BeanUtils.copyProperties(messageObject, message);
        return message;
    }

    private List<MessageObject> FromMessageList(List<Message> messageList) {
        if (messageList == null) return null;

        List<MessageObject> messageObjectList = new ArrayList<>();
        for (Message message : messageList) {
            messageObjectList.add(this.FromMessage(message));
        }
        return messageObjectList;
    }

    private List<Message> ToMessageList(List<MessageObject> messageObjectList) {
        if (messageObjectList == null) return null;

        List<Message> messageList = new ArrayList<>();
        for (MessageObject messageObject : messageObjectList) {
            messageList.add(this.ToMessage(messageObject));
        }
        return messageList;
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
