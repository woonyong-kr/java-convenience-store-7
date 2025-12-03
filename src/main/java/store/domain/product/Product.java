package store.domain.product;

public class Product {
    private static final String ERROR_EXCEED_STOCK = "[ERROR] 재고 수량 초과: ";

    private final String name;
    private final int price;
    private int normalStock;
    private int promotionStock;
    private final String promotion;

    public Product(
            String name,
            int price,
            int normalStock,
            int promotionStock,
            String promotion
    ) {
        this.name = name;
        this.price = price;
        this.normalStock = normalStock;
        this.promotionStock = promotionStock;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getNormalStock() {
        return normalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public int getTotalStock() {
        return normalStock + promotionStock;
    }

    public String getPromotion() {
        return promotion;
    }

    public boolean hasPromotion() {
        return promotion != null && !promotion.isBlank();
    }

    public void addNormalStock(int amount) {
        this.normalStock += amount;
    }

    public void addPromotionStock(int amount) {
        this.promotionStock += amount;
    }

    public void reducePromotionStock(int amount) {
        if (this.promotionStock < amount) {
            throw new IllegalArgumentException(ERROR_EXCEED_STOCK + amount);
        }
        this.promotionStock -= amount;
    }

    public void reduceNormalStock(int amount) {
        if (this.normalStock < amount) {
            throw new IllegalArgumentException(ERROR_EXCEED_STOCK + amount);
        }
        this.normalStock -= amount;
    }
}
