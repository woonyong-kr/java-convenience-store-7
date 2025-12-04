package store.state;

import store.convert.parser.YesNoParser;
import store.support.io.Input;
import store.support.io.Output;
import store.validation.YesNoValidator;

public class AskContinueState implements StoreState {

    private static final String ASK_CONTINUE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    private final YesNoValidator yesNoValidator;
    private final YesNoParser yesNoParser;

    public AskContinueState() {
        this.yesNoValidator = new YesNoValidator();
        this.yesNoParser = new YesNoParser();
    }

    @Override
    public Class<? extends StoreState> update(StoreContext context) {
        Output.printLine(ASK_CONTINUE_MESSAGE);

        boolean continues = context.retryUntilSuccess(() ->
                Input.readLine(yesNoValidator, yesNoParser));

        if (continues) {
            return ShowProductsState.class;
        }
        return null;
    }
}
