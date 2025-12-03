package store;

import store.controller.StoreController;
import store.console.InputView;
import store.console.OutputView;
import store.io.ResourceFileLoader;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.state.AskContinueState;
import store.state.CheckoutState;
import store.state.InputPurchaseState;
import store.state.ShowProductsState;
import store.state.StoreContext;

public class Application {
    public static void main(String[] args) {

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ResourceFileLoader resourceFileLoader = new ResourceFileLoader();

        ProductService productService = new ProductService(resourceFileLoader);
        PromotionService promotionService = new PromotionService(resourceFileLoader);
        OrderService orderService = new OrderService();

        StoreContext storeContext = new StoreContext(
                inputView,
                outputView,
                resourceFileLoader,
                productService,
                promotionService,
                orderService,
                new ShowProductsState(),
                new InputPurchaseState(),
                new CheckoutState(),
                new AskContinueState()
        );

        StoreController controller = new StoreController(storeContext);

        controller.run();
    }
}
