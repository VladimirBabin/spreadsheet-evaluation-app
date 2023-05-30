package com.vladimirbabin.wixgrow.spreadsheetevaluator.dto;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.function.Function;

public enum Type {
    NUMERIC(value -> {
        if (value instanceof Number) {
            return true;
        }
        if (!(value instanceof String)) {
            return false;
        }
        String stringObj = (String) value;
        return NumberUtils.isParsable(stringObj);
    }),
    BOOLEAN(Type::isBoolean),
    FORMULA(value -> {
        if (!(value instanceof String)) {
            return false;
        }
        String stringObj = (String) value;
        return stringObj.startsWith("=") || stringObj.contains("(");
    }),
    NOTATION(Type::isNotation),
    ERROR(value -> value.toString().startsWith("#")),
    STRING(value -> value instanceof String);

    private final Function<Object, Boolean> deserializer;

    Type(Function<Object, Boolean> deserializer) {
        this.deserializer = deserializer;
    }

    public boolean deserialize(Object value) {
        return deserializer.apply(value);
    }

    public static boolean isBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return true;
        } else if (!(obj instanceof String)) {
            return false;
        }
        String stringObj = (String) obj;
        return stringObj.equalsIgnoreCase("true") || stringObj.equalsIgnoreCase("false");
    }

    public static boolean isNotation(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        String parameter = (String) obj;
        return parameter.matches("[A-Z][0-9]+");
    }
}
