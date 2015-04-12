package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.dto.basic.Message;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by Zhe on 2015/3/9.
 * Description: Only Authorized user can consume it.
 * ErrorCode
 * 40401    :   Message Not found!
 */

@RestController
@RequestMapping("/message")
public class MessageController {

    private RestClient dataAPIClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/pushTo/{userid}", method = RequestMethod.GET)
    public List<Message> getNewMessagesByUserId(@PathVariable(value = "userid") Long userid) {
        if (userid == null || userid <= 0) throw new BadRequestException("UserId不能小于0！");
        try {
            List<Message> messageList = dataAPIClient.get("message/pushto", List.class, userid);
            if (messageList == null || messageList.size() == 0) {
                throw new NotFoundException(40401, "Message not found for userid = " + userid);
            }
            return messageList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "Message not found for userid = " + userid);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Message getById(@PathVariable(value = "id") Integer id) throws NotFoundException {
        if (id == null || id <= 0) throw new BadRequestException("Id不能小于0！");
        try {
            Message message = dataAPIClient.get("message/{id}", Message.class, id);
            if (message == null) throw new NotFoundException(40401, "Message not found for id = " + id);
            return message;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "Message not found for id = " + id);
        }
    }

    @RequestMapping(value = "/getunread", method = RequestMethod.GET)
    public List<Message> getUnreadMessagesBy(@RequestParam(value = "userid", required = true) Long userId,
                                             @RequestParam(value = "companyid", required = true) Long companyId,
                                             @RequestParam(value = "lastreadmessageid", required = true) Long lastReadMessageId) {
        if (userId == null || userId <= 0) throw new BadRequestException("UserId不能小于0！");
        if (companyId == null || companyId <= 0) throw new BadRequestException("CompanyId不能小于0！");
        try {
            List<Message> messageList = dataAPIClient.get("message/getunread?userid={0}&companyid={1}&lastreadmessageid={2}", List.class, userId, companyId, lastReadMessageId);
            if (messageList == null || messageList.size() == 0) {
                throw new NotFoundException("Message not found!");
            }
            return messageList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "Message not found for userid = " + userId + ". companyId = " + companyId + ". lastReadMessageId = " + lastReadMessageId);
        }

    }

    @RequestMapping(value = "/image/{imagekey}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "imagekey") String imagekey) {
        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("imagekey不能为空！");
        try {
            FileObject fileObject = dataAPIClient.get("message/image/{imagekey}", FileObject.class, imagekey);
            if (fileObject.getLenth() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLenth())
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "找不到消息图片 imagekey = " + imagekey);
        }
    }

}