package store.domain.order;

import store.domain.product.Product;
import store.domain.product.PromotionPolicy;

public class PurchaseResult {
    private final String productName;
    private final int quantity;
    private final int price;
    private final int freeQuantity;
    private final int nonPromotionQuantity;

    private PurchaseResult(String productName, int quantity, int price, int freeQuantity, int nonPromotionQuantity) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.freeQuantity = freeQuantity;
        this.nonPromotionQuantity = nonPromotionQuantity;
    }

    public static PurchaseResult of(Order order, Product product, PromotionPolicy policy) {
        int quantity = order.getQuantity();
        int price = product.getPrice();
        int freeQuantity = policy.getTotalFreeQuantity(quantity);
        int nonPromotionQuantity = policy.getNonPromotionQuantity(quantity);

        return new PurchaseResult(
                order.getName(),
                quantity,
                price,
                freeQuantity,
                nonPromotionQuantity
        );
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return price * quantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getPromotionDiscount() {
        return price * freeQuantity;
    }

    public int getNonPromotionPrice() {
        return price * nonPromotionQuantity;
    }
}
