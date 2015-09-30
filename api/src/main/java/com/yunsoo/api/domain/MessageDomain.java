package com.yunsoo.api.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.yunsoo.api.Constants;
import com.yunsoo.api.dto.Message;
import com.yunsoo.api.dto.MessageDetails;
import com.yunsoo.api.dto.MessageImageRequest;
import com.yunsoo.api.dto.MessageToApp;
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
import java.util.ArrayList;
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
    private static final String COVER_IMAGE_NAME = "image-cover";
    private static final IIGtPush iiGtPush = new IGtPush(Constants.PushBase.API, Constants.PushBase.APPKEY, Constants.PushBase.MASTERSECRET);


    private static ObjectMapper mapper = new ObjectMapper();


    @Autowired
    private RestClient dataAPIClient;

    public Message getById(Integer id) {
        return dataAPIClient.get("message/{id}", Message.class, id);
    }

    public Page<MessageObject> getMessageByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("message" + query, new ParameterizedTypeReference<List<MessageObject>>() {
        });
    }

    public MessageObject getMessageById(String id) {
        try {
            return dataAPIClient.get("message/{id}", MessageObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public MessageObject createMessage(MessageObject messageObject) {
        return dataAPIClient.post("message", messageObject, MessageObject.class);
    }

    public void updateMessage(MessageObject messageObject) {
        dataAPIClient.put("message/{id}", messageObject, messageObject.getId());
    }

    public void deleteMessage(String id) {
        dataAPIClient.delete("message/{id}", id);
    }

    public void saveMessageCoverImage(MessageImageRequest messageImageRequest, String orgId, String id) {
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
        //save message cover image
        dataAPIClient.put("file/s3?path=organization/{orgId}/message/{messageId}/{imageName}",
                new ResourceInputStream(new ByteArrayInputStream(imageDataBytes), imageDataBytes.length, contentType), orgId, id, COVER_IMAGE_NAME);
    }

    public String saveMessageBodyImage(MessageImageRequest messageImageRequest, String orgId, String id) {
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
        String imageName = getRandomString();
        //save message cover image
        dataAPIClient.put("file/s3?path=organization/{orgId}/message/{messageId}/{imageName}",
                new ResourceInputStream(new ByteArrayInputStream(imageDataBytes), imageDataBytes.length, contentType), orgId, id, imageName);
        return imageName;
    }

    public String saveMessageBodyText(String messageBodyData, String orgId, String id) {

        if (((messageBodyData == null) || ("".equals(messageBodyData)))) {
            throw new BadRequestException("upload html text failed");
        }
        byte[] messageBodyDataBytes = Base64.decodeBase64(messageBodyData);
        //save message body text
        dataAPIClient.put("file/s3?path=organization/{orgId}/message/{messageId}/{bodyFileName}",
                new ResourceInputStream(new ByteArrayInputStream(messageBodyDataBytes), messageBodyDataBytes.length, MediaType.TEXT_HTML_VALUE), orgId, id, BODY_FILE_NAME);
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
            ResourceInputStream resourceInputStream = new ResourceInputStream(new ByteArrayInputStream(bytes), bytes.length, MediaType.APPLICATION_JSON_VALUE);
            dataAPIClient.put("file/s3?path=organization/{orgId}/message/{messageId}/{fileName}",
                    resourceInputStream, orgId, id, DETAILS_FILE_NAME);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

    public ResourceInputStream getMessageImage(String orgId, String id, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/message/{id}/{imageName}", orgId, id, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public Long count(String orgId, List<String> typeCodeIn, List<String> statusCodeIn) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("type_code_in", typeCodeIn)
                .append("status_code_in", statusCodeIn)
                .build();
        return dataAPIClient.get("message/count/on" + query, Long.class);
    }

    public IPushResult pushMessageToApp(String orgId, String id, MessageToApp messageToApp) {
        // define messge template
        try {
            String content = mapper.writeValueAsString(messageToApp);
            TransmissionTemplate transmissionTemplate = new TransmissionTemplate();
            transmissionTemplate.setAppId(Constants.PushBase.APPID);
            transmissionTemplate.setAppkey(Constants.PushBase.APPKEY);
            transmissionTemplate.setTransmissionType(2);
            transmissionTemplate.setTransmissionContent(content);
            // define app and message tag
            List appIdList = new ArrayList();
            List tagList = new ArrayList<>();
            appIdList.add(Constants.PushBase.APPID);
            tagList.add("yunsu");
            tagList.add(orgId);

            // message content
            AppMessage appMessage = new AppMessage();
            appMessage.setData(transmissionTemplate);
            appMessage.setOffline(true);
            appMessage.setAppIdList(appIdList);
            appMessage.setTagList(tagList);

            //push message to users
            iiGtPush.connect();
            IPushResult iPushResult = iiGtPush.pushMessageToApp(appMessage);
//            iiGtPush.close();

            //push message to ios users
            TransmissionTemplate iostransmissionTemplate = new TransmissionTemplate();
            iostransmissionTemplate.setAppId(Constants.PushBase.APPID);
            iostransmissionTemplate.setAppkey(Constants.PushBase.APPKEY);
            iostransmissionTemplate.setTransmissionType(2);
            iostransmissionTemplate.setTransmissionContent(content);
            APNPayload payload = new APNPayload();
            payload.setBadge(1);
            payload.setContentAvailable(1);
            payload.setSound("default");
            payload.setCategory("default");
            APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
            alertMsg.setTitle(messageToApp.getTitle());
            //  alertMsg.setBody(messageToApp.getBody());
            alertMsg.setBody(messageToApp.getTitle());
            payload.setAlertMsg(alertMsg);
            //payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));
            iostransmissionTemplate.setAPNInfo(payload);
            AppMessage iosappMessage = new AppMessage();
            iosappMessage.setData(iostransmissionTemplate);
            iosappMessage.setOffline(true);
            iosappMessage.setAppIdList(appIdList);
            iosappMessage.setTagList(tagList);
//            iiGtPush.connect();
            IPushResult iosPushResult = iiGtPush.pushMessageToApp(iosappMessage);
            iiGtPush.close();

            return iPushResult;
        } catch (IOException ex) {
            return null;
        }
    }

    //generate random 10 bit ID
    public static String getRandomString() {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(base.length());
            buffer.append(base.charAt(number));
        }
        return buffer.toString();
    }
}
