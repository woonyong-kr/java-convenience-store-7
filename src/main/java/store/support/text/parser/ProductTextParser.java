package store.support.text.parser;

import java.util.Arrays;
import java.util.List;
import store.domain.product.Product;

public class ProductTextParser extends TextParser<List<Product>> {
    private static final String DELIMITER = ",";
    private static final String LINE_BREAK = "\n";
    private static final String NULL_TEXT = "null";
    private static final int EXPECTED_PARTS = 4;

    @Override
    public List<Product> parse(String text) {
        return Arrays.stream(text.split(LINE_BREAK))
                .skip(1)
                .map(this::parseLine)
                .toList();
    }

    private Product parseLine(String line) {
        String[] parts = line.split(DELIMITER);
        validateCollectionLength(parts, EXPECTED_PARTS);
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
}
