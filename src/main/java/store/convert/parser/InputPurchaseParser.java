package store.convert.parser;

import java.util.Arrays;
import java.util.List;
import store.domain.order.Order;
import store.support.convert.Parser;

public class InputPurchaseParser implements Parser<List<Order>> {

    private static final String ITEM_DELIMITER = ",";
    private static final String PART_DELIMITER = "-";
    private static final String BRACKET_PATTERN = "[\\[\\]]";
    private static final int EXPECTED_PARTS = 2;

    @Override
    public List<Order> parse(String text) {
        return Arrays.stream(Parser.split(text, ITEM_DELIMITER))
                .map(this::parseLine)
                .toList();
    }

    private Order parseLine(String line) {
        String[] items = line.replaceAll(BRACKET_PATTERN, "").split(PART_DELIMITER);
        Parser.validateLength(items, EXPECTED_PARTS);
        Parser.validateNotEmpty(items[0]);
        return new Order(
                items[0],
                Parser.toInt(items[1])
        );
    }
}
