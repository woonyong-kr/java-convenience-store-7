package store.state;

import java.util.List;
import store.convert.mapper.ReceiptTextMapper;
import store.domain.order.Order;
import store.domain.payment.Receipt;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.service.OrderService;
import store.service.PaymentService;
import store.service.ProductService;
import store.service.PromotionService;
import store.support.io.Output;

public class PaymentState implements StoreState {

    private final ReceiptTextMapper receiptTextMapper;

    public PaymentState() {
        this.receiptTextMapper = new ReceiptTextMapper();
    }

    @Override
    public Class<? extends StoreState> update(StoreContext context) {
        List<Order> orders = context.getService(OrderService.class).getCurrentOrder();
        List<Product> products = context.getService(ProductService.class).getProducts();
        List<Promotion> promotions = context.getService(PromotionService.class).getPromotions();
        boolean useMembership = context.getService(OrderService.class).isUseMembership();

        Receipt receipt = context.getService(PaymentService.class).createReceipt(orders, products, promotions, useMembership);
        Output.printLine(receipt, receiptTextMapper);

        orders.forEach(order ->
                context.getService(ProductService.class).sellProduct(order.getName(), order.getQuantity()));

        context.getService(OrderService.class).clearOrder();
        return AskContinueState.class;
    }
}
