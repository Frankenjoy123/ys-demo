package com.yunsoo.common.web.util;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/8
 * Descriptions:
 */
public class PageableUtilsTest {

    @Test
    public void test_formatPages() {
        System.out.println(PageableUtils.formatPages(null, null));
        System.out.println(PageableUtils.formatPages(0, 0));
        System.out.println(PageableUtils.formatPages(0, null));
        System.out.println(PageableUtils.formatPages(null, 1));
        System.out.println(PageableUtils.formatPages(1, null));
        System.out.println(PageableUtils.formatPages(1, 2));

    }

    @Test
    public void test_parsePages() {
        Integer[] result;
        result = PageableUtils.parsePages(PageableUtils.formatPages(null, null));
        System.out.println(Arrays.asList(result));
        result = PageableUtils.parsePages(PageableUtils.formatPages(0, 0));
        System.out.println(Arrays.asList(result));
        result = PageableUtils.parsePages(PageableUtils.formatPages(1, 2));
        System.out.println(Arrays.asList(result));
    }
}
