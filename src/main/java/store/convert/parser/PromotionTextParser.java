package store.convert.parser;

import java.util.Arrays;
import java.util.List;
import store.domain.product.Promotion;
import store.support.convert.Parser;

public class PromotionTextParser implements Parser<List<Promotion>> {

    private static final String DELIMITER = ",";
    private static final int EXPECTED_PARTS = 5;

    @Override
    public List<Promotion> parse(String text) {
        return Arrays.stream(Parser.splitLines(text))
                .skip(1)
                .map(this::parseLine)
                .toList();
    }

    private Promotion parseLine(String line) {
        String[] parts = Parser.split(line, DELIMITER);
        Parser.validateLength(parts, EXPECTED_PARTS);
        Parser.validateNotEmpty(parts[0]);
        return new Promotion(
                parts[0],
                Parser.toInt(parts[1]),
                Parser.toInt(parts[2]),
                Parser.toLocalDate(parts[3]),
                Parser.toLocalDate(parts[4])
        );
    }
}
