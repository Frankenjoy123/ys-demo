package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.MessageDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.Message;
import com.yunsoo.api.dto.MessageBodyImage;
import com.yunsoo.api.dto.MessageImageRequest;
import com.yunsoo.api.dto.MessageToApp;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    private OrganizationDomain organizationDomain;


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
        return message;
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
    public Long getNewMessagesByMessageId(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "type_code_in", required = false) List<String> typeCodeIn,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {

        orgId = fixOrgId(orgId);
        return messageDomain.count(orgId, typeCodeIn, statusCodeIn);
    }

    //create message
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
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

        // push message
        OrganizationObject organizationObject = organizationDomain.getOrganizationById(messageObject.getOrgId());
        if (organizationObject == null) {
            throw new NotFoundException("organization not found");
        }
        MessageToApp messageToApp = new MessageToApp();
        String orgId = organizationObject.getId();
        String orgName = organizationObject.getName();
        messageToApp.setOrg(orgName);
        messageToApp.setTitle(message.getTitle());
        messageToApp.setBody(message.getDetails().getBody());
        messageDomain.pushMessageToApp(orgId, id, messageToApp);
        return new Message(messageObject);
    }


    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessages(@PathVariable(value = "id") String id) {
        messageDomain.deleteMessage(id);
    }


//    @RequestMapping(value = "/{id}/{imagekey}", method = RequestMethod.GET)
//    public ResponseEntity<?> getThumbnail(@PathVariable(value = "id") String id,
//                                          @PathVariable(value = "imagekey") String imagekey) {
//        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("imagekey不能为空！");
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
//    }

    @RequestMapping(value = "{id}/image/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageImage(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "image_name") String imageName) {
        MessageObject messageObject = findMessageById(id);
        String orgId = messageObject.getOrgId();

        ResourceInputStream resourceInputStream = messageDomain.getMessageImage(orgId, id, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("image not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));
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
    public void putMessageCoverImage(@PathVariable(value = "id") String id,
                                     @RequestBody @Valid MessageImageRequest messageImageRequest) {
        MessageObject messageObject = findMessageById(id);
        String orgId = messageObject.getOrgId();
        messageDomain.saveMessageCoverImage(messageImageRequest, orgId, id);
        LOGGER.info("message cover image saved [orgId: {}, messageId:{}]", orgId, id);
    }

    @RequestMapping(value = "{id}/bodyimage", method = RequestMethod.PUT)
    public MessageBodyImage putMessageBodyImage(@PathVariable(value = "id") String id,
                                      @RequestBody @Valid MessageImageRequest messageImageRequest) {
        MessageObject messageObject = findMessageById(id);
        MessageBodyImage messageBodyImage = new MessageBodyImage();
        String orgId = messageObject.getOrgId();
        String imageName = messageDomain.saveMessageBodyImage(messageImageRequest, orgId, id);
        messageBodyImage.setImageName(imageName);
        LOGGER.info("message body image saved [orgId: {}, messageId:{},name:{}]", orgId, id, imageName);
        return messageBodyImage;
    }


}