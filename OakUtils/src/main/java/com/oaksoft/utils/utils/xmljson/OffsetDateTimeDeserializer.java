package com.oaksoft.utils.utils.xmljson;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        OffsetDateTime dateTime = null;
        switch (jp.getCurrentToken()) {
            case VALUE_STRING:
                if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
                    String str = jp.getText().trim();
                    if (str.length() != 0) {
                        dateTime = OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    }
                }
                break;
        }
        if (dateTime == null) {
            context.handleUnexpectedToken(OffsetDateTime.class, jp);
            return null; // shouldn't get here the previous call is supposed to throw an exception
        } else {
            return dateTime;
        }
    }

}
