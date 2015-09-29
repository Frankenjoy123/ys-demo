package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.UserReportObject;
import com.yunsoo.common.util.IdGenerator;
import com.yunsoo.common.util.ImageProcessor;
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
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by yan on 9/28/2015.
 */
@Component
public class UserReportDomain {
    @Autowired
    private RestClient dataAPIClient;

    public UserReportObject saveUserReport(UserReportObject object){
        return dataAPIClient.post("userReport", object, UserReportObject.class);
    }

    public Page<UserReportObject> getUserReportsByUserId(String userId, Pageable pageable){
        QueryStringBuilder builder = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("user_id", userId);

        Page<UserReportObject> objectPage = dataAPIClient.getPaged("userReport" + builder.toString(), new ParameterizedTypeReference<List<UserReportObject>>() {
        });

        return objectPage;
    }



    public void saveReportImage(String userId,String reportId, byte[] imageDataBytes) {
        String imageName = IdGenerator.getNew();
        try {
            ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));
            ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
            imageProcessor.write(imageOutputStream, "png");
            dataAPIClient.put("file/s3?path=user/{userId}/report/{reportId}/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(imageOutputStream.toByteArray()), imageOutputStream.size(), "image/png"),
                    userId, reportId, imageName);
        } catch (IOException e) {
            throw new InternalServerErrorException("report Image upload failed [userId: " + userId + "], [reportId: " + reportId +"]");
        }
    }
}
