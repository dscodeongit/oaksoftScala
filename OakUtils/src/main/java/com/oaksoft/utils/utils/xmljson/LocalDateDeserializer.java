package com.oaksoft.utils.utils.xmljson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        LocalDate date = null;
        switch (jp.getCurrentToken()) {
            case VALUE_STRING:
                if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
                    String str = jp.getText().trim();
                    if (str.length() != 0) {
                        date = LocalDate.parse(str, DateTimeFormatter.ISO_DATE);
                    }
                }
                break;
        }
        if (date == null) {
            context.handleUnexpectedToken(LocalDate.class, jp);
            return null; // shouldn't get here the previous call is supposed to throw an exception
        } else {
            return date;
        }
    }
}
