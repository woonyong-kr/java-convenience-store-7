package store.state;

import java.util.List;
import store.convert.mapper.ReceiptTextMapper;
import store.domain.order.Order;
import store.domain.payment.Receipt;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.support.io.Output;

public class PaymentState implements StoreState {

    private final ReceiptTextMapper receiptTextMapper;

    public PaymentState() {
        this.receiptTextMapper = new ReceiptTextMapper();
    }

    @Override
    public Class<? extends StoreState> update(StoreContext context) {
        List<Order> orders = context.getOrderService().getCurrentOrder();
        List<Product> products = context.getProductService().getProducts();
        List<Promotion> promotions = context.getPromotionService().getPromotions();
        boolean useMembership = context.getOrderService().isUseMembership();

        Receipt receipt = context.getPaymentService().createReceipt(orders, products, promotions, useMembership);
        Output.printLine(receipt, receiptTextMapper);

        orders.forEach(order ->
                context.getProductService().sellProduct(order.getName(), order.getQuantity()));

        context.getOrderService().clearOrder();
        return AskContinueState.class;
    }
}
