package com.yunsoo.web.taobao.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yunsoo.web.taobao.util.DateTimeUtils;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2015/3/13
 * Descriptions:
 */
public class DateTimeJsonSerializer extends JsonSerializer<DateTime> {

    @Override
    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(DateTimeUtils.toUTCString(value));
    }

}
