package com.yunsoo.api.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductBaseDetails;
import com.yunsoo.api.dto.ProductBaseImage;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@Component
public class ProductBaseDomain {

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Autowired
    private ProductCategoryDomain productCategoryDomain;


    public ProductBaseObject getProductBaseById(String productBaseId) {
        try {
            return dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public ProductBaseDetails getProductBaseDetailByProductBaseIdAndVersion(String productBaseId, String orgId, Integer version) throws IOException {
        try {
            String path = "organization/" + orgId + "/product_base/" + productBaseId + "/" + version + "/details.json";
            FileObject fileObject = dataAPIClient.get("file?path={0}", FileObject.class, path);
            byte[] buf = fileObject.getData();
            return mapper.readValue(new ByteArrayInputStream(buf), ProductBaseDetails.class);
        } catch (NotFoundException ex) {
            throw new NotFoundException(40402, "Can't find prod detail");
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
        List<ProductBaseVersionsObject> productBaseVersionsObjects = dataAPIClient.get("productbaseversions?product_base_Id={product_base_Id}&version={version}", new ParameterizedTypeReference<List<ProductBaseVersionsObject>>() {
        }, productBaseId, version);
        if (productBaseVersionsObjects.size() == 0) {
            return null;
        }
        return productBaseVersionsObjects.get(0);
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

    public void deleteProductBase(String id) {
        dataAPIClient.delete("productbase/{id}", id);
    }

    public void deleteProductBaseVersions(String productBaseId, Integer version) {
        dataAPIClient.delete("productbaseversions/{product_base_id}/{version}", productBaseId, version);

    }

    public void createProductBaseFile(ProductBase productBase, String productBaseId, String orgId, Integer version) {
        try {
            FileObject fileObject = new FileObject();
            byte[] buf = mapper.writeValueAsBytes(productBase.getProductBaseDetails());
            fileObject.setData(buf);
            fileObject.setContentType("application/octet-stream");
            fileObject.setS3Path("organization/" + orgId + "/product_base/" + productBaseId + "/" + version + "/details.json");

            dataAPIClient.post("file/", fileObject, Long.class);
        } catch (JsonProcessingException ex) {
            throw new InternalServerErrorException("文件上传出错！");
        }
    }

    public void createProductBaseImage(ProductBaseImage productBaseImage, String orgId, Integer version) {
        try {
            String productBaseId = productBaseImage.getProductBaseId();
            String imageContent = productBaseImage.getImageContent();
            String imageBase64 = imageContent.substring(imageContent.indexOf(",") + 1);
            InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(imageBase64));
            ImageProcessor imageProcessor = new ImageProcessor().read(inputStream);

            ProductBaseImage.Image imageRect = productBaseImage.getImageRect();
            ProductBaseImage.Image imageSquare = productBaseImage.getImageSquare();

            ByteArrayOutputStream outStream_rect_1 = new ByteArrayOutputStream();
            ByteArrayOutputStream outStream_rect_2 = new ByteArrayOutputStream();
            ByteArrayOutputStream outStream_square = new ByteArrayOutputStream();


            ImageProcessor imageProcessorRect = imageProcessor.crop(imageRect.getInitX(), imageRect.getInitY(), imageRect.getWidth(), imageRect.getHeight());
            ImageProcessor imageProcessorSquare = imageProcessor.crop(imageSquare.getInitX(), imageSquare.getInitY(), imageSquare.getWidth(), imageSquare.getHeight());

            imageProcessorRect.resize(800, 400).write(outStream_rect_1, "png");
            imageProcessorRect.resize(400, 200).write(outStream_rect_2, "png");
            imageProcessorSquare.resize(400, 400).write(outStream_square, "png");

            InputStream inputStream_rect1 = new ByteArrayInputStream(outStream_rect_1.toByteArray());
            ResourceInputStream resourceInputStream_rect1 = new ResourceInputStream(inputStream_rect1, outStream_rect_1.toByteArray().length, "image/png");
            dataAPIClient.put("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/image-800x400.png", resourceInputStream_rect1, orgId, productBaseId, version);

            InputStream inputStream_rect2 = new ByteArrayInputStream(outStream_rect_1.toByteArray());
            ResourceInputStream resourceInputStream_rect2 = new ResourceInputStream(inputStream_rect2, outStream_rect_2.toByteArray().length, "image/png");
            dataAPIClient.put("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/image-400x200.png", resourceInputStream_rect2, orgId, productBaseId, version);

            InputStream inputStream_square = new ByteArrayInputStream(outStream_square.toByteArray());
            ResourceInputStream resourceInputStream_square = new ResourceInputStream(inputStream_square, outStream_square.toByteArray().length, "image/png");
            dataAPIClient.put("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/image-400x400.png", resourceInputStream_square, orgId, productBaseId, version);

            outStream_rect_1.close();
            outStream_rect_2.close();
            outStream_square.close();
            inputStream.close();
        } catch (IOException ex) {
            throw new InternalServerErrorException("图片上传出错！");
        }
    }

    public ResourceInputStream getProductBaseImage(String productBaseId, String orgId, Integer version, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}", orgId, productBaseId, version, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

}
