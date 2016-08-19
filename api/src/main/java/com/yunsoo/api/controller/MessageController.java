package com.yunsoo.api.controller;

import com.yunsoo.api.domain.MessageDomain;
import com.yunsoo.api.dto.Message;
import com.yunsoo.api.dto.MessageImageRequest;
import com.yunsoo.api.file.dto.ImageResponse;
import com.yunsoo.api.file.service.ImageService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MessageDomain messageDomain;

    @Autowired
    private ImageService imageService;

    private final String COVER_IMAGE_NAME = "image-cover";

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
    @PreAuthorize("hasPermission(#orgId, 'org', 'message:read')")
    public List<Message> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                     Pageable pageable,
                                     HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);
        Page<MessageObject> messagePage = messageDomain.getMessageByOrgId(orgId, pageable);

        return PageUtils.response(response, messagePage.map(Message::new), pageable != null);
    }

    @RequestMapping(value = "/count/on", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'message:read')")
    public Long getNewMessagesByMessageId(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "type_code_in", required = false) List<String> typeCodeIn,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {

        orgId = AuthUtils.fixOrgId(orgId);
        return messageDomain.count(orgId, typeCodeIn, statusCodeIn);
    }

    //create message
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageObject createMessage() {
        MessageObject messageObject = new MessageObject();
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
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
    @PreAuthorize("hasPermission(#message.orgId, 'org', 'message:create')")
    public Message updateMessage(@PathVariable(value = "id") String id,
                                 @RequestBody @Valid Message message) {
        MessageObject messageObject = findMessageById(id);

        if (StringUtils.hasText(message.getOrgId()))
            messageObject.setOrgId(message.getOrgId());
        else
            messageObject.setOrgId(AuthUtils.getCurrentAccount().getOrgId());

        if (StringUtils.hasText(message.getCreatedAccountId()))
            messageObject.setCreatedAccountId(message.getCreatedAccountId());
        else
            messageObject.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
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
//        Organization org = authOrganizationService.getById(messageObject.getOrgId());
//        if (org == null) {
//            throw new NotFoundException("organization not found");
//        }
//        MessageToApp messageToApp = new MessageToApp();
//        String orgId = org.getId();
//        String orgName = org.getName();
//        messageToApp.setMessageId(id);
//        messageToApp.setOrgId(orgId);
//        messageToApp.setOrgName(orgName);
//        messageToApp.setTitle(message.getTitle());
//        messageToApp.setBody(message.getDetails().getBody());
        //messageDomain.pushMessageToApp(orgId, id, messageToApp);
        return new Message(messageObject);
    }


    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessages(@PathVariable(value = "id") String id) {
        messageDomain.deleteMessage(id);
    }


    @RequestMapping(value = "{id}/image/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageImage(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "image_name") String imageName) {
        MessageObject messageObject = findMessageById(id);
        String orgId = messageObject.getOrgId();
        String path = String.format("organization/{orgId}/message/{id}/{imageName}", orgId, id, imageName);
        return imageService.getImage(path);
    }

    @RequestMapping(value = "{id}/coverimage", method = RequestMethod.PUT)
    public void putMessageCoverImage(@PathVariable(value = "id") String id,
                                     @RequestBody @Valid MessageImageRequest messageImageRequest) throws IOException {
        MessageObject messageObject = findMessageById(id);
        String orgId = messageObject.getOrgId();
        String path = String.format("organization/%s/message/%s/%s", orgId, id, COVER_IMAGE_NAME);

        saveImage(messageImageRequest, path);
    }

    @RequestMapping(value = "{id}/bodyimage", method = RequestMethod.PUT)
    public ImageResponse putMessageBodyImage(@PathVariable(value = "id") String id,
                                                @RequestBody @Valid MessageImageRequest messageImageRequest) throws IOException {
        MessageObject messageObject = findMessageById(id);
        String orgId = messageObject.getOrgId();
        String path = String.format("organization/%s/message/%s/%s", orgId, id, RandomUtils.generateString(10));
        return saveImage(messageImageRequest, path);
    }

    private MessageObject findMessageById(String id) {
        MessageObject messageObject = messageDomain.getMessageById(id);
        if (messageObject == null) {
            throw new NotFoundException("message not found");
        }
        return messageObject;
    }

    private ImageResponse saveImage(MessageImageRequest messageImageRequest, String path) throws IOException {
        String imageData = messageImageRequest.getData();
        //data:image/png;base64,
        if (((imageData == null) || ("".equals(imageData)))) {
            throw new BadRequestException("upload cover image failed");
        }
        int splitIndex = imageData.indexOf(",");
        String metaHeader = imageData.substring(0, splitIndex);
        String contentType = metaHeader.split(";")[0].split(":")[1];
        String imageDataBase64 = imageData.substring(splitIndex + 1);
        byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);

        return imageService.saveImage(imageDataBytes, contentType, path);
    }

}