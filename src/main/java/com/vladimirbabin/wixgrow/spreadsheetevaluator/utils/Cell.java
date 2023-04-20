package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

public class Cell {
    private Type type;
    private Object value;

    public Cell(Object value) {
        this.value = value;
    }

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
}
