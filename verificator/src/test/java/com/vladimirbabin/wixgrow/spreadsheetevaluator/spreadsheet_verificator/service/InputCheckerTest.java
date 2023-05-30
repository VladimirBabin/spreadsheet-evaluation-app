package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InputCheckerTest {

    private final InputChecker inputChecker = new InputChecker();

    private static Stream<Arguments> provideSheetIdAndFirstCellNumberValue() {
        return Stream.of(
                Arguments.of("sheet-1", 2),
                Arguments.of("sheet-2", 5),
                Arguments.of("sheet-3", 22),
                Arguments.of("sheet-4", 221212),
                Arguments.of("sheet-11", 10.75)
        );
    }

    private static Stream<Arguments> provideSheetIdAndFirstCellStringValue() {
        return Stream.of(
                Arguments.of("sheet-19", "Hello, World!"),
                Arguments.of("sheet-20", "Codes"),
                Arguments.of("sheet-21", "First"),
                Arguments.of("sheet-22", "First"),
                Arguments.of("sheet-23", "Last")
        );
    }

    private static Stream<Arguments> provideSheetIdAndFirstCellBooleanValue() {
        return Stream.of(
                Arguments.of("sheet-13", false),
                Arguments.of("sheet-14", true),
                Arguments.of("sheet-15", true),
                Arguments.of("sheet-16", true),
                Arguments.of("sheet-17", false)
        );
    }

    private static Stream<Arguments> provideSheetIdAndCellCoordinatesWithValidErrorValue() {
        return Stream.of(
                Arguments.of("sheet-15", 2, 2),
                Arguments.of("sheet-16", 2, 2),
                Arguments.of("sheet-31", 0, 0),
                Arguments.of("sheet-32", 0, 2),
                Arguments.of("sheet-33", 0, 1)
        );
    }

    private static Stream<Arguments> provideSheetIdInvalidTypeAndCellCoordinates() {
        return Stream.of(
                Arguments.of("sheet-1", Type.BOOLEAN, 0, 0),
                Arguments.of("sheet-10", Type.NUMERIC, 0, 2),
                Arguments.of("sheet-15", Type.BOOLEAN, 2, 2),
                Arguments.of("sheet-20", Type.BOOLEAN, 0, 0),
                Arguments.of("sheet-33", Type.STRING, 0, 1)
        );
    }

    private static Stream<Arguments> provideSheetIdValidTypeInvalidValueAndCellCoordinates() {
        return Stream.of(
                Arguments.of("sheet-1", Type.NUMERIC, 3, 0, 0),
                Arguments.of("sheet-10", Type.BOOLEAN, true, 0, 2),
                Arguments.of("sheet-19", Type.STRING, "Hello,World!", 0, 0),
                Arguments.of("sheet-20", Type.STRING, "Code", 0, 0),
                Arguments.of("sheet-23", Type.STRING, "First", 0, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSheetIdAndFirstCellNumberValue")
    public void checkMethodTestOnValidNumberCell(String sheetId, Number value) {
        Input input = new Input(value);
        input.setType(Type.NUMERIC);
        String res = inputChecker.check(sheetId, input, 0, 0);
        assertEquals("ok", res);
    }

    @ParameterizedTest
    @MethodSource("provideSheetIdAndFirstCellStringValue")
    public void checkMethodTestOnValidStringCell(String sheetId, String value) {
        Input input = new Input(value);
        input.setType(Type.STRING);
        String res = inputChecker.check(sheetId, input, 0, 0);
        assertEquals("ok", res);
    }

    @ParameterizedTest
    @MethodSource("provideSheetIdAndFirstCellBooleanValue")
    public void checkMethodTestOnValidBooleanCell(String sheetId, boolean value) {
        Input input = new Input(value);
        input.setType(Type.BOOLEAN);
        String res = inputChecker.check(sheetId, input, 0, 0);
        assertEquals("ok", res);
    }

    @ParameterizedTest
    @MethodSource("provideSheetIdAndCellCoordinatesWithValidErrorValue")
    public void checkMethodTestOnValidErrorCellWithCoordinates(String sheetId, int i, int j) {
        Input input = new Input("Error: some random error");
        input.setType(Type.ERROR);
        String res = inputChecker.check(sheetId, input, i, j);
        assertEquals("ok", res);
    }

    @ParameterizedTest
    @MethodSource("provideSheetIdInvalidTypeAndCellCoordinates")
    public void checkMethodTestOnInvalidTypeResultForNumberCell(String sheetId, Type type, int i, int j) {
        Input input = new Input("Error: some random error");
        input.setType(type);
        String res = inputChecker.check(sheetId, input, i, j);
        assertNotEquals("ok", res);
        assertTrue(res.startsWith("invalid result: invalid type in cell"));
    }

    @ParameterizedTest
    @MethodSource("provideSheetIdValidTypeInvalidValueAndCellCoordinates")
    public void checkMethodTestOnInvalidValueResultForNumberCell(String sheetId, Type type, Object value, int i, int j) {
        Input input = new Input(value);
        input.setType(type);
        String res = inputChecker.check(sheetId, input, i, j);
        assertNotEquals("ok", res);
        assertTrue(res.startsWith("invalid result: invalid value in cell"));
    }
}