package store.state;

import java.util.List;
import store.convert.parser.YesNoParser;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.support.io.Input;
import store.support.io.Output;
import store.validation.YesNoValidator;

public class CheckoutState implements StoreState {

    private static final String PROMOTION_NOT_APPLICABLE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String FREE_ITEM_AVAILABLE = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String ASK_MEMBERSHIP = "멤버십 할인을 받으시겠습니까? (Y/N)";

    private final YesNoValidator yesNoValidator;
    private final YesNoParser yesNoParser;

    public CheckoutState() {
        this.yesNoValidator = new YesNoValidator();
        this.yesNoParser = new YesNoParser();
    }

    @Override
    public Class<? extends StoreState> update(StoreContext context) {
        List<Order> orders = context.getOrderService().getCurrentOrder();
        orders.forEach(order -> checkPromotion(context, order));

        if (orders.stream().anyMatch(order -> order.getQuantity() != 0)) {
            askUseMembership(context);
        }
        return PaymentState.class;
    }

    private void checkPromotion(StoreContext context, Order order) {
        Product product = context.getProductService().findByName(order.getName());
        Promotion promotion = context.getPromotionService()
                .findByName(product.getPromotion()).orElse(null);

        askNonPromotionPurchase(context, order, product, promotion);
        askFreeProduct(context, order, product, promotion);
    }

    private void askNonPromotionPurchase(StoreContext context, Order order, Product product, Promotion promotion) {
        int nonPromotionQuantity = context.getOrderService()
                .getNonPromotionQuantity(order, product, promotion);

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

    private void askFreeProduct(StoreContext context, Order order, Product product, Promotion promotion) {
        int freeItemQuantity = context.getOrderService()
                .getFreeProductQuantity(order, product, promotion);

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

    private void askUseMembership(StoreContext context) {
        Output.printLine(ASK_MEMBERSHIP);
        boolean useMembership = context.retryUntilSuccess(() ->
                Input.readLine(yesNoValidator, yesNoParser));
        context.getOrderService().applyMembership(useMembership);
    }
}
