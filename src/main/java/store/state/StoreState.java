package store.state;

public interface StoreState {
    StoreState update(StoreContext context);
}