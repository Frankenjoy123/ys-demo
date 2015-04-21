package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.dto.basic.Message;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;

/**
 * Created by Zhe on 2015/3/9.
 * <p>
 * ErrorCode
 * 40401    :   Message Not found!
 */

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private AccountDomain accountDomain;

    private final String AUTH_HEADER_NAME = "YS_WOLF_AUTH_TOKEN";
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'AccountInToken', 'message:read')")
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

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#message, 'message:create')")
    public long createMessages(@RequestBody Message message) {
        if (message == null) throw new BadRequestException("Message不能为空！");
        long id = dataAPIClient.post("message", message, Long.class);
        return id;
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#message, 'message:update')")
    public void updateMessages(@RequestBody Message message) {
        if (message == null) throw new BadRequestException("Message不能为空！");
        dataAPIClient.patch("message", message);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#message, 'message:delete')")
    public void deleteMessages(@RequestHeader(AUTH_HEADER_NAME) String token, @PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) throw new BadRequestException("Id不能小于0！");
        Message message = dataAPIClient.get("/user/collection/{id}", Message.class, id);
        if (!accountDomain.validateToken(token, message.getOrgId())) {
            throw new UnauthorizedException("不能删除其他机构的信息！");
        }
        dataAPIClient.delete("message/{id}", id);
    }

    @RequestMapping(value = "/image/{imagekey}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "imagekey") String imagekey) {
        if (imagekey == null || imagekey.isEmpty()) throw new BadRequestException("imagekey不能为空！");
        try {
            FileObject fileObject = dataAPIClient.get("message/image/{imagekey}", FileObject.class, imagekey);
            if (fileObject.getLength() > 0) {
                return ResponseEntity.ok()
                        .contentLength(fileObject.getLength())
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