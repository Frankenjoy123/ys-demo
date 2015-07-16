package com.yunsoo.common.web.util;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/8
 * Descriptions:
 */
public class PageableUtils {

    public static String formatPages(Integer page, Integer total) {
        return "pages "
                + (page == null || page < 0 ? "0" : page.toString())
                + "/"
                + (total == null || total < 1 ? "*" : total.toString());
    }

    public static Integer[] parsePages(String contentRange) {
        Integer page = 0, total = null;
        if (contentRange != null) {
            String[] pagesArray = contentRange.replace("pages ", "").split("/");
            try {
                page = Integer.parseInt(pagesArray[0]);
                total = "*".equals(pagesArray[1]) ? null : Integer.parseInt(pagesArray[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        return new Integer[]{page, total};
    }
}