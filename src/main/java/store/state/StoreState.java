package store.state;

public interface StoreState {
    Class<? extends StoreState> update(StoreContext context);
}