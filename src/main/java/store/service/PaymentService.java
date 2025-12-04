package store.service;

import java.util.List;
import store.domain.order.Order;
import store.domain.payment.Receipt;
import store.domain.payment.ReceiptLine;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.support.service.Service;

public class PaymentService extends Service {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MEMBERSHIP_DISCOUNT_LIMIT = 8000;

    public Receipt createReceipt(List<Order> orders, List<Product> products, List<Promotion> promotions, boolean useMembership) {
        List<ReceiptLine> lines = orders.stream()
                .map(order -> {
                    Product product = findProduct(products, order.getName());
                    return new ReceiptLine(order.getName(), order.getQuantity(), product.getPrice() * order.getQuantity());
                })
                .toList();

        List<ReceiptLine> freeLines = orders.stream()
                .map(order -> {
                    Product product = findProduct(products, order.getName());
                    Promotion promotion = findPromotion(promotions, product.getPromotion());
                    int freeCount = getFreeCount(order, product, promotion);
                    return new ReceiptLine(order.getName(), freeCount, 0);
                })
                .filter(line -> line.getQuantity() > 0)
                .toList();

        int totalQuantity = orders.stream().mapToInt(Order::getQuantity).sum();
        int totalPrice = calculateTotalPrice(orders, products);
        int promotionDiscount = calculatePromotionDiscount(orders, products, promotions);
        int membershipDiscount = calculateMembershipDiscount(orders, products, promotions, useMembership);

        return new Receipt(lines, freeLines, totalQuantity, totalPrice, promotionDiscount, membershipDiscount);
    }


    public int calculateTotalPrice(List<Order> orders, List<Product> products) {
        return orders.stream()
                .mapToInt(order -> findProduct(products, order.getName()).getPrice() * order.getQuantity())
                .sum();
    }

    public int calculatePromotionDiscount(List<Order> orders, List<Product> products, List<Promotion> promotions) {
        return orders.stream()
                .mapToInt(order -> calculateOrderPromotionDiscount(order, products, promotions))
                .sum();
    }

    public int calculateMembershipDiscount(List<Order> orders, List<Product> products, List<Promotion> promotions, boolean useMembership) {
        if (!useMembership) {
            return 0;
        }
        int nonPromotionTotal = orders.stream()
                .mapToInt(order -> calculateNonPromotionPrice(order, products, promotions))
                .sum();
        int discount = (int) (nonPromotionTotal * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, MEMBERSHIP_DISCOUNT_LIMIT);
    }

    private int calculateOrderPromotionDiscount(Order order, List<Product> products, List<Promotion> promotions) {
        Product product = findProduct(products, order.getName());
        Promotion promotion = findPromotion(promotions, product.getPromotion());
        int freeCount = getFreeCount(order, product, promotion);
        return product.getPrice() * freeCount;
    }

    private int calculateNonPromotionPrice(Order order, List<Product> products, List<Promotion> promotions) {
        Product product = findProduct(products, order.getName());
        Promotion promotion = findPromotion(promotions, product.getPromotion());
        int nonPromotionQuantity = getNonPromotionQuantity(order, product, promotion);
        return product.getPrice() * nonPromotionQuantity;
    }

    private int getFreeCount(Order order, Product product, Promotion promotion) {
        if (!isPromotionApplicable(promotion)) {
            return 0;
        }
        int unit = promotion.getBuy() + promotion.getGet();
        int promotionApplicableQuantity = getPromotionApplicableQuantity(order, product, unit);
        return promotionApplicableQuantity / unit * promotion.getGet();
    }

    private int getNonPromotionQuantity(Order order, Product product, Promotion promotion) {
        if (!isPromotionApplicable(promotion)) {
            return order.getQuantity();
        }
        int unit = promotion.getBuy() + promotion.getGet();
        int promotionApplicableQuantity = getPromotionApplicableQuantity(order, product, unit);
        return order.getQuantity() - promotionApplicableQuantity;
    }

    private int getPromotionApplicableQuantity(Order order, Product product, int unit) {
        int maxPromotionStock = (product.getPromotionStock() / unit) * unit;
        return Math.min(order.getQuantity(), maxPromotionStock);
    }

    private boolean isPromotionApplicable(Promotion promotion) {
        return promotion != null && promotion.isActive();
    }

    private Product findProduct(List<Product> products, String name) {
        return products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private Promotion findPromotion(List<Promotion> promotions, String name) {
        if (name == null) {
            return null;
        }
        return promotions.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
