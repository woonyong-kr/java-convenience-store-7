package store.state;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import store.service.OrderService;
import store.service.PaymentService;
import store.service.ProductService;
import store.service.PromotionService;
import store.support.io.Output;

public class StoreContext {

    private final ProductService productService;
    private final PromotionService promotionService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final Map<Class<? extends StoreState>, StoreState> storeState;
    private StoreState currentState;

    public StoreContext(
            ProductService productService,
            PromotionService promotionService,
            OrderService orderService,
            PaymentService paymentService,
            StoreState... storeState
    ) {
        this.productService = productService;
        this.promotionService = promotionService;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.storeState = Arrays.stream(storeState)
                .collect(Collectors.toMap(StoreState::getClass, state -> state));
        this.currentState = storeState[0];
    }

    public ProductService getProductService() {
        return productService;
    }

    public PromotionService getPromotionService() {
        return promotionService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void update() {
        currentState = storeState.get(currentState.update(this));
    }

    public <T> T retryUntilSuccess(Supplier<T> task) {
        while (true) {
            try {
                return task.get();
            } catch (IllegalArgumentException e) {
                Output.printLine(e.getMessage());
            }
        }
    }

    public boolean isFinished() {
        return currentState == null;
    }
}
