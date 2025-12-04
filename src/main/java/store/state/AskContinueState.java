package store.state;

import store.convert.parser.YesNoParser;
import store.support.io.Input;
import store.support.io.Output;
import store.support.state.annotation.Action;
import store.support.state.annotation.State;
import store.validation.YesNoValidator;

@State
public class AskContinueState {

    private static final String ASK_CONTINUE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    private final YesNoValidator yesNoValidator;
    private final YesNoParser yesNoParser;

    public AskContinueState() {
        this.yesNoValidator = new YesNoValidator();
        this.yesNoParser = new YesNoParser();
    }

    @Action
    public void askContinue(StoreContext context) {
        Output.printLine(ASK_CONTINUE_MESSAGE);

        boolean continues = context.retryUntilSuccess(() ->
                Input.readLine(yesNoValidator, yesNoParser));

        if (continues) {
            context.transitionTo(ShowProductsState.class);
            return;
        }
        context.finish();
    }
}
