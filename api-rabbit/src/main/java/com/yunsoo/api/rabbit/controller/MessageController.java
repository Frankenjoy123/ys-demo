package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.MessageDomain;
import com.yunsoo.api.rabbit.dto.Message;
import com.yunsoo.api.rabbit.file.service.ImageService;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/3/9.
 * Description: Only Authorized user can consume it.
 */

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageDomain messageDomain;

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Message getMessageById(@PathVariable(value = "id") String id) throws NotFoundException {
        if (id == null) throw new BadRequestException("Id can not be null！");
        MessageObject messageObject = messageDomain.getById(id);
        if (messageObject == null) {
            throw new NotFoundException("message not found for id = " + id);
        }
        Message message = new Message(messageObject);
        message.setDetails(messageDomain.getMessageDetails(messageObject.getOrgId(), id));
        return message;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Message> getMessagesByFilter(@RequestParam(value = "org_id", required = true) String orgId) {
        List<MessageObject> messageObjects = messageDomain.getMessageByOrgId(orgId);
        List<Message> messages = messageObjects.stream().map(Message::new).collect(Collectors.toList());
        return messages;
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
        String path = String.format("organization/%s/message/%s/%s",messageObject.getOrgId(), id, imageName );
        return imageService.getImage(path);
    }
}