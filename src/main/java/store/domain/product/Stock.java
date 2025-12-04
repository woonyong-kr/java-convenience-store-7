package store.domain.product;

public class Stock {
    private static final String ERROR_INSUFFICIENT_STOCK = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private int normalQuantity;
    private int promotionQuantity;

    public Stock(int normalQuantity, int promotionQuantity) {
        this.normalQuantity = normalQuantity;
        this.promotionQuantity = promotionQuantity;
    }

    public int getNormalQuantity() {
        return normalQuantity;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    public int getTotalQuantity() {
        return normalQuantity + promotionQuantity;
    }

    public boolean hasEnough(int quantity) {
        return getTotalQuantity() >= quantity;
    }

    public void reduce(int quantity) {
        if (!hasEnough(quantity)) {
            throw new IllegalArgumentException(ERROR_INSUFFICIENT_STOCK);
        }
        int fromPromotion = Math.min(promotionQuantity, quantity);
        promotionQuantity -= fromPromotion;
        normalQuantity -= (quantity - fromPromotion);
    }
}
