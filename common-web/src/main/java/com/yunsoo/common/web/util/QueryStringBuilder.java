package com.yunsoo.common.web.util;

import com.yunsoo.common.util.DateTimeUtils;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/7
 * Descriptions:
 */
public class QueryStringBuilder {

    private StringBuilder query = new StringBuilder();
    private boolean hasParameter;

    public QueryStringBuilder() {
    }

    public QueryStringBuilder(Prefix prefix) {
        query.append(prefix.value());
    }

    /**
     * it do nothing if value is null
     *
     * @param name  parameter name must not be null, empty or whitespace
     * @param value parameter value
     * @return this
     */
    public QueryStringBuilder append(String name, Object value) {
        Assert.hasText(name, "name must not be null, empty or whitespace");

        if (value != null) {
            checkSuffix();
            query.append(name).append('=').append(formatValue(value));
            hasParameter = true;
        }

        return this;
    }

    /**
     * append the parameter even the value is null
     * be careful use this method, the value will be empty string if it's null
     *
     * @param name  parameter name must not be null, empty or whitespace
     * @param value parameter value
     * @return this
     */
    public QueryStringBuilder appendEvenNull(String name, Object value) {
        Assert.hasText(name, "name must not be null, empty or whitespace");

        checkSuffix();
        query.append(name).append('=').append(formatValue(value));
        hasParameter = true;

        return this;
    }

    /**
     * build the query string with the parameters appended
     *
     * @return the query string
     */
    public String build() {
        return query.toString();
    }


    private void checkSuffix() {
        if (hasParameter) {
            query.append('&');
        }
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof DateTime) {
            return DateTimeUtils.toUTCString((DateTime) value);
        }
        return value.toString();
    }


    public static enum Prefix {

        QUESTION_MARK('?'),

        AND('&');

        private char value;

        Prefix(char value) {
            this.value = value;
        }

        public char value() {
            return value;
        }
    }
}
