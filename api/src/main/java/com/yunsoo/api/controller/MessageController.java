package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.MessageDomain;
import com.yunsoo.api.dto.Message;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Message> getMessages(@RequestParam(value = "org_id", required = false) String orgId,
                                     @PageableDefault(page = 0, size = 20)
                                     @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                     Pageable pageable,
                                     HttpServletResponse response) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }

        List<MessageObject> messageObjects = dataAPIClient.get("message?orgid={0}&pageIndex={1}&pageSize={2}", new ParameterizedTypeReference<List<MessageObject>>() {
        }, orgId, pageable.getPageNumber(), pageable.getPageSize());
        response.setHeader("Content-Range", "pages " + pageable.getPageNumber() + "/*");
        return messageObjects.stream().map(this::toMessage).collect(Collectors.toList());
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
        if (!accountPermissionDomain.hasPermission(tokenAuthenticationService.getAuthentication().getDetails().getId(), tPermission)) {
            throw new ForbiddenException("没有权限删去此信息！");
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
        message.setBody(object.getBody());
        message.setDigest(object.getDigest());
        message.setExpiredDateTime(object.getExpiredDateTime());
        message.setLink(object.getLink());
        message.setType(object.getType());
        message.setStatus(object.getStatus());
        message.setPostShowTime(object.getPostShowTime());
        message.setCreatedBy(object.getCreatedBy());
        message.setCreatedDateTime(object.getCreatedDateTime());
        message.setLastUpdatedBy(object.getLastUpdatedBy());
        message.setLastUpdatedDateTime(object.getLastUpdatedDateTime());
        return message;
    }

}