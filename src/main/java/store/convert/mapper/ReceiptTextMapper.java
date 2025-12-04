package store.convert.mapper;

import store.domain.payment.Receipt;
import store.domain.payment.ReceiptLine;
import store.support.convert.Mapper;

public class ReceiptTextMapper implements Mapper<Receipt> {

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
        sb.append(HEADER).append(Mapper.LINE_DELIMITER);
        sb.append(COLUMN_HEADER).append(Mapper.LINE_DELIMITER);
    }

    private void appendLines(StringBuilder sb, Receipt receipt) {
        for (ReceiptLine line : receipt.getLines()) {
            sb.append(String.format(LINE_FORMAT, line.getName(), line.getQuantity(),
                            Mapper.number(line.getPrice())))
                    .append(Mapper.LINE_DELIMITER);
        }
    }

    private void appendFreeSection(StringBuilder sb, Receipt receipt) {
        sb.append(FREE_HEADER).append(Mapper.LINE_DELIMITER);
        for (ReceiptLine line : receipt.getFreeLines()) {
            sb.append(String.format(FREE_LINE_FORMAT, line.getName(), line.getQuantity()))
                    .append(Mapper.LINE_DELIMITER);
        }
    }

    private void appendSummary(StringBuilder sb, Receipt receipt) {
        sb.append(FOOTER).append(Mapper.LINE_DELIMITER);
        sb.append(String.format(TOTAL_FORMAT, receipt.getTotalQuantity(), Mapper.number(receipt.getTotalPrice())))
                .append(Mapper.LINE_DELIMITER);
        sb.append(String.format(PROMOTION_DISCOUNT_FORMAT, Mapper.number(receipt.getPromotionDiscount())))
                .append(Mapper.LINE_DELIMITER);
        sb.append(String.format(MEMBERSHIP_DISCOUNT_FORMAT, Mapper.number(receipt.getMembershipDiscount())))
                .append(Mapper.LINE_DELIMITER);
        sb.append(String.format(FINAL_PRICE_FORMAT, Mapper.number(receipt.getFinalPrice())));
    }
}
