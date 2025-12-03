package store.support.text.mapper;

import java.text.DecimalFormat;
import store.domain.payment.Receipt;
import store.domain.payment.ReceiptLine;

public class ReceiptTextMapper implements TextMapper<Receipt, String> {
    private static final String HEADER = "==============W 편의점================";
    private static final String FREE_HEADER = "=============증\t정===============";
    private static final String FOOTER = "====================================";

    private static final String COLUMN_HEADER = "상품명\t\t수량\t금액";
    private static final String LINE_FORMAT = "%s\t\t%d\t%s";
    private static final String FREE_LINE_FORMAT = "%s\t\t%d";
    private static final String TOTAL_FORMAT = "총구매액\t\t%d\t%s";
    private static final String PROMOTION_DISCOUNT_FORMAT = "행사할인\t\t\t-%s";
    private static final String MEMBERSHIP_DISCOUNT_FORMAT = "멤버십할인\t\t\t-%s";
    private static final String FINAL_PRICE_FORMAT = "내실돈\t\t\t %s";

    private static final String LINE_BREAK = "\n";
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###");

    @Override
    public String map(Receipt receipt) {
        StringBuilder sb = new StringBuilder();

        appendHeader(sb);
        appendLines(sb, receipt);
        appendFreeSection(sb, receipt);
        appendSummary(sb, receipt);

        return sb.toString();
    }

    private void appendHeader(StringBuilder sb) {
        sb.append(HEADER).append(LINE_BREAK);
        sb.append(COLUMN_HEADER).append(LINE_BREAK);
    }

    private void appendLines(StringBuilder sb, Receipt receipt) {
        for (ReceiptLine line : receipt.getLines()) {
            sb.append(String.format(LINE_FORMAT, line.getName(), line.getQuantity(),
                            PRICE_FORMAT.format(line.getPrice())))
                    .append(LINE_BREAK);
        }
    }

    private void appendFreeSection(StringBuilder sb, Receipt receipt) {
        sb.append(FREE_HEADER).append(LINE_BREAK);
        for (ReceiptLine line : receipt.getFreeLines()) {
            sb.append(String.format(FREE_LINE_FORMAT, line.getName(), line.getQuantity()))
                    .append(LINE_BREAK);
        }
    }

    private void appendSummary(StringBuilder sb, Receipt receipt) {
        sb.append(FOOTER).append(LINE_BREAK);
        sb.append(String.format(TOTAL_FORMAT, receipt.getTotalQuantity(), PRICE_FORMAT.format(receipt.getTotalPrice())))
                .append(LINE_BREAK);
        sb.append(String.format(PROMOTION_DISCOUNT_FORMAT, PRICE_FORMAT.format(receipt.getPromotionDiscount())))
                .append(LINE_BREAK);
        sb.append(String.format(MEMBERSHIP_DISCOUNT_FORMAT, PRICE_FORMAT.format(receipt.getMembershipDiscount())))
                .append(LINE_BREAK);
        sb.append(String.format(FINAL_PRICE_FORMAT, PRICE_FORMAT.format(receipt.getFinalPrice())));
    }
}
