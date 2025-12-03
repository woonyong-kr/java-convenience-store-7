package store.support.text.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

public abstract class TextParser<T> implements Function<String, T> {
    private static final String ERROR_EMPTY_VALUE = "[ERROR] 데이터 빈 값 존재";
    private static final String ERROR_INVALID_NUMBER = "[ERROR] 수량 숫자 아님";
    private static final String ERROR_INVALID_FORMAT = "[ERROR] 데이터 형식 오류";
    private static final String ERROR_INVALID_DATE = "[ERROR] 날짜 형식 오류";


    public abstract T parse(String text);

    @Override
    public T apply(String text) {
        return parse(text);
    }

    protected int parseNumber(String value) {
        validateNotEmpty(value);
        validateNumber(value);
        return Integer.parseInt(value);
    }

    protected LocalDate parseDate(String value) {
        validateNotEmpty(value);
        validateDate(value);
        return LocalDate.parse(value);
    }

    protected void validateNotEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(ERROR_EMPTY_VALUE);
        }
    }

    protected void validateNumber(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(ERROR_INVALID_NUMBER);
        }
    }

    protected void validateCollectionLength(String[] parts, int length) {
        if (parts.length != length) {
            throw new IllegalStateException(ERROR_INVALID_FORMAT);
        }
    }

    protected void validateDate(String value) {
        try {
            LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalStateException(ERROR_INVALID_DATE);
        }
    }
}