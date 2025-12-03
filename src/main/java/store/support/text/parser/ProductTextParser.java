package store.support.text.parser;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.domain.product.Product;

public class ProductTextParser extends TextParser<List<Product>> {
    private static final String DELIMITER = ",";
    private static final String LINE_BREAK = "\n";
    private static final String NULL_TEXT = "null";
    private static final int EXPECTED_PARTS = 4;

    @Override
    public List<Product> parse(String text) {
        Map<String, ProductLine> lineMap = new LinkedHashMap<>();

        Arrays.stream(text.split(LINE_BREAK))
                .skip(1)
                .map(this::parseLineToDto)
                .forEach(dto -> mergeLine(lineMap, dto));

        return lineMap.values().stream()
                .map(ProductLine::toProduct)
                .toList();
    }

    private ProductLine parseLineToDto(String line) {
        String[] parts = line.split(DELIMITER);
        validateCollectionLength(parts, EXPECTED_PARTS);
        validateNotEmpty(parts[0]);
        return new ProductLine(
                parts[0],
                parseNumber(parts[1]),
                parseNumber(parts[2]),
                convertNullText(parts[3])
        );
    }

    private void mergeLine(Map<String, ProductLine> lineMap, ProductLine dto) {
        if (lineMap.containsKey(dto.name)) {
            lineMap.get(dto.name).addStock(dto);
            return;
        }
        lineMap.put(dto.name, dto);
    }

    private String convertNullText(String value) {
        validateNotEmpty(value);
        if (NULL_TEXT.equals(value)) {
            return null;
        }
        return value;
    }

    private static class ProductLine {
        private final String name;
        private final int price;
        private int normalStock;
        private int promotionStock;
        private String promotion;

        public ProductLine(String name, int price, int stock, String promotion) {
            this.name = name;
            this.price = price;
            this.normalStock = stock;
            this.promotionStock = 0;
            this.promotion = promotion;
            if (promotion != null) {
                this.normalStock = 0;
                this.promotionStock = stock;
            }
        }

        public void addStock(ProductLine other) {
            if (other.promotion != null) {
                this.promotionStock += other.promotionStock;
                this.promotion = other.promotion;
                return;
            }
            this.normalStock += other.normalStock;
        }

        public Product toProduct() {
            return new Product(name, price, normalStock, promotionStock, promotion);
        }
    }
}
