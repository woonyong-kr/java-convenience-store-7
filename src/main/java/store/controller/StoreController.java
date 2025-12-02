package store.controller;

import store.state.StoreContext;

public class StoreController {
    private final StoreContext storeContext;

    public StoreController(StoreContext storeContext) {
        this.storeContext = storeContext;
    }

    public void run() {
        while (!storeContext.isFinished()) {
            storeContext.update();
        }
    }
}
