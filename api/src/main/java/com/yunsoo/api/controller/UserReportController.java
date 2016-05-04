package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.UserReportDomain;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.User;
import com.yunsoo.api.dto.UserReport;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.data.object.UserReportObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 9/29/2015.
 */
@RestController
@RequestMapping(value = "userReport")
public class UserReportController {

    @Autowired
    private UserReportDomain domain;

    @Autowired
    private ProductBaseDomain productBaseDomain;


    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public UserReport getById(@PathVariable(value = "id") String id) {
        UserReport report = new UserReport(domain.getReportById(id));

        return report;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserReport> getByFilter(@RequestParam(value = "product_base_id", required = false) String productBaseId, @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable, HttpServletResponse response) {
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        List<String> productBaseIds = new ArrayList<>();
        if (productBaseId == null) {
            productBaseIds = productBaseDomain.getProductBaseByOrgId(orgId, null).getContent().stream().map(item -> item.getId()).collect(Collectors.toList());
        } else
            productBaseIds.add(productBaseId);

        Page<UserReportObject> objectPage = domain.getUserReportsByProductBaseId(productBaseIds, pageable);

        if (pageable != null)
            response.setHeader("Content-Range", objectPage.toContentRange());

        List<UserReport> reportList = new ArrayList<>();
        objectPage.getContent().forEach(object -> {
            UserReport report = new UserReport(object);
            ProductBaseObject pbo = productBaseDomain.getProductBaseById(object.getProductBaseId());
            if (pbo != null) {
                report.setProductBase(new ProductBase(pbo));
            }
            UserObject uo = domain.getUserById(object.getUserId());
            if (uo != null) {
                report.setUser(new User(uo));
            }
            report.setImageNames(domain.getReportImageNames(object.getUserId(), object.getId()));
            reportList.add(report);
        });

        return reportList;
    }

    @RequestMapping(value = "{report_id}/image/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getReportImage(
            @PathVariable(value = "report_id") String reportId,
            @PathVariable(value = "image_name") String imageName) {
        UserReportObject object = domain.getReportById(reportId);

        ResourceInputStream resourceInputStream = domain.getReportImage(object.getUserId(), reportId, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("image not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));
    }


}
