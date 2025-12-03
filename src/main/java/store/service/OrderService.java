package store.service;

import java.util.Collections;
import java.util.List;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.product.Promotion;

public class OrderService {
    private List<Order> currentOrder;

    public void registerOrder(List<Order> order) {
        this.currentOrder = order;
    }

    public List<Order> getCurrentOrder() {
        return Collections.unmodifiableList(currentOrder);
    }

    public void clearOrder() {
        this.currentOrder = null;
    }

    public int getNonPromotionQuantity(Order order, Product product, Promotion promotion) {
        if (promotion == null || !promotion.isActive()) {
            return 0;
        }

        int unit = promotion.getBuy() + promotion.getGet();
        int promotionStock = product.getPromotionStock();
        int maxPromotionApplicable = (promotionStock / unit) * unit;

        return Math.max(0, order.getQuantity() - maxPromotionApplicable);
    }

    public int getFreeProductQuantity(Order order, Product product, Promotion promotion) {
        if (promotion == null || !promotion.isActive()) {
            return 0;
        }

        int buy = promotion.getBuy();
        int get = promotion.getGet();
        int unit = buy + get;
        int quantity = order.getQuantity();
        int promotionStock = product.getPromotionStock();

        int remainder = quantity % unit;
        if (remainder == buy && promotionStock >= quantity + get) {
            return get;
        }
        return 0;
    }
}
