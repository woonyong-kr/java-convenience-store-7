package store.state;

import store.convert.mapper.ProductTextMapper;
import store.service.ProductService;
import store.support.io.Output;
import store.support.state.annotation.Action;
import store.support.state.annotation.State;

@State(initial = true)
public class ShowProductsState {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String PRODUCT_LIST_MESSAGE = "현재 보유하고 있는 상품입니다.";

    private final ProductTextMapper productTextMapper;

    public ShowProductsState() {
        this.productTextMapper = new ProductTextMapper();
    }

    @Action
    public void showProducts(StoreContext context) {
        Output.printLine(WELCOME_MESSAGE);
        Output.printLine(PRODUCT_LIST_MESSAGE);
        Output.printLine();

        context.getService(ProductService.class).getProducts()
                .forEach(product -> Output.printLine(product, productTextMapper));
        Output.printLine();

        context.transitionTo(InputPurchaseState.class);
    }
}
