package com.yunsoo.common.web.util;

import com.yunsoo.common.util.DateTimeUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * @param pageable page, size, sort
     * @return this
     */
    public QueryStringBuilder append(Pageable pageable) {
        if (pageable != null) {
            this.append("page", pageable.getPageNumber());
            this.append("size", pageable.getPageSize());
            //sort
            Sort sort = pageable.getSort();
            if (sort != null) {
                Sort.Direction direction = null;
                List<String> properties = new ArrayList<>();
                for (Sort.Order order : sort) {
                    String prop = order.getProperty();
                    if (StringUtils.hasText(prop)) {
                        if (direction != order.getDirection()) {
                            if (properties.size() > 0) {
                                if (direction != null) {
                                    properties.add(direction.name().toLowerCase());
                                }
                                this.append("sort", properties);
                                properties = new ArrayList<>();
                            }
                            direction = order.getDirection();
                        }
                        properties.add(prop);
                    }
                }
                //check if there are properties left behind
                if (properties.size() > 0) {
                    if (direction != null) {
                        properties.add(direction.name().toLowerCase());
                    }
                    this.append("sort", properties);
                }
            }

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

    @Override
    public String toString() {
        return build();
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
        } else if (value instanceof Collection) {
            return StringUtils.collectionToCommaDelimitedString((Collection<?>) value);
        } else if (value instanceof Object[]) {
            return StringUtils.arrayToCommaDelimitedString((Object[]) value);
        }
        return value.toString();
    }


    public enum Prefix {

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
