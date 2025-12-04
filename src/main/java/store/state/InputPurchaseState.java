package store.state;

import java.util.List;
import store.domain.order.Order;
import store.convert.parser.InputPurchaseParser;
import store.validation.InputPurchaseValidator;

public class InputPurchaseState implements StoreState {
    private static final String INPUT_PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    private final InputPurchaseValidator inputPurchaseValidator;
    private final InputPurchaseParser inputPurchaseParser;

    public InputPurchaseState() {
        this.inputPurchaseValidator = new InputPurchaseValidator();
        this.inputPurchaseParser = new InputPurchaseParser();
    }

    @Override
    public Class<? extends StoreState> update(StoreContext context) {
        context.getOutputView().printLine(INPUT_PURCHASE_MESSAGE);

        List<Order> orders = context.retryUntilSuccess(
                () -> {
                    List<Order> items;
                    items = context.getInputView()
                            .readLine(inputPurchaseValidator, inputPurchaseParser);
                    items.forEach(item ->
                            context.getProductService()
                                    .checkOrder(item.getName(), item.getQuantity()));
                    return items;
                });

        context.getOrderService().registerOrder(orders);

        return CheckoutState.class;
    }
}
