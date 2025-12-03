package store.state;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import store.console.InputView;
import store.console.OutputView;
import store.io.ResourceFileLoader;
import store.service.OrderService;
import store.service.PaymentService;
import store.service.ProductService;
import store.service.PromotionService;

public class StoreContext {

    private final InputView inputView;
    private final OutputView outputView;
    private final ResourceFileLoader resourceFileLoader;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final Map<Class<? extends StoreState>, StoreState> storeState;
    private StoreState currentState;

    public StoreContext(
            InputView inputView,
            OutputView outputView,
            ResourceFileLoader resourceFileLoader,
            ProductService productService,
            PromotionService promotionService,
            OrderService orderService,
            PaymentService paymentService,
            StoreState... storeState
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.resourceFileLoader = resourceFileLoader;
        this.productService = productService;
        this.promotionService = promotionService;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.storeState = Arrays.stream(storeState)
                .collect(Collectors.toMap(StoreState::getClass, state -> state));
        this.currentState = storeState[0];
    }

    public InputView getInputView() {
        return inputView;
    }

    public OutputView getOutputView() {
        return outputView;
    }

    public ResourceFileLoader getResourceFileLoader() {
        return resourceFileLoader;
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
                outputView.printLine(e.getMessage());
            }
        }
    }

    public boolean isFinished() {
        return currentState == null;
    }
}
