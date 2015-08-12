package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.MessageDomain;
import com.yunsoo.api.dto.Message;
import com.yunsoo.api.dto.MessageImageRequest;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by Zhe on 2015/3/9.
 * <p>
 * ErrorCode
 * 40401    :   Message Not found!
 */

@RestController
@RequestMapping("/message")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private MessageDomain messageDomain;

    @Autowired
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'message:read')")
    public Message getById(@PathVariable(value = "id") String id) {
        MessageObject messageObject = findMessageById(id);
        if (messageObject == null) {
            throw new NotFoundException("message not found");
        }
        Message message = new Message(messageObject);
        message.setDetails(messageDomain.getMessageDetails(messageObject.getOrgId(), id));
        return new Message(messageObject);
    }

    //query by org id
    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'message:read')")
    public List<Message> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                     Pageable pageable,
                                     HttpServletResponse response) {
        orgId = fixOrgId(orgId);
        Page<MessageObject> messagePage = messageDomain.getMessageByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", messagePage.toContentRange());
        }
        List<Message> messages = messagePage.map(p -> {
            Message message = new Message(p);
            return message;
        }).getContent();

        return messages;
    }


    @RequestMapping(value = "/count/on", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'message:read')")
    public int getNewMessagesByMessageId(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "type_code_in", required = false) List<String> typeCodeIn,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
            @RequestParam(value = "post_show_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime postShowTime) {

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("type_code_in", typeCodeIn)
                .append("status_code_in", statusCodeIn)
                .append("post_show_time", postShowTime)
                .build();

        int countMessage = dataAPIClient.get("message/count/on" + query, int.class);
        return countMessage;
    }

    //create message
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#message.orgId, 'filterByOrg', 'message:create')")
    public MessageObject createMessage() {
        MessageObject messageObject = new MessageObject();
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        messageObject.setOrgId(orgId);
        messageObject.setTitle("");
        messageObject.setStatusCode(LookupCodes.MessageStatus.CREATED);
        messageObject.setTypeCode(LookupCodes.MessageType.BUSINESS);
        messageObject.setCreatedAccountId(currentAccountId);
        messageObject.setCreatedDateTime(DateTime.now());
        messageObject.setModifiedAccountId(null);
        messageObject.setModifiedDateTime(null);
        return messageDomain.createMessage(messageObject);
    }

    //update message
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#message.orgId, 'filterByOrg', 'message:create')")
    public Message updateMessage(@PathVariable(value = "id") String id,
                                 @RequestBody @Valid Message message) {
        MessageObject messageObject = findMessageById(id);

        if (StringUtils.hasText(message.getOrgId()))
            messageObject.setOrgId(message.getOrgId());
        else
            messageObject.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());

        if (StringUtils.hasText(message.getCreatedAccountId()))
            messageObject.setCreatedAccountId(message.getCreatedAccountId());
        else
            messageObject.setCreatedAccountId(tokenAuthenticationService.getAuthentication().getDetails().getId());
        messageObject.setId(id);
        messageObject.setTitle(message.getTitle());
        messageObject.setTypeCode(message.getTypeCode());
        messageObject.setStatusCode(LookupCodes.MessageStatus.CREATED);
        messageObject.setCreatedDateTime(DateTime.now());
        messageObject.setModifiedAccountId(null);
        messageObject.setModifiedDateTime(null);
        messageDomain.updateMessage(messageObject);
        messageDomain.saveMessageDetails(message.getDetails(), messageObject.getOrgId(), id);
        return new Message(messageObject);
    }


//
//    @RequestMapping(value = "", method = RequestMethod.PATCH)
//    @PreAuthorize("hasPermission(#message, 'message:update')")
//    public void updateMessages(@RequestBody Message message) {
//        if (message == null) throw new BadRequestException("Message不能为空！");
//        message.setModifiedAccountId(tokenAuthenticationService.getAuthentication().getDetails().getId()); //set last updated by
//        dataAPIClient.patch("message", message);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessages(@RequestHeader(Constants.HttpHeaderName.ACCESS_TOKEN) String token, @PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) throw new BadRequestException("Id不能小于0！");
        Message message = dataAPIClient.get("/message/{id}", Message.class, id);
        if (message == null) return; //no such message exist!

        TPermission tPermission = new TPermission();
        tPermission.setOrgId(message.getOrgId());
        tPermission.setResourceCode("message");
        tPermission.setActionCode("delete");
        if (!accountPermissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getDetails().getId(), tPermission)) {
            throw new ForbiddenException("没有权限删去此信息！");
        }
        message.setStatusCode(LookupCodes.MessageStatus.DELETED); //set status as deleted
        dataAPIClient.patch("message", message);
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
                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                        .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "找不到消息图片 imagekey = " + imagekey);
        }
    }

    private Message toMessage(MessageObject object) {
        if (object == null) {
            return null;
        }
        Message message = new Message();
        message.setId(object.getId());
        message.setOrgId(object.getOrgId());
        message.setTitle(object.getTitle());
        message.setStatusCode(object.getStatusCode());
        message.setTypeCode(object.getTypeCode());
        message.setCreatedAccountId(object.getCreatedAccountId());
        message.setCreatedDateTime(object.getCreatedDateTime());
        message.setModifiedAccountId(object.getModifiedAccountId());
        message.setModifiedDateTime(object.getModifiedDateTime());
        return message;
    }

    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }

    private MessageObject findMessageById(String id) {
        MessageObject messageObject = messageDomain.getMessageById(id);
        if (messageObject == null) {
            throw new NotFoundException("message not found");
        }
        return messageObject;
    }

    @RequestMapping(value = "{id}/coverimage", method = RequestMethod.PUT)
    public String putMessageCoverImage(@PathVariable(value = "id") String id,
                                     @RequestBody @Valid MessageImageRequest messageImageRequest) {
        MessageObject messageObject = findMessageById(id);
        String orgId = messageObject.getOrgId();
        String coverPath = messageDomain.saveMessageCoverImage(messageImageRequest, orgId, id);
        LOGGER.info("message cover image saved [orgId: {}, messageId:{}]", orgId, id);
        return coverPath;
    }

    @RequestMapping(value = "{id}/bodyimage", method = RequestMethod.PUT)
    public String putMessageBodyImage(@PathVariable(value = "id") String id,
                                      @RequestBody @Valid MessageImageRequest messageImageRequest) {
        MessageObject messageObject = findMessageById(id);
        String orgId = messageObject.getOrgId();
        String imagePath = messageDomain.saveMessageBodyImage(messageImageRequest, orgId, id);
        LOGGER.info("message body image saved [orgId: {}, messageId:{},path:{}]", orgId, id, imagePath);
        return imagePath;
    }


}