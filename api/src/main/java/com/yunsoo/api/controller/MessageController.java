package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.MessageDomain;
import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.dto.basic.Message;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private PermissionDomain permissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'message:read')")
//    @PostAuthorize("returnObject.orgId == authentication.getOrgId()")
    public Message getById(@PathVariable(value = "id") Integer id) {
        if (id <= 0) throw new BadRequestException("Id不能小于0！");
        Message message = messageDomain.getById(id);
        if (message == null) {
            throw new NotFoundException(40401, "Message not found for id = " + id);
        }
        return message;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'message:read')")
    public List<Message> getMessages(//@RequestHeader(AUTH_HEADER_NAME) String token,
                                     @RequestParam(value = "orgid", required = false) String orgId,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return dataAPIClient.get("message?orgid={0}&pageIndex={1}&pageSize={2}", new ParameterizedTypeReference<List<Message>>() {
        }, orgId, pageIndex, pageSize);
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#message.orgId, 'filterByOrg', 'message:create')")
    public long createMessages(@RequestBody Message message) {
        if (message == null) throw new BadRequestException("Message不能为空！");
        message.setStatus(LookupCodes.MessageStatus.CREATED); //set as created
        message.setCreatedBy(tokenAuthenticationService.getAuthentication().getDetails().getId()); //set created by
        long id = dataAPIClient.post("message", message, long.class);
        return id;
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#message, 'message:update')")
    public void updateMessages(@RequestBody Message message) {
        if (message == null) throw new BadRequestException("Message不能为空！");
        message.setLastUpdatedBy(tokenAuthenticationService.getAuthentication().getDetails().getId()); //set last updated by
        dataAPIClient.patch("message", message);
    }

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
        if (!permissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getDetails().getId(), tPermission)) {
            throw new UnauthorizedException("没有权限删去此信息！");
        }
        message.setStatus(LookupCodes.MessageStatus.DELETED); //set status as deleted
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