package com.yunsoo.data.api.util;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by:   Lijian
 * Created on:   2016-03-11
 * Descriptions:
 */
public class ResponseEntityUtils {

    public static ResponseEntity<?> convert(S3Object s3Object) throws IOException {
        if (s3Object == null) {
            return null;
        }
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

        InputStreamReader isr = new InputStreamReader(s3ObjectInputStream);
        /*
        String charset = isr.getEncoding();
        if(charset.toLowerCase().equals("cp1252"));
            charset = "GBK";
*/
        ObjectMetadata metadata = s3Object.getObjectMetadata();
     //   String contentType = metadata.getContentType() + "; charset=" + charset;
        String contentType = metadata.getContentType();
        long contentLength = metadata.getContentLength();

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        if (contentType != null) {
            bodyBuilder.contentType(MediaType.parseMediaType(contentType));

        }
        if (contentLength > 0) {
            bodyBuilder.contentLength(contentLength);
        }

        ByteArrayInputStream resultInputStream = new ByteArrayInputStream(StreamUtils.copyToByteArray(s3ObjectInputStream));
        s3ObjectInputStream.close();
     //   isr.close();
        return bodyBuilder.body(new InputStreamResource(resultInputStream));
    }

}
