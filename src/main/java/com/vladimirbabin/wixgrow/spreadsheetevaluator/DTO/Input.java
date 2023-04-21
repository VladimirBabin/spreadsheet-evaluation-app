package com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO;

public class Input {
    private Type type;
    private Object value;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Input(Object value) {
        this.value = value;
    }
}
