package store.support.text;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import store.domain.product.Promotion;

public class PromotionTextParser implements TextParser<Promotion> {
    private static final String DELIMITER = ",";
    private static final int EXPECTED_PARTS = 5;
    private static final String ERROR_INVALID_FORMAT = "[ERROR] 프로모션 데이터 형식 오류: ";
    private static final String ERROR_EMPTY_VALUE = "[ERROR] 프로모션 데이터 빈 값 존재";
    private static final String ERROR_INVALID_NUMBER = "[ERROR] 프로모션 수량 숫자 아님: ";
    private static final String ERROR_INVALID_DATE = "[ERROR] 프로모션 날짜 형식 오류: ";

    @Override
    public Promotion parse(String text) {
        String[] parts = text.split(DELIMITER);
        validateCollectionLength(parts);
        validateNotEmpty(parts[0]);
        return new Promotion(
                parts[0],
                parseNumber(parts[1]),
                parseNumber(parts[2]),
                parseDate(parts[3]),
                parseDate(parts[4])
        );
    }

    private int parseNumber(String value) {
        validateNotEmpty(value);
        validateNumber(value);
        return Integer.parseInt(value);
    }

    private LocalDate parseDate(String value) {
        validateNotEmpty(value);
        validateDate(value);
        return LocalDate.parse(value);
    }

    private void validateCollectionLength(String[] parts) {
        if (parts.length != EXPECTED_PARTS) {
            throw new IllegalStateException(ERROR_INVALID_FORMAT + String.join(DELIMITER, parts));
        }
    }

    private void validateNotEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(ERROR_EMPTY_VALUE);
        }
    }

    private void validateNumber(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(ERROR_INVALID_NUMBER + value);
        }
    }

    private void validateDate(String value) {
        try {
            LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalStateException(ERROR_INVALID_DATE + value);
        }
    }
}
