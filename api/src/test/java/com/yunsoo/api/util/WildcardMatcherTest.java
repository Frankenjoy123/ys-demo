package com.yunsoo.api.util;

import org.junit.Test;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/15
 * Descriptions:
 */
public class WildcardMatcherTest {

    @Test
    public void test_match() {
        assert !WildcardMatcher.match("product", null);
        assert !WildcardMatcher.match("product", "");
        assert !WildcardMatcher.match("product", "prod");
        assert WildcardMatcher.match("prod*", "prod");
        assert WildcardMatcher.match("prod*", "prod*");
        assert WildcardMatcher.match("prod*", "product");
        assert WildcardMatcher.match("*uct", "product");
        assert WildcardMatcher.match("*uct", "*uct");
        assert WildcardMatcher.match("pr*uct", "product");
        assert WildcardMatcher.match("pr*uct:read", "product:read");
        assert WildcardMatcher.match("pr*uct*", "product:read");
        assert WildcardMatcher.match("*", "product");
        assert WildcardMatcher.match("product*", "product[](){}\\+-?:*^$|");

    }
}
