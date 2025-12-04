package store.service;

import java.util.Collections;
import java.util.List;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.domain.product.PromotionPolicy;
import store.support.service.Service;

public class OrderService extends Service {
    private List<Order> currentOrder;
    private boolean useMembership;

    public OrderService() {
        clearOrder();
    }

    public boolean isUseMembership() {
        return useMembership;
    }

    public void applyMembership(boolean useMembership) {
        this.useMembership = useMembership;
    }

    public void registerOrder(List<Order> order) {
        this.currentOrder = order;
    }

    public List<Order> getCurrentOrder() {
        return Collections.unmodifiableList(currentOrder);
    }

    public void clearOrder() {
        this.currentOrder = null;
        this.useMembership = false;
    }

    public int getNonPromotionQuantity(Order order, Product product, Promotion promotion) {
        PromotionPolicy policy = product.createPromotionPolicy(promotion);
        return policy.getNonPromotionQuantity(order.getQuantity());
    }

    public int getFreeProductQuantity(Order order, Product product, Promotion promotion) {
        PromotionPolicy policy = product.createPromotionPolicy(promotion);
        return policy.getAdditionalFreeQuantity(order.getQuantity());
    }
}
