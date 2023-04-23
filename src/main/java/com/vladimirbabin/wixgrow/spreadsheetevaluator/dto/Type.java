package com.vladimirbabin.wixgrow.spreadsheetevaluator.dto;

import java.util.function.Function;

public enum Type {

    NUMERIC(value -> value instanceof Number),
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
    STRING(value -> value instanceof String); //default

    private Function<Object, Boolean> deserializer;

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
        var stringObj = (String) obj;
        return stringObj.equalsIgnoreCase("true") || stringObj.equalsIgnoreCase("false");
    }

    public static boolean isNotation(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        var parameter = (String) obj;
        return parameter.matches("[A-Z][0-9]+");
    }
}
