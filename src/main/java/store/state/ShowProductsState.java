package store.state;

import store.convert.mapper.ProductTextMapper;
import store.service.ProductService;
import store.support.io.Output;

public class ShowProductsState implements StoreState {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String PRODUCT_LIST_MESSAGE = "현재 보유하고 있는 상품입니다.";

    private final ProductTextMapper productTextMapper = new ProductTextMapper();

    public ShowProductsState() {
    }

    @Override
    public Class<? extends StoreState> update(StoreContext context) {
        Output.printLine(WELCOME_MESSAGE);
        Output.printLine(PRODUCT_LIST_MESSAGE);
        Output.printLine();

        context.getService(ProductService.class).getProducts()
                .forEach(product -> Output.printLine(product, productTextMapper));
        Output.printLine();

        return InputPurchaseState.class;
    }
}
