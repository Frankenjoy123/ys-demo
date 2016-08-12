package com.yunsoo.auth.api.util;

import com.yunsoo.common.web.Constants;
import com.yunsoo.common.web.client.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-12
 * Descriptions:
 */
public final class PageUtils {

    private PageUtils() {
    }

    public static <T> Page<T> convert(org.springframework.data.domain.Page<T> page) {
        return new Page<>(page.getContent(), page.getNumber(), page.getTotalPages(), (int) page.getTotalElements());
    }

    public static <T> List<T> response(HttpServletResponse response, Page<T> page) {
        response.setHeader("Content-Range", page.toContentRange());
        response.setHeader(Constants.HttpHeaderName.CONTENT_RANGE, page.toContentRange());
        return page.getContent();
    }

    public static <T> List<T> response(HttpServletResponse response, Page<T> page, boolean setHeader) {
        if (setHeader) {
            String contentRange = page.toContentRange();
            response.setHeader("Content-Range", contentRange);
            response.setHeader(Constants.HttpHeaderName.CONTENT_RANGE, contentRange);
        }
        return page.getContent();
    }

}
