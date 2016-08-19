package com.yunsoo.api.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.dto.Message;
import com.yunsoo.api.dto.MessageDetails;
import com.yunsoo.api.dto.MessageImageRequest;
import com.yunsoo.api.file.service.FileService;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/28
 * Descriptions:
 */
@Component
public class MessageDomain {

    private static final String DETAILS_FILE_NAME = "message-details.json";
    private static final String BODY_FILE_NAME = "message-body.html";

    private static ObjectMapper mapper = new ObjectMapper();


    @Autowired
    private RestClient dataApiClient;

    @Autowired
    private FileService fileService;

    public Message getById(Integer id) {
        return dataApiClient.get("message/{id}", Message.class, id);
    }

    public Page<MessageObject> getMessageByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataApiClient.getPaged("message" + query, new ParameterizedTypeReference<List<MessageObject>>() {
        });
    }

    public MessageObject getMessageById(String id) {
        try {
            return dataApiClient.get("message/{id}", MessageObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public MessageObject createMessage(MessageObject messageObject) {
        return dataApiClient.post("message", messageObject, MessageObject.class);
    }

    public void updateMessage(MessageObject messageObject) {
        dataApiClient.put("message/{id}", messageObject, messageObject.getId());
    }

    public void deleteMessage(String id) {
        dataApiClient.delete("message/{id}", id);
    }

    public String saveMessageBodyText(String messageBodyData, String orgId, String id) {

        if (((messageBodyData == null) || ("".equals(messageBodyData)))) {
            throw new BadRequestException("upload html text failed");
        }
        byte[] messageBodyDataBytes = Base64.decodeBase64(messageBodyData);
        //save message body text
        String filePath = String.format("organization/%s/message/%s/%s", orgId, id,BODY_FILE_NAME );
        fileService.putFile(filePath, new ResourceInputStream(new ByteArrayInputStream(messageBodyDataBytes), messageBodyDataBytes.length, MediaType.TEXT_HTML_VALUE));

        return "organization/" + orgId + "/message/" + id + "/" + BODY_FILE_NAME;
    }


    public void saveMessageDetails(MessageDetails messageDetails, String orgId, String id) {
        try {
            if (messageDetails.getBody() != null) {
                saveMessageBodyText(messageDetails.getBody(), orgId, id);
                //     String bodyPath = "organization/" + orgId + "/message/" + id + "/" + BODY_FILE_NAME;
                messageDetails.setBody(messageDetails.getBody());
            }
            byte[] bytes = mapper.writeValueAsBytes(messageDetails);
            String filePath = String.format("organization/%s/message/%s/%s", orgId, id,DETAILS_FILE_NAME );

            ResourceInputStream resourceInputStream = new ResourceInputStream(new ByteArrayInputStream(bytes), bytes.length, MediaType.APPLICATION_JSON_VALUE);
            fileService.putFile(filePath, resourceInputStream
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageDetails getMessageDetails(String orgId, String id) {
        ResourceInputStream resourceInputStream;
        try {
            String path = String.format("organization/%s/message/$s/%s", orgId, id, DETAILS_FILE_NAME);
            resourceInputStream = fileService.getFile(path);
        } catch (NotFoundException ex) {
            return null;
        }
        try {
            return mapper.readValue(resourceInputStream, MessageDetails.class);
        } catch (IOException e) {
            return null;
        }
    }


    public Long count(String orgId, List<String> typeCodeIn, List<String> statusCodeIn) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("type_code_in", typeCodeIn)
                .append("status_code_in", statusCodeIn)
                .build();
        return dataApiClient.get("message/count/on" + query, Long.class);
    }

}
