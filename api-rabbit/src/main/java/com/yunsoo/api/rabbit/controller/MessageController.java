package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.dto.basic.Message;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.util.DateTimeUtils;
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
    //    private final String AUTH_HEADER_NAME = "YS_RABBIT_AUTH_TOKEN";
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Message> getMessagesByFilter(@RequestParam(value = "type", required = true) String type,
                                             @RequestParam(value = "orgid", required = true) String orgId,
                                             @RequestParam(value = "postdatetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime postdatetime,
                                             @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        List<Message> messageList = dataAPIClient.get("message?type={0}&orgid={1}&status={2}&ignoreExpireDate=true&postdatetime={3}&pageIndex={4}&pageSize={5}", new ParameterizedTypeReference<List<Message>>() {
        }, type, orgId, LookupCodes.MessageStatus.APPROVED, DateTimeUtils.toUTCString(postdatetime), pageIndex, pageSize);
        return messageList;
    }

    @RequestMapping(value = "/getunread", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#userId, 'UserInToken', 'message:read')")
    public List<Message> getUnreadMessagesBy(@RequestParam(value = "userid", required = true) String userId,
                                             @RequestParam(value = "orgid", required = true) String orgId,
                                             @RequestParam(value = "lastreadmessageid", required = true) Long lastReadMessageId) {
        if (userId == null || userId.isEmpty()) throw new BadRequestException("UserId不能为空！");
        if (orgId == null || orgId.isEmpty()) throw new BadRequestException("OrgId不能为空！");
//        if (lastReadMessageId == null || lastReadMessageId < 0) throw new BadRequestException("lastReadMessageId不能小于0！");
//        if (!userDomain.validateToken(token, userId)) {
//            throw new UnauthorizedException("不能读取其他用户的收藏信息！");
//        }
        try {
            List<Message> messageList = dataAPIClient.get("message/getunread?userid={0}&orgid={1}&lastreadmessageid={2}", new ParameterizedTypeReference<List<Message>>() {
            }, userId, orgId, lastReadMessageId);
            if (messageList == null || messageList.size() == 0) {
                throw new NotFoundException("Message not found!");
            }
            return messageList;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "Message not found for userid = " + userId + ". orgId = " + orgId + ". lastReadMessageId = " + lastReadMessageId);
        }

    }

    @RequestMapping(value = "/{id}/{imagekey}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(@PathVariable(value = "id") String id,
                                          @PathVariable(value = "imagekey") String imagekey) {
        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("imagekey不能为空！");
        try {
            FileObject fileObject = dataAPIClient.get("message/{id}/{imagekey}", FileObject.class, id, imagekey);
            if (fileObject.getLength() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLength())
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "找不到消息图片 imagekey = " + imagekey);
        }
    }
}