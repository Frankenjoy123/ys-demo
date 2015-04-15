package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.config.AmazonSetting;
import com.yunsoo.data.service.config.DataServiceSetting;
import com.yunsoo.data.service.service.MessageService;
import com.yunsoo.data.service.service.contract.Message;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//import org.apache.http.HttpStatus;

/**
 * Created by Zhe on 2015/1/26.
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private final MessageService messageService;

    @Autowired
    private AmazonSetting amazonSetting;

    @Autowired
    private DataServiceSetting dataServiceSetting;

    @Autowired
    MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //Push unread messages to user.
    @RequestMapping(value = "/pushto/{userid}/type/{typeid}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getNewMessagesByUserId(@PathVariable(value = "userid") Long id,
                                                                @PathVariable(value = "typeid") Integer typeid) {
        List<Message> messageList = messageService.getMessagesByFilter(typeid, dataServiceSetting.getMessage_approved_status_id(), null, true); //push approved message only
        return new ResponseEntity<List<Message>>(messageList, HttpStatus.OK);
    }

    //Get Message by Id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Message> getNewMessagesByMessageId(@PathVariable(value = "id") Integer id) {
        Message message = messageService.get(id);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessagesByFilter(@RequestParam(value = "type", required = false) Integer type,
                                                             @RequestParam(value = "status", required = false) Integer status,
                                                             @RequestParam(value = "companyid", required = false) Long companyId,
                                                             @RequestParam(value = "ignoreexpiredate", required = false, defaultValue = "true") boolean ignoreExpireDate) {
        List<Message> messageList = messageService.getMessagesByFilter(type, status, companyId, ignoreExpireDate);
        return new ResponseEntity<List<Message>>(messageList, HttpStatus.OK);
    }

    @RequestMapping(value = "/getUnread", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getUnreadMessagesBy(@RequestParam(value = "userid", required = true) Long userId,
                                                             @RequestParam(value = "companyid", required = true) Long companyId,
                                                             @RequestParam(value = "lastreadmessageid", required = true) Long lastReadMessageId) {
        List<Message> messageList = messageService.getUnreadMessages(userId, companyId, lastReadMessageId);
        return new ResponseEntity<List<Message>>(messageList, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> createMessages(@RequestBody Message message) {
        long id = messageService.save(message);
        return new ResponseEntity<Long>(id, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public ResponseEntity updateMessages(@RequestBody Message message) {
        message.setLastUpatedDateTime(DateTime.now());
        messageService.patchUpdate(message);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessages(@PathVariable(value = "id") Long id) {
        messageService.delete(id);
    }

    @RequestMapping(value = "/image/{imagekey}", method = RequestMethod.GET)
    public ResponseEntity getThumbnail(
            @PathVariable(value = "imagekey") String imagekey) {

        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("ImageKey不能为空！");
        S3Object s3Object;
        try {
            s3Object = messageService.getMessageImage(amazonSetting.getS3_basebucket(), amazonSetting.getS3_message_image_url() + "/" + imagekey);
            if (s3Object == null) throw new NotFoundException("找不到图片!");

            FileObject fileObject = new FileObject();
            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
            fileObject.setThumbnailData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLenth(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);

        } catch (IOException ex) {
            //to-do: log
            throw new InternalServerErrorException("图片获取出错！");
        }
    }
}
