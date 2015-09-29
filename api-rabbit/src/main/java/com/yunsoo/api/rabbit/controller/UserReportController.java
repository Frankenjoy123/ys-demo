package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserReportDomain;
import com.yunsoo.api.rabbit.dto.UserReport;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.UserReportObject;
import com.yunsoo.common.web.client.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 9/28/2015.
 */
@RestController
@RequestMapping("/userReport")
public class UserReportController {

    @Autowired
    private UserReportDomain domain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(method = RequestMethod.POST)
    public UserReport saveUserReport (@RequestBody UserReport report){
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        UserReportObject object = UserReport.toUserReportObject(report);
        if(userId != null )
            object.setUserId(userId);
        return new UserReport(domain.saveUserReport(object));

    }

    @RequestMapping(value = "{id}/image", method = RequestMethod.PUT)
    public void saveReportImage(@PathVariable(value = "id") String reportId,
                                 @RequestBody byte[] imageDataBytes) {
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        if(userId == null )
            return;
        if (imageDataBytes != null && imageDataBytes.length > 0) {

            domain.saveReportImage(userId,reportId, imageDataBytes);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserReport> getByFilter(Pageable pageable, HttpServletResponse response){
        String userId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        Page<UserReportObject> objectPage = domain.getUserReportsByUserId(userId, pageable);
        if(pageable != null)
            response.setHeader("Content-Range", objectPage.toContentRange());

        return objectPage.getContent().stream().map(UserReport::new).collect(Collectors.toList());
    }

}
