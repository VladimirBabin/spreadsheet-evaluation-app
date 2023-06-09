package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto;

public class Input {
    private Type type;
    private Object value;

    public Input(Object value) {
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

    @Override
    public String toString() {
        return "Input{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
