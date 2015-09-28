package com.yunsoo.api.rabbit.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.rabbit.dto.MessageDetails;
import com.yunsoo.common.data.object.MessageObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2015/8/19
 * Descriptions:
 */

@Component
public class MessageDomain {

    private static final String DETAILS_FILE_NAME = "message-details.json";
    private static final String BODY_FILE_NAME = "message-body.html";
    private static final String COVER_IMAGE_NAME = "image-cover";

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestClient dataAPIClient;

    public MessageObject getById(String id) {
        try {
            return dataAPIClient.get("message/{id}", MessageObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public ResourceInputStream getMessageImage(String orgId, String id, String imageName) {

        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/message/{id}/{imageName}", orgId, id, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }


    public MessageDetails getMessageDetails(String orgId, String id) {
        ResourceInputStream resourceInputStream;
        try {
            resourceInputStream = dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/message/{messageId}/{fileName}",
                    orgId, id, DETAILS_FILE_NAME);
        } catch (NotFoundException ex) {
            return null;
        }
        try {
            return mapper.readValue(resourceInputStream, MessageDetails.class);
        } catch (IOException e) {
            return null;
        }
    }

    public List<MessageObject> getMessageByOrgId(String orgId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .build();

        return dataAPIClient.get("message" + query, new ParameterizedTypeReference<List<MessageObject>>() {
        });
    }
}
