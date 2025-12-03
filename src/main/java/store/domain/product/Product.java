package store.domain.product;

public class Product {
    private static final String ERROR_EXCEED_QUANTITY = "[ERROR] 재고 수량 초과: ";

    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    public Product(
            String name,
            int price,
            int quantity,
            String promotion
    ) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    public void reduceQuantity(int amount) {
        if (this.quantity < amount) {
            throw new IllegalArgumentException(ERROR_EXCEED_QUANTITY + amount);
        }
        this.quantity -= amount;
    }
}
