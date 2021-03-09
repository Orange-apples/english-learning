package com.cxylm.springboot.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GsonEnumConverter implements JsonSerializer<Enum> {
    @Override
    public JsonElement serialize(Enum anEnum, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(anEnum.ordinal());
    }
}
