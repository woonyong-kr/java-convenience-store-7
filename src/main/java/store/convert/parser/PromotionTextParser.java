package store.convert.parser;

import java.util.Arrays;
import java.util.List;
import store.domain.product.Promotion;

public class PromotionTextParser extends TextParser<List<Promotion>> {
    private static final String DELIMITER = ",";
    private static final String LINE_BREAK = "\n";
    private static final int EXPECTED_PARTS = 5;

    @Override
    public List<Promotion> parse(String text) {
        return Arrays.stream(text.split(LINE_BREAK))
                .skip(1)
                .map(this::parseLine)
                .toList();
    }

    private Promotion parseLine(String line) {
        String[] parts = line.split(DELIMITER);
        validateCollectionLength(parts, EXPECTED_PARTS);
        validateNotEmpty(parts[0]);
        return new Promotion(
                parts[0],
                parseNumber(parts[1]),
                parseNumber(parts[2]),
                parseDate(parts[3]),
                parseDate(parts[4])
        );
    }
}
