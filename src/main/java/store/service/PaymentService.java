package store.service;

import java.util.List;
import store.domain.order.Order;
import store.domain.order.PurchaseResult;
import store.domain.payment.Receipt;
import store.domain.payment.ReceiptLine;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.domain.product.PromotionPolicy;
import store.support.service.Service;

public class PaymentService extends Service {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MEMBERSHIP_DISCOUNT_LIMIT = 8000;

    public Receipt createReceipt(List<Order> orders, List<Product> products, List<Promotion> promotions, boolean useMembership) {
        List<PurchaseResult> results = createPurchaseResults(orders, products, promotions);

        List<ReceiptLine> lines = results.stream()
                .map(result -> new ReceiptLine(result.getProductName(), result.getQuantity(), result.getTotalPrice()))
                .toList();

        List<ReceiptLine> freeLines = results.stream()
                .filter(result -> result.getFreeQuantity() > 0)
                .map(result -> new ReceiptLine(result.getProductName(), result.getFreeQuantity(), 0))
                .toList();

        int totalQuantity = results.stream().mapToInt(PurchaseResult::getQuantity).sum();
        int totalPrice = results.stream().mapToInt(PurchaseResult::getTotalPrice).sum();
        int promotionDiscount = results.stream().mapToInt(PurchaseResult::getPromotionDiscount).sum();
        int membershipDiscount = calculateMembershipDiscount(results, useMembership);

        return new Receipt(lines, freeLines, totalQuantity, totalPrice, promotionDiscount, membershipDiscount);
    }

    private List<PurchaseResult> createPurchaseResults(List<Order> orders, List<Product> products, List<Promotion> promotions) {
        return orders.stream()
                .map(order -> {
                    Product product = findProduct(products, order.getName());
                    Promotion promotion = findPromotion(promotions, product.getPromotion());
                    PromotionPolicy policy = product.createPromotionPolicy(promotion);
                    return PurchaseResult.of(order, product, policy);
                })
                .toList();
    }


    private int calculateMembershipDiscount(List<PurchaseResult> results, boolean useMembership) {
        if (!useMembership) {
            return 0;
        }
        int nonPromotionTotal = results.stream()
                .mapToInt(PurchaseResult::getNonPromotionPrice)
                .sum();
        int discount = (int) (nonPromotionTotal * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, MEMBERSHIP_DISCOUNT_LIMIT);
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
