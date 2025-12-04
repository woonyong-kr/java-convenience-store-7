package store.state;

import java.util.List;
import store.convert.parser.YesNoParser;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.domain.product.PromotionPolicy;
import store.service.OrderService;
import store.service.ProductService;
import store.service.PromotionService;
import store.support.io.Input;
import store.support.io.Output;
import store.support.state.annotation.Action;
import store.support.state.annotation.State;
import store.validation.YesNoValidator;

@State
public class CheckoutState {

    private static final String PROMOTION_NOT_APPLICABLE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String FREE_ITEM_AVAILABLE = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String ASK_MEMBERSHIP = "멤버십 할인을 받으시겠습니까? (Y/N)";

    private final YesNoValidator yesNoValidator;
    private final YesNoParser yesNoParser;

    public CheckoutState() {
        this.yesNoValidator = new YesNoValidator();
        this.yesNoParser = new YesNoParser();
    }

    @Action(order = 1)
    public void checkPromotions(StoreContext context) {
        List<Order> orders = context.getService(OrderService.class).getCurrentOrder();
        orders.forEach(order -> checkPromotion(context, order));
    }

    @Action(order = 2)
    public void askMembership(StoreContext context) {
        List<Order> orders = context.getService(OrderService.class).getCurrentOrder();
        if (orders.stream().anyMatch(order -> order.getQuantity() != 0)) {
            Output.printLine(ASK_MEMBERSHIP);
            boolean useMembership = context.retryUntilSuccess(() ->
                    Input.readLine(yesNoValidator, yesNoParser));
            context.getService(OrderService.class).applyMembership(useMembership);
        }
    }

    @Action(order = 3)
    public void goToPayment(StoreContext context) {
        context.transitionTo(PaymentState.class);
    }

    private void checkPromotion(StoreContext context, Order order) {
        Product product = context.getService(ProductService.class).findByName(order.getName());
        Promotion promotion = context.getService(PromotionService.class)
                .findByName(product.getPromotion()).orElse(null);
        PromotionPolicy policy = product.createPromotionPolicy(promotion);

        askNonPromotionPurchase(context, order, policy);
        askFreeProduct(context, order, policy);
    }

    private void askNonPromotionPurchase(StoreContext context, Order order, PromotionPolicy policy) {
        if (!policy.isActive()) {
            return;
        }
        int nonPromotionQuantity = policy.getNonPromotionQuantity(order.getQuantity());

        if (nonPromotionQuantity > 0) {
            String message = String.format(PROMOTION_NOT_APPLICABLE, order.getName(), nonPromotionQuantity);
            Output.printLine(message);
            boolean acceptFullPrice = context.retryUntilSuccess(() ->
                    Input.readLine(yesNoValidator, yesNoParser));
            if (!acceptFullPrice) {
                order.reduceQuantity(nonPromotionQuantity);
            }
        }
    }

    private void askFreeProduct(StoreContext context, Order order, PromotionPolicy policy) {
        int freeItemQuantity = policy.getAdditionalFreeQuantity(order.getQuantity());

        if (freeItemQuantity > 0) {
            String message = String.format(FREE_ITEM_AVAILABLE, order.getName(), freeItemQuantity);
            Output.printLine(message);
            boolean addFreeProduct = context.retryUntilSuccess(() ->
                    Input.readLine(yesNoValidator, yesNoParser));
            if (addFreeProduct) {
                order.addQuantity(freeItemQuantity);
            }
        }
    }
}
