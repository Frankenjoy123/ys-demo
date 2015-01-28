package com.yunsoo.controller;

import com.yunsoo.service.MessageService;
import com.yunsoo.service.contract.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/1/26.
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private final MessageService messageService;

    @Autowired
    MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/pushTo/{userid}", method = RequestMethod.GET)
    public List<Message> getNewMessagesByUserId(@PathVariable(value = "userid") Integer id) {
        return messageService.getMessagesByFilter(1, 1, null, true);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Message getNewMessagesByMessageId(@PathVariable(value = "id") Integer id) {
        return messageService.get(id);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<Message> getMessagesByFilter(@RequestParam(value = "type", required = false) Integer type,
                                             @RequestParam(value = "status", required = false) Integer status,
                                             @RequestParam(value = "companyId", required = false) Integer companyId,
                                             @RequestParam(value = "ignoreExpireDate", required = false) boolean ignoreExpireDate) {
        List<Message> messageList = messageService.getMessagesByFilter(type, status, companyId, ignoreExpireDate);
        return messageList;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public long createMessages(Message message) {
        long id = messageService.save(message);
        return id;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.PUT)
    public int deleteMessages(@PathVariable(value = "id") Integer id) {
        boolean result = messageService.delete(id);
        if (result) {
            return 1;
        } else {
            return 0;
        }
    }
}
