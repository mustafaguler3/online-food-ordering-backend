package com.example.food_ordering.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy");
        try {
            return formatter.parse(jsonParser.getText());
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format. Expected MM/yy");
        }
    }
}
