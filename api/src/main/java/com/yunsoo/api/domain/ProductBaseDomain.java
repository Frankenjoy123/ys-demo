package com.yunsoo.api.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.dto.ImageRequest;
import com.yunsoo.api.dto.ProductBaseDetails;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@Component
public class ProductBaseDomain {

    private static final String DETAILS_FILE_NAME = "details.json";
    private static final String IMAGE_NAME_800X400 = "image-800x400";
    private static final String IMAGE_NAME_400X200 = "image-400x200";
    private static final String IMAGE_NAME_400X400 = "image-400x400";

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestClient dataAPIClient;

    public ProductBaseObject getProductBaseById(String productBaseId) {
        try {
            return dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public ProductBaseDetails getProductBaseDetails(String orgId, String productBaseId, Integer version) {
        ResourceInputStream resourceInputStream;
        try {
            resourceInputStream = dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{fileName}",
                    orgId, productBaseId, version, DETAILS_FILE_NAME);
        } catch (NotFoundException ex) {
            return null;
        }
        try {
            return mapper.readValue(resourceInputStream, ProductBaseDetails.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void saveProductBaseDetails(ProductBaseDetails productBaseDetails, String orgId, String productBaseId, Integer version) {
        try {
            byte[] bytes = mapper.writeValueAsBytes(productBaseDetails);
            ResourceInputStream resourceInputStream = new ResourceInputStream(new ByteArrayInputStream(bytes), bytes.length, MediaType.APPLICATION_JSON_VALUE);
            dataAPIClient.put("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{fileName}",
                    resourceInputStream, orgId, productBaseId, version, DETAILS_FILE_NAME);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductBaseVersionsObject> getProductBaseVersionsByProductBaseId(String productBaseId) {
        return dataAPIClient.get("productbaseversions/{product_base_Id}", new ParameterizedTypeReference<List<ProductBaseVersionsObject>>() {
        }, productBaseId);
    }

    public Map<String, List<ProductBaseVersionsObject>> getProductBaseVersionsByProductBaseIds(List<String> productBaseIds) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("product_base_ids", productBaseIds)
                .build();
        return dataAPIClient.get("productbaseversions" + query, new ParameterizedTypeReference<Map<String, List<ProductBaseVersionsObject>>>() {
        });
    }

    public Page<ProductBaseObject> getProductBaseByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("productbase" + query, new ParameterizedTypeReference<List<ProductBaseObject>>() {
        });
    }

    public ProductBaseVersionsObject getProductBaseVersionsByProductBaseIdAndVersion(String productBaseId, Integer version) {
        try {
            return dataAPIClient.get("productbaseversions/{product_base_Id}/{version}", ProductBaseVersionsObject.class, productBaseId, version);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public ProductBaseObject createProductBase(ProductBaseObject productBaseObject) {
        return dataAPIClient.post("productbase", productBaseObject, ProductBaseObject.class);
    }

    public ProductBaseVersionsObject createProductBaseVersions(ProductBaseVersionsObject productBaseVersionsObject) {
        return dataAPIClient.post("productbaseversions/{product_base_id}", productBaseVersionsObject, ProductBaseVersionsObject.class, productBaseVersionsObject.getProductBaseId());
    }

    public void patchUpdate(ProductBaseVersionsObject productBaseVersionsObject) {
        dataAPIClient.put("productbaseversions/{product_base_id}/{version}", productBaseVersionsObject, productBaseVersionsObject.getProductBaseId(), productBaseVersionsObject.getVersion());
    }

    public void deleteProductBase(String productBaseId) {
        dataAPIClient.delete("productbase/{product_base_id}", productBaseId);
    }

    public void deleteProductBaseVersions(String productBaseId, Integer version) {
        dataAPIClient.delete("productbaseversions/{product_base_id}/{version}", productBaseId, version);
    }

    public ResourceInputStream getProductBaseImage(String orgId, String productBaseId, Integer version, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}", orgId, productBaseId, version, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public void saveProductBaseImage(ImageRequest imageRequest, String orgId, String productBaseId, Integer version) {
        try {
            String imageData = imageRequest.getData();
            //data:image/png;base64,
            int splitIndex = imageData.indexOf(",");
            String metaHeader = imageData.substring(0, splitIndex);
            String contentType = metaHeader.split(";")[0].split(":")[1];
            String imageDataBase64 = imageData.substring(splitIndex + 1);
            byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);
            ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));
            int rawWidth = imageProcessor.getWidth();
            int rawHeight = imageProcessor.getHeight();

            //save raw image
            dataAPIClient.put("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/image-raw",
                    new ResourceInputStream(new ByteArrayInputStream(imageDataBytes), imageDataBytes.length, contentType), orgId, productBaseId, version);

            //400x400
            if (imageRequest.getRange1x1() != null) {
                ImageRequest.Range range = imageRequest.getRange1x1();
                if (!checkRange(range, rawWidth, rawHeight)) {
                    throw new BadRequestException("crop range1x1 out of image range");
                }
                ImageProcessor imageProcessor1x1 = imageProcessor.crop(range.getX(), range.getY(), range.getWidth(), range.getHeight());
                //400x400
                ByteArrayOutputStream image400x400OutputStream = new ByteArrayOutputStream();
                imageProcessor1x1.resize(400, 400).write(image400x400OutputStream, "png");
                dataAPIClient.put("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}",
                        new ResourceInputStream(new ByteArrayInputStream(image400x400OutputStream.toByteArray()), image400x400OutputStream.size(), "image/png"),
                        orgId, productBaseId, version, IMAGE_NAME_400X400);
            }

            //800x400, 400x200
            if (imageRequest.getRange2x1() != null) {
                ImageRequest.Range range = imageRequest.getRange2x1();
                if (!checkRange(range, rawWidth, rawHeight)) {
                    throw new BadRequestException("crop range2x1 out of image range");
                }
                ImageProcessor imageProcessor2x1 = imageProcessor.crop(range.getX(), range.getY(), range.getWidth(), range.getHeight());
                //800x400
                ByteArrayOutputStream image800x400OutputStream = new ByteArrayOutputStream();
                imageProcessor2x1.resize(800, 400).write(image800x400OutputStream, "png");
                dataAPIClient.put("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}",
                        new ResourceInputStream(new ByteArrayInputStream(image800x400OutputStream.toByteArray()), image800x400OutputStream.size(), "image/png"),
                        orgId, productBaseId, version, IMAGE_NAME_800X400);
                //400x200
                ByteArrayOutputStream image400x200OutputStream = new ByteArrayOutputStream();
                imageProcessor2x1.resize(400, 200).write(image400x200OutputStream, "png");
                dataAPIClient.put("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}",
                        new ResourceInputStream(new ByteArrayInputStream(image400x200OutputStream.toByteArray()), image400x200OutputStream.size(), "image/png"),
                        orgId, productBaseId, version, IMAGE_NAME_400X200);
            }
        } catch (IOException ex) {
            throw new InternalServerErrorException("image upload failed");
        }
    }


    private boolean checkRange(ImageRequest.Range range, int rawWidth, int rawHeight) {
        return range.getX() >= 0
                && range.getY() >= 0
                && range.getWidth() > 0
                && range.getHeight() > 0
                && range.getX() + range.getWidth() <= rawWidth
                && range.getY() + range.getHeight() <= rawHeight;
    }

}
