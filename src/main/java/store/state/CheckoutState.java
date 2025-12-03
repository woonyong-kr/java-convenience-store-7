package store.state;

public class CheckoutState implements StoreState {

    @Override
    public Class<? extends StoreState> update(StoreContext context) {
        return AskContinueState.class;
    }
}
