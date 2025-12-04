package store.support.convert;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

@FunctionalInterface
public interface Parser<T> extends Function<String, T> {

    String LINE_DELIMITER = "\n";
    String ERROR_EMPTY = "[ERROR] 빈 값이 존재합니다.";
    String ERROR_NOT_NUMBER = "[ERROR] 숫자 형식이 아닙니다.";
    String ERROR_NOT_DATE = "[ERROR] 날짜 형식이 올바르지 않습니다.";
    String ERROR_INVALID_LENGTH = "[ERROR] 데이터 형식이 올바르지 않습니다.";

    T parse(String input);

    @Override
    default T apply(String input) {
        return parse(input);
    }

    static void validateNotEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(ERROR_EMPTY);
        }
    }

    static void validateLength(String[] parts, int expected) {
        if (parts.length != expected) {
            throw new IllegalArgumentException(ERROR_INVALID_LENGTH);
        }
    }

    static int toInt(String value) {
        validateNotEmpty(value);
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_NOT_NUMBER);
        }
    }

    static long toLong(String value) {
        validateNotEmpty(value);
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_NOT_NUMBER);
        }
    }

    static LocalDate toLocalDate(String value) {
        validateNotEmpty(value);
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ERROR_NOT_DATE);
        }
    }

    static String[] split(String value, String delimiter) {
        validateNotEmpty(value);
        return value.split(delimiter);
    }

    static String[] splitLines(String value) {
        return split(value, LINE_DELIMITER);
    }
}
