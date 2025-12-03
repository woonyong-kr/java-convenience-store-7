package store.service;

import java.util.Collections;
import java.util.List;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.product.Promotion;

public class OrderService {
    private List<Order> currentOrder;
    private boolean useMembership;

    public OrderService() {
        this.useMembership = false;
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
    }

    public int getNonPromotionQuantity(Order order, Product product, Promotion promotion) {
        if (isPromotionApplicable(promotion)) {
            return Math.max(0, order.getQuantity() - getMaxPromotionApplicable(product, promotion));
        }
        return 0;
    }

    public int getFreeProductQuantity(Order order, Product product, Promotion promotion) {
        if (isPromotionApplicable(promotion) && canReceiveFreeProduct(order, product, promotion)) {
            return promotion.getGet();
        }
        return 0;
    }

    private int getMaxPromotionApplicable(Product product, Promotion promotion) {
        int unit = getPromotionUnit(promotion);
        return (product.getPromotionStock() / unit) * unit;
    }

    private boolean canReceiveFreeProduct(Order order, Product product, Promotion promotion) {
        int remainder = order.getQuantity() % getPromotionUnit(promotion);
        return remainder == promotion.getBuy()
                && product.getPromotionStock() >= order.getQuantity() + promotion.getGet();
    }

    private boolean isPromotionApplicable(Promotion promotion) {
        return promotion != null && promotion.isActive();
    }

    private int getPromotionUnit(Promotion promotion) {
        return promotion.getBuy() + promotion.getGet();
    }
}
