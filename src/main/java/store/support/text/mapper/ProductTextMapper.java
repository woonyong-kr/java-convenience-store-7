package store.support.text.mapper;

import java.text.DecimalFormat;
import store.domain.product.Product;

public class ProductTextMapper implements TextMapper<Product, String> {
    private static final String PRICE_UNIT = "원";
    private static final String STOCK_UNIT = "개";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final String SPACE = " ";
    private static final String PREFIX = "- ";
    private static final String LINE_BREAK = "\n";
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###");

    @Override
    public String map(Product product) {
        StringBuilder result = new StringBuilder();

        if (product.hasPromotion()) {
            result.append(formatLine(
                    product.getName(),
                    product.getPrice(),
                    product.getPromotionStock(),
                    product.getPromotion()
            ));
            result.append(LINE_BREAK);
        }

        result.append(formatLine(
                product.getName(),
                product.getPrice(),
                product.getNormalStock(),
                null
        ));

        return result.toString();
    }

    private String formatLine(String name, int price, int stock, String promotion) {
        return PREFIX + name + formatPrice(price) + formatStock(stock) + formatPromotion(promotion);
    }

    private String formatPrice(int price) {
        return SPACE + PRICE_FORMAT.format(price) + PRICE_UNIT;
    }

    private String formatStock(int stock) {
        if (stock == 0) {
            return SPACE + OUT_OF_STOCK;
        }
        return SPACE + stock + STOCK_UNIT;
    }

    private String formatPromotion(String promotion) {
        if (promotion == null) {
            return "";
        }
        return SPACE + promotion;
    }
}