package store.convert.mapper;

import store.domain.product.Product;
import store.support.convert.Mapper;

public class ProductTextMapper implements Mapper<Product> {

    private static final String PRICE_UNIT = "원";
    private static final String STOCK_UNIT = "개";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final String SPACE = " ";
    private static final String PREFIX = "- ";

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
            result.append(Mapper.LINE_DELIMITER);
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
        return SPACE + Mapper.number(price) + PRICE_UNIT;
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
