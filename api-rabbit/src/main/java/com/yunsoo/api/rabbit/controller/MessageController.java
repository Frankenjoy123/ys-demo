package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.MessageDomain;
import com.yunsoo.api.rabbit.dto.basic.Message;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/3/9.
 * Description: Only Authorized user can consume it.
 * ErrorCode
 * 40401    :   Message Not found!
 */

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private RestClient dataAPIClient;
    //    private final String AUTH_HEADER_NAME = "YS_RABBIT_AUTH_TOKEN";

    @Autowired
    private MessageDomain messageDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    //    @RequestMapping(value = "/pushTo/{userid}/type/{typeid}", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(#message, 'message:read')")
    @PreAuthorize("hasPermission(#userid, 'UserInToken', 'message:read')")
    public List<Message> getNewMessagesByUserId(@PathVariable(value = "userid") String userid,
                                                @PathVariable(value = "type") String type) {
        if (userid == null || userid.isEmpty()) throw new BadRequestException("UserId不能为空！");
        if (type == null || type.isEmpty()) throw new BadRequestException("Type不能为空！");

        try {
            List<Message> messageList = dataAPIClient.get("message/pushto/{userid}/type/{type}", new ParameterizedTypeReference<List<Message>>() {
            }, userid, type);
            if (messageList == null || messageList.size() == 0) {
                throw new NotFoundException(40401, "Message not found for userid = " + userid);
            }
            return messageList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "Message not found for userid = " + userid);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(#message, 'message:read')")
    public Message getById(@PathVariable(value = "id") String id) throws NotFoundException {
        if (id == null) throw new BadRequestException("Id can not be null！");
        MessageObject messageObject = messageDomain.getById(id);
        if (messageObject == null) {
            throw new NotFoundException("message not found for id = " + id);
        }
        Message message = new Message(messageObject);
        message.setDetails(messageDomain.getMessageDetails(messageObject.getOrgId(), id));
        return message;
//        try {
//            Message message = dataAPIClient.get("message/{id}", Message.class, id);
//            if (message == null) throw new NotFoundException(40401, "Message not found for id = " + id);
//            return message;
//        } catch (NotFoundException ex) {
//            throw new NotFoundException(40401, "Message not found for id = " + id);
//        }
    }

    @Deprecated //todo: to be removed
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Message> getMessagesByFilter(@RequestParam(value = "type", required = true) String type,
                                             @RequestParam(value = "orgid", required = true) String orgId,
                                             @RequestParam(value = "postdatetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime postdatetime,
                                             @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

//        List<Message> messageList = dataAPIClient.get("message?type={0}&orgid={1}&status={2}&ignoreExpireDate=true&postdatetime={3}&pageIndex={4}&pageSize={5}", new ParameterizedTypeReference<List<Message>>() {
//        }, type, orgId, LookupCodes.MessageStatus.APPROVED, DateTimeUtils.toUTCString(postdatetime), pageIndex, pageSize);
//        return messageList;
        List<MessageObject> messageObjects = messageDomain.getMessageByOrgId(orgId);
        List<Message> messages = messageObjects.stream().map(Message::new).collect(Collectors.toList());
        return messages;
    }

    @RequestMapping(value = "/getunread", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#userId, 'UserInToken', 'message:read')")
    public List<Message> getUnreadMessagesBy(@RequestParam(value = "userid", required = true) String userId,
                                             @RequestParam(value = "orgid", required = true) String orgId,
                                             @RequestParam(value = "lastreadmessageid", required = true) Long lastReadMessageId) {
        if (userId == null || userId.isEmpty()) throw new BadRequestException("UserId不能为空！");
        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("OrgId不能为空！");

        //fix 404 issue
        try {
            List<Message> messageList = dataAPIClient.get("message/getunread?userid={0}&orgid={1}&lastreadmessageid={2}", new ParameterizedTypeReference<List<Message>>() {
            }, userId, orgId, lastReadMessageId);
            return messageList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "Message not found for userid = " + userId + ". orgId = " + orgId + ". lastReadMessageId = " + lastReadMessageId);
        }

    }

    @Deprecated //todo: to be removed
    @RequestMapping(value = "/{id}/{imagekey}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(@PathVariable(value = "id") String id,
                                          @PathVariable(value = "imagekey") String imagekey) {
        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("imagekey不能为空！");
        return getMessageImage(id, imagekey);
//        try {
//            FileObject fileObject = dataAPIClient.get("message/{id}/{imagekey}", FileObject.class, id, imagekey);
//            if (fileObject.getLength() > 0) {
//                return ResponseEntity.ok()
//                        .contentLength(fileObject.getLength())
//                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
//                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
//            } else {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
//                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
//            }
//        } catch (NotFoundException ex) {
//            throw new NotFoundException(40402, "找不到消息图片 imagekey = " + imagekey);
//        }
    }

    @RequestMapping(value = "{id}/image/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageImage(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "image_name") String imageName) {
        if (id == null) {
            throw new BadRequestException("message id should be valid");
        }
        MessageObject messageObject = messageDomain.getById(id);
        if (messageObject == null) {
            throw new NotFoundException("message can not be found");
        }
        ResourceInputStream resourceInputStream = messageDomain.getMessageImage(messageObject.getOrgId(), id, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("message image found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));

    }
}