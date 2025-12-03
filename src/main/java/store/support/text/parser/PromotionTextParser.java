package store.support.text.parser;

import store.domain.product.Promotion;

public class PromotionTextParser extends TextParser<Promotion> {
    private static final String DELIMITER = ",";
    private static final int EXPECTED_PARTS = 5;

    @Override
    public Promotion parse(String text) {
        String[] parts = text.split(DELIMITER);
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
