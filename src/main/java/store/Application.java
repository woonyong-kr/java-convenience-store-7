package store;

import store.controller.StoreController;
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

public class Application {

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        PromotionService promotionService = new PromotionService();
        OrderService orderService = new OrderService();
        PaymentService paymentService = new PaymentService();

        StoreContext storeContext = new StoreContext(
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
