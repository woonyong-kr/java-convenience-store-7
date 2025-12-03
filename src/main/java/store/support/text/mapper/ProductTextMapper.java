package store.support.text.mapper;

import java.text.DecimalFormat;
import store.domain.product.Product;

public class ProductTextMapper implements TextMapper<Product, String> {
    private static final String PRICE_UNIT = "원";
    private static final String QUANTITY_UNIT = "개";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final String SPACE = " ";
    private static final String PREFIX = "- ";
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###");

    @Override
    public String map(Product product) {
        String name = product.getName();
        String price = formatPrice(product.getPrice());
        String quantity = formatQuantity(product.getQuantity());
        String promotion = formatPromotion(product.getPromotion());

        return PREFIX + name + price + quantity + promotion;
    }

    private String formatPrice(int price) {
        return SPACE + PRICE_FORMAT.format(price) + PRICE_UNIT;
    }

    private String formatQuantity(int quantity) {
        if (quantity == 0) {
            return SPACE + OUT_OF_STOCK;
        }
        return SPACE + quantity + QUANTITY_UNIT;
    }

    private String formatPromotion(String promotion) {
        if (promotion == null) {
            return "";
        }
        return SPACE + promotion;
    }
}