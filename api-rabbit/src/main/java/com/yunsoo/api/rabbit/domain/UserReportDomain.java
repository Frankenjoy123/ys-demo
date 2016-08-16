package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.UserReportObject;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by:   yan
 * Created on:   9/28/2015
 * Descriptions:
 */
@Component
public class UserReportDomain {
    @Autowired
    private RestClient dataApiClient;

    public UserReportObject saveUserReport(UserReportObject object){
        return dataApiClient.post("userReport", object, UserReportObject.class);
    }

    public Page<UserReportObject> getUserReportsByUserId(String userId, Pageable pageable){
        QueryStringBuilder builder = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("user_id", userId).append(pageable);

        Page<UserReportObject> objectPage = dataApiClient.getPaged("userReport" + builder.toString(), new ParameterizedTypeReference<List<UserReportObject>>() {
        });

        return objectPage;
    }

    public UserReportObject getReportById(String id){
        return dataApiClient.get("userReport/{id}", UserReportObject.class, id);
    }

    public void saveReportImage(String userId,String reportId, byte[] imageDataBytes) {
        String imageName = ObjectIdGenerator.getNew();
        try {
            ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));
            ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
            imageProcessor.write(imageOutputStream, "image/png");
            dataApiClient.put("file/s3?path=user/{userId}/report/{reportId}/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(imageOutputStream.toByteArray()), imageOutputStream.size(), "image/png"),
                    userId, reportId, imageName);
        } catch (IOException e) {
            throw new InternalServerErrorException("report Image upload failed [userId: " + userId + "], [reportId: " + reportId +"]");
        }
    }

}
