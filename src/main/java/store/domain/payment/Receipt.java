package store.domain.payment;

import java.util.List;

public class Receipt {
    private final List<ReceiptLine> receiptLines;
    private final List<ReceiptLine> freeReceiptLines;
    private final int totalQuantity;
    private final int totalPrice;
    private final int promotionDiscount;
    private final int membershipDiscount;

    public Receipt(
            List<ReceiptLine> receiptLines,
            List<ReceiptLine> freeFeceiptLines,
            int totalQuantity,
            int totalPrice,
            int promotionDiscount,
            int membershipDiscount
    ) {
        this.receiptLines = receiptLines;
        this.freeReceiptLines = freeFeceiptLines;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public List<ReceiptLine> getLines() {
        return receiptLines;
    }

    public List<ReceiptLine> getFreeLines() {
        return freeReceiptLines;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalPrice() {
        return totalPrice - promotionDiscount - membershipDiscount;
    }
}
