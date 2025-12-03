package store.support.text.parser;

import java.util.Arrays;
import java.util.List;
import store.domain.product.Product;

public class ProductTextParser implements TextParser<List<Product>> {
    private static final String DELIMITER = ",";
    private static final String LINE_BREAK = "\n";
    private static final String NULL_TEXT = "null";
    private static final int EXPECTED_PARTS = 4;
    private static final String ERROR_INVALID_FORMAT = "[ERROR] 프로덕트 데이터 형식 오류: ";
    private static final String ERROR_EMPTY_VALUE = "[ERROR] 프로덕트 데이터 빈 값 존재";
    private static final String ERROR_INVALID_NUMBER = "[ERROR] 프로덕트 수량 숫자 아님: ";


    @Override
    public List<Product> parse(String text) {
        return Arrays.stream(text.split(LINE_BREAK))
                .skip(1)
                .map(this::parseLine)
                .toList();
    }

    private Product parseLine(String line) {
        String[] parts = line.split(DELIMITER);
        validateCollectionLength(parts);
        validateNotEmpty(parts[0]);
        return new Product(
                parts[0],
                parseNumber(parts[1]),
                parseNumber(parts[2]),
                convertNullText(parts[3])
        );
    }

    private String convertNullText(String value) {
        validateNotEmpty(value);
        if (NULL_TEXT.equals(value)) {
            return null;
        }
        return value;
    }

    private int parseNumber(String value) {
        validateNotEmpty(value);
        validateNumber(value);
        return Integer.parseInt(value);
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
}
