package store.state;

import store.support.text.mapper.ProductTextMapper;

public class ShowProductsState implements StoreState {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String PRODUCT_LIST_MESSAGE = "현재 보유하고 있는 상품입니다.";

    private final ProductTextMapper  productTextMapper = new ProductTextMapper();

    public ShowProductsState() {
    }

    @Override
    public Class<? extends StoreState> update(StoreContext context) {
        context.getOutputView().printLine(WELCOME_MESSAGE);
        context.getOutputView().printLine(PRODUCT_LIST_MESSAGE);
        context.getOutputView().printLine("");

        context.getProductService().getProducts().forEach(product -> {
            context.getOutputView().printLine(product, productTextMapper);});
        context.getOutputView().printLine("");

        return InputPurchaseState.class;
    }
}
