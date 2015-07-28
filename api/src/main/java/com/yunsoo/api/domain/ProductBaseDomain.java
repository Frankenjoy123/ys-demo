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
        return dataAPIClient.get("productbaseversions?product_base_Id={product_base_Id}", new ParameterizedTypeReference<List<ProductBaseVersionsObject>>() {
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

    public String createProductBase(ProductBaseObject productBaseObject) {
        return dataAPIClient.post("productbase", productBaseObject, String.class);
    }

    public ProductBaseVersionsObject createProductBaseVersions(ProductBaseVersionsObject productBaseVersionsObject) {
        return dataAPIClient.post("productbaseversions/{product_base_id}", productBaseVersionsObject, ProductBaseVersionsObject.class, productBaseVersionsObject.getProductBaseId());
    }

    public void patchUpdate(ProductBaseVersionsObject productBaseVersionsObject) {
        dataAPIClient.patch("productbaseversions/{product_base_id}/{version}", productBaseVersionsObject, productBaseVersionsObject.getProductBaseId(), productBaseVersionsObject.getVersion());
    }

    public void deleteProductBase(String id) {
        dataAPIClient.delete("productbase/{id}", id);
    }

    public void deleteProductBaseVersions(String productBaseId, Integer version) {
        dataAPIClient.delete("productbaseversions/{product_base_id}/{version}", productBaseId, version);

    }

    public void createProductBaseFile(ProductBase productBase, String productBaseId, String orgId, Integer version) throws JsonProcessingException {

        FileObject fileObject = new FileObject();
        byte[] buf = mapper.writeValueAsBytes(productBase.getProductBaseDetails());
        fileObject.setData(buf);
        fileObject.setContentType("application/octet-stream");
        fileObject.setS3Path("organization/" + orgId + "/product_base/" + productBaseId + "/" + version + "/details.json");

        dataAPIClient.post("file/", fileObject, Long.class);

    }

    public void createProductBaseImage(ProductBaseImage productBaseImage, String orgId, Integer version) {
        try {
            String productBaseId = productBaseImage.getProductBaseId();
            InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(productBaseImage.getImageContent()));
            ImageProcessor imageProcessor = new ImageProcessor().read(inputStream);

            ProductBaseImage.Image imageRect = productBaseImage.getImageRect();
            ProductBaseImage.Image imageSquare = productBaseImage.getImageSquare();

            ByteArrayOutputStream outStream_rect_1 = new ByteArrayOutputStream();
            ByteArrayOutputStream outStream_rect_2 = new ByteArrayOutputStream();

            ImageProcessor imageProcessorRect = imageProcessor.crop(imageRect.getInitX(), imageRect.getInitY(), imageRect.getWidth(), imageRect.getHeight());
            imageProcessorRect.resize(800, 400).write(outStream_rect_1, "png");
            imageProcessorRect.resize(400, 200).write(outStream_rect_2, "png");

            FileObject fileObject_rect_1 = new FileObject();
            FileObject fileObject_rect_2 = new FileObject();

            fileObject_rect_1.setData(outStream_rect_1.toByteArray());
            fileObject_rect_1.setContentType("image/png");
            fileObject_rect_1.setS3Path("organization/" + orgId + "/product_base/" + productBaseId + "/" + version + "/image-800x400.png");
            dataAPIClient.post("file/", fileObject_rect_1, Long.class);

            fileObject_rect_2.setData(outStream_rect_2.toByteArray());
            fileObject_rect_2.setContentType("image/png");
            fileObject_rect_2.setS3Path("organization/" + orgId + "/product_base/" + productBaseId + "/" + version + "/image-400x200.png");
            dataAPIClient.post("file/", fileObject_rect_2, Long.class);
            outStream_rect_1.close();
            outStream_rect_2.close();
            ByteArrayOutputStream outStream_square = new ByteArrayOutputStream();
            ImageProcessor imageProcessorSquare = imageProcessor.crop(imageSquare.getInitX(), imageSquare.getInitY(), imageSquare.getWidth(), imageSquare.getHeight());
            imageProcessorSquare.resize(400, 400).write(outStream_square, "png");

            FileObject fileObject_square = new FileObject();
            fileObject_square.setData(outStream_square.toByteArray());
            fileObject_square.setContentType("application/octet-stream");
            fileObject_square.setS3Path("organization/" + orgId + "/product_base/" + productBaseId + "/" + version + "/image-400x400.png");
            dataAPIClient.post("file/", fileObject_square, Long.class);
            outStream_square.close();

            inputStream.close();
        } catch (IOException ex) {
            throw new InternalServerErrorException("图片上传出错！");
        }
    }

}
