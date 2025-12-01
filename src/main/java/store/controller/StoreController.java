package store.controller;

import java.util.function.Supplier;
import store.console.InputView;
import store.console.OutputView;
import store.io.ResourceFileLoader;
import store.service.ProductService;
import store.service.PromotionService;

public class StoreController {
    private final OutputView outputView;
    private final ProductService productService;
    private final PromotionService  promotionService;

    public StoreController(
            OutputView outputView,
            ProductService productService,
            PromotionService  promotionService
    ) {
        this.outputView = outputView;
        this.productService = productService;
        this.promotionService = promotionService;
    }

    public void run() {
        productService.loadProduct();
        promotionService.loadPromotions();
    }

    private <T> T retryUntilSuccess(Supplier<T> task) {
        while (true) {
            try {
                return task.get();
            } catch (IllegalArgumentException e) {
                outputView.printLine(e.getMessage());
            }
        }
    }
}
