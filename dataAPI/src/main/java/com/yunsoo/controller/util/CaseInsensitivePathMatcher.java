package com.yunsoo.controller.util;

/**
 * Created by Zhe on 2015/1/28.
 *
 * Help make restful API case insensitive.
 */

import java.util.Map;

import org.springframework.util.AntPathMatcher;

public class CaseInsensitivePathMatcher extends AntPathMatcher {
    @Override
    protected boolean doMatch(String pattern, String path, boolean fullMatch, Map<String, String> uriTemplateVariables) {
        return super.doMatch(pattern.toLowerCase(), path.toLowerCase(), fullMatch, uriTemplateVariables);
    }
}
