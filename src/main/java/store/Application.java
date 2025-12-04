package store;

import store.service.OrderService;
import store.service.PaymentService;
import store.service.ProductService;
import store.service.PromotionService;
import store.state.StoreContext;
import store.support.state.runtime.StateRunner;

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
                paymentService
        );

        StateRunner.run("store.state", storeContext);
    }
}
