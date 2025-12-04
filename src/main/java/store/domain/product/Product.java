package store.domain.product;

public class Product {
    private final String name;
    private final int price;
    private final Stock stock;
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
        this.stock = new Stock(normalStock, promotionStock);
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getNormalStock() {
        return stock.getNormalQuantity();
    }

    public int getPromotionStock() {
        return stock.getPromotionQuantity();
    }

    public int getTotalStock() {
        return stock.getTotalQuantity();
    }

    public String getPromotion() {
        return promotion;
    }

    public boolean hasPromotion() {
        return promotion != null && !promotion.isBlank();
    }

    public void addNormalStock(int amount) {
        stock.addNormal(amount);
    }

    public void addPromotionStock(int amount) {
        stock.addPromotion(amount);
    }

    public void sell(int quantity) {
        stock.reduce(quantity);
    }
}
