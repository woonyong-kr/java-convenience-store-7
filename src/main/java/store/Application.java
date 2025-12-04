package store;

import store.controller.StoreController;
import store.console.InputView;
import store.console.OutputView;
import store.io.ResourceFileLoader;
import store.service.OrderService;
import store.service.PaymentService;
import store.service.ProductService;
import store.service.PromotionService;
import store.state.AskContinueState;
import store.state.CheckoutState;
import store.state.InputPurchaseState;
import store.state.PaymentState;
import store.state.ShowProductsState;
import store.state.StoreContext;
import store.support.state.runtime.StateRunner;

public class Application {
    public static void main(String[] args) {

        StateRunner.run("store.statetest");

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ResourceFileLoader resourceFileLoader = new ResourceFileLoader();

        ProductService productService = new ProductService(resourceFileLoader);
        PromotionService promotionService = new PromotionService(resourceFileLoader);
        OrderService orderService = new OrderService();
        PaymentService paymentService = new PaymentService();

        StoreContext storeContext = new StoreContext(
                inputView,
                outputView,
                resourceFileLoader,
                productService,
                promotionService,
                orderService,
                paymentService,
                new ShowProductsState(),
                new InputPurchaseState(),
                new CheckoutState(),
                new PaymentState(),
                new AskContinueState()
        );

        StoreController controller = new StoreController(storeContext);

        controller.run();
    }
}
