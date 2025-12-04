package store.domain.product;

import java.time.LocalDate;

public class PromotionPolicy {
    private final Promotion promotion;
    private final int promotionStock;

    private PromotionPolicy(Promotion promotion, int promotionStock) {
        this.promotion = promotion;
        this.promotionStock = promotionStock;
    }

    public static PromotionPolicy of(Promotion promotion, int promotionStock) {
        return new PromotionPolicy(promotion, promotionStock);
    }

    public static PromotionPolicy none() {
        return new PromotionPolicy(null, 0);
    }

    public boolean isActive() {
        return isActive(LocalDate.now());
    }

    public boolean isActive(LocalDate date) {
        return promotion != null && promotion.isActive(date);
    }

    public int getUnit() {
        if (!isActive()) {
            return 0;
        }
        return promotion.getBuy() + promotion.getGet();
    }

    public int getMaxApplicableQuantity() {
        if (!isActive()) {
            return 0;
        }
        int unit = getUnit();
        return (promotionStock / unit) * unit;
    }

    public int getNonPromotionQuantity(int orderQuantity) {
        if (!isActive()) {
            return orderQuantity;
        }
        return Math.max(0, orderQuantity - getMaxApplicableQuantity());
    }

    public int getPromotionApplicableQuantity(int orderQuantity) {
        if (!isActive()) {
            return 0;
        }
        return Math.min(orderQuantity, getMaxApplicableQuantity());
    }

    public int getAdditionalFreeQuantity(int orderQuantity) {
        if (!isActive()) {
            return 0;
        }
        int unit = getUnit();
        int remainder = orderQuantity % unit;
        boolean canAddMore = remainder == promotion.getBuy()
                && promotionStock >= orderQuantity + promotion.getGet();
        if (canAddMore) {
            return promotion.getGet();
        }
        return 0;
    }

    public int getTotalFreeQuantity(int orderQuantity) {
        if (!isActive()) {
            return 0;
        }
        int unit = getUnit();
        int applicableQuantity = getPromotionApplicableQuantity(orderQuantity);
        return (applicableQuantity / unit) * promotion.getGet();
    }
}
