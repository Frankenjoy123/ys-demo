package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.MessageDomain;
import com.yunsoo.api.rabbit.dto.basic.Message;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zhe on 2015/3/9.
 * Description: Only Authorized user can consume it.
 */

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageDomain messageDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Message getById(@PathVariable(value = "id") String id) throws NotFoundException {
        if (id == null) throw new BadRequestException("Id can not be null！");
        MessageObject messageObject = messageDomain.getById(id);
        if (messageObject == null) {
            throw new NotFoundException("message not found for id = " + id);
        }
        Message message = new Message(messageObject);
        message.setDetails(messageDomain.getMessageDetails(messageObject.getOrgId(), id));
        return message;
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