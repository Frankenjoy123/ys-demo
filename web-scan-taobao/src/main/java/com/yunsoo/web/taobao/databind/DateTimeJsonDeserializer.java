package com.yunsoo.web.taobao.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.yunsoo.web.taobao.util.DateTimeUtils;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2015/3/13
 * Descriptions:
 */
public class DateTimeJsonDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String str = p.getText();
        return DateTimeUtils.parse(str);
    }

}
