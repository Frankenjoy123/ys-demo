package com.yunsoo.api.controller;

import com.yunsoo.api.dto.basic.Message;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/3/9.
 */

@RestController
@RequestMapping("/message")
public class MessageController {

    private RestClient dataAPIClient;

    @Autowired
    MessageController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/pushTo/{userid}", method = RequestMethod.GET)
    public List<Message> getNewMessagesByUserId(@PathVariable(value = "userid") Long userid) {
        List<Message> messageList = dataAPIClient.get("message/pushto", List.class, userid);
        return messageList;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Message getById(@PathVariable(value = "id") int id) throws NotFoundException {
        Message message = dataAPIClient.get("message/{id}", Message.class, id);
        if (message == null) throw new NotFoundException("Message not found id=" + id);
        return message;
    }

    @RequestMapping(value = "/getUnread", method = RequestMethod.GET)
    public List<Message> getUnreadMessagesBy(@RequestParam(value = "userId", required = true) Long userId,
                                             @RequestParam(value = "companyId", required = true) Long companyId) {
        List<Message> messageList = dataAPIClient.get("getUnread", List.class, userId, companyId);
        return messageList;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public long createMessages(@RequestBody Message message) {
        long id = dataAPIClient.post("/create", message, Long.class);
        return id;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public void updateMessages(@RequestBody Message message) {
        dataAPIClient.patch("/update", message);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deleteMessages(@PathVariable(value = "id") Long id) {
        dataAPIClient.delete("/delete", id);
    }
}
