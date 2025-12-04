package store.state;

import java.util.List;
import store.convert.parser.InputPurchaseParser;
import store.domain.order.Order;
import store.service.OrderService;
import store.service.ProductService;
import store.support.io.Input;
import store.support.io.Output;
import store.validation.InputPurchaseValidator;
import store.support.state.annotation.Action;
import store.support.state.annotation.State;

@State
public class InputPurchaseState {

    private static final String INPUT_PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    private final InputPurchaseValidator inputPurchaseValidator;
    private final InputPurchaseParser inputPurchaseParser;
    private List<Order> orders;

    public InputPurchaseState() {
        this.inputPurchaseValidator = new InputPurchaseValidator();
        this.inputPurchaseParser = new InputPurchaseParser();
    }

    @Action(order = 1)
    public void showPrompt(StoreContext context) {
        Output.printLine(INPUT_PURCHASE_MESSAGE);
    }

    @Action(order = 2)
    public void readAndValidateInput(StoreContext context) {
        this.orders = context.retryUntilSuccess(() -> {
            List<Order> items = Input.readLine(inputPurchaseValidator, inputPurchaseParser);
            items.forEach(item ->
                    context.getService(ProductService.class).checkOrder(item.getName(), item.getQuantity()));
            return items;
        });
    }

    @Action(order = 3)
    public void registerOrder(StoreContext context) {
        context.getService(OrderService.class).registerOrder(orders);
        context.transitionTo(CheckoutState.class);
    }
}
