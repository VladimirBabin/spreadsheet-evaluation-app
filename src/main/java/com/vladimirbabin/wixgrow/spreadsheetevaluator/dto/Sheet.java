package com.vladimirbabin.wixgrow.spreadsheetevaluator.dto;

import java.util.List;

public class Sheet<T> {
    private String id;
    private List<List<T>> data;

    @Override
    public String toString() {
        return "Sheet{" +
                "id='" + id + '\'' +
                ", data=" + data +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<T>> getData() {
        return data;
    }

    public void setData(List<List<T>> data) {
        this.data = data;
    }

    public Sheet() {
    }

    public T getElementByNotation(String notation) {

        char[] notationArray = notation.toCharArray();
        try {
            T toReturn = data.get(notationArray[1] - 49).get(notationArray[0] - 65);
            if (toReturn instanceof String && notation.equals(toReturn)) {
                throw new IllegalArgumentException("Circular reference");
            }
            return toReturn;
        } catch (IndexOutOfBoundsException exception) {
            exception.printStackTrace();
            throw new RuntimeException("There is no such notation in the sheet");
        }
    }
}
