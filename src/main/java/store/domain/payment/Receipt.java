package store.domain.payment;

import java.util.List;
import store.domain.order.PurchaseResult;

public class Receipt {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MEMBERSHIP_DISCOUNT_LIMIT = 8000;

    private final List<PurchaseResult> results;
    private final boolean useMembership;

    public Receipt(List<PurchaseResult> results, boolean useMembership) {
        this.results = results;
        this.useMembership = useMembership;
    }

    public List<ReceiptLine> getLines() {
        return results.stream()
                .filter(result -> result.getQuantity() > 0)
                .map(result -> new ReceiptLine(result.getProductName(), result.getQuantity(), result.getTotalPrice()))
                .toList();
    }

    public List<ReceiptLine> getFreeLines() {
        return results.stream()
                .filter(result -> result.getFreeQuantity() > 0)
                .map(result -> new ReceiptLine(result.getProductName(), result.getFreeQuantity(), 0))
                .toList();
    }

    public int getTotalQuantity() {
        return results.stream().mapToInt(PurchaseResult::getQuantity).sum();
    }

    public int getTotalPrice() {
        return results.stream().mapToInt(PurchaseResult::getTotalPrice).sum();
    }

    public int getPromotionDiscount() {
        return results.stream().mapToInt(PurchaseResult::getPromotionDiscount).sum();
    }

    public int getMembershipDiscount() {
        if (!useMembership) {
            return 0;
        }
        int nonPromotionTotal = results.stream()
                .mapToInt(PurchaseResult::getNonPromotionPrice)
                .sum();
        int discount = (int) (nonPromotionTotal * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, MEMBERSHIP_DISCOUNT_LIMIT);
    }

    public int getFinalPrice() {
        return getTotalPrice() - getPromotionDiscount() - getMembershipDiscount();
    }
}
