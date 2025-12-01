package store;

import store.controller.StoreController;
import store.console.InputView;
import store.console.OutputView;
import store.io.ResourceFileLoader;
import store.service.ProductService;
import store.service.PromotionService;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ResourceFileLoader resourceFileLoader = new ResourceFileLoader();

        StoreController controller = new StoreController(
                outputView,
                new ProductService(resourceFileLoader),
                new PromotionService(resourceFileLoader)
        );
        controller.run();
    }
}
