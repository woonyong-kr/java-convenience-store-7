package store.state;

import java.util.function.Supplier;
import store.console.InputView;
import store.console.OutputView;
import store.io.ResourceFileLoader;
import store.service.ProductService;
import store.service.PromotionService;

public class StoreContext {

    private final InputView inputView;
    private final OutputView outputView;
    private final ResourceFileLoader resourceFileLoader;
    private final ProductService productService;
    private final PromotionService promotionService;
    private StoreState currentState;

    public StoreContext(
            InputView inputView,
            OutputView outputView,
            ResourceFileLoader resourceFileLoader,
            ProductService productService,
            PromotionService promotionService,
            StoreState ... storeState
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.resourceFileLoader = resourceFileLoader;
        this.productService = productService;
        this.promotionService = promotionService;
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

    public ProductService  getProductService() {
        return productService;
    }

    public PromotionService getPromotionService() {
        return promotionService;
    }

    public void update() {
        currentState = currentState.update(this);
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
