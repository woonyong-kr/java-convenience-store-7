package store.support.text.parser;

import java.util.Arrays;
import java.util.List;
import store.domain.order.Order;

public class InputPurchaseParser extends TextParser<List<Order>>  {
    private static final String ITEM_DELIMITER = ",";
    private static final String PART_DELIMITER = "-";
    private static final String BRACKET_PATTERN = "[\\[\\]]";
    private static final int EXPECTED_PARTS = 2;

    @Override
    public List<Order> parse(String text) {
        return Arrays.stream(text.split(ITEM_DELIMITER))
                .map(this::parseLine)
                .toList();
    }

    private Order parseLine(String line) {
        String[] items = line.replaceAll(BRACKET_PATTERN, "").split(PART_DELIMITER);
        validateCollectionLength(items, EXPECTED_PARTS);
        validateNotEmpty(items[0]);
        return new Order(
                items[0],
                parseNumber(items[1])
        );
    }
}
