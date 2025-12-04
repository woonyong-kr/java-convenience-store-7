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
import store.support.state.annotation.Action;
import store.support.state.annotation.State;

@State
public class PaymentState {

    private final ReceiptTextMapper receiptTextMapper;
    private List<Order> validOrders;

    public PaymentState() {
        this.receiptTextMapper = new ReceiptTextMapper();
    }

    @Action(order = 1)
    public void filterValidOrders(StoreContext context) {
        List<Order> orders = context.getService(OrderService.class).getCurrentOrder();
        this.validOrders = orders.stream()
                .filter(order -> order.getQuantity() > 0)
                .toList();

        if (validOrders.isEmpty()) {
            context.getService(OrderService.class).clearOrder();
            context.transitionTo(AskContinueState.class);
        }
    }

    @Action(order = 2)
    public void printReceipt(StoreContext context) {
        List<Product> products = context.getService(ProductService.class).getProducts();
        List<Promotion> promotions = context.getService(PromotionService.class).getPromotions();
        boolean useMembership = context.getService(OrderService.class).isUseMembership();

        Receipt receipt = context.getService(PaymentService.class)
                .createReceipt(validOrders, products, promotions, useMembership);
        Output.printLine(receipt, receiptTextMapper);
    }

    @Action(order = 3)
    public void deductStock(StoreContext context) {
        validOrders.forEach(order ->
                context.getService(ProductService.class).sellProduct(order.getName(), order.getQuantity()));
    }

    @Action(order = 4)
    public void clearAndTransition(StoreContext context) {
        context.getService(OrderService.class).clearOrder();
        context.transitionTo(AskContinueState.class);
    }
}
