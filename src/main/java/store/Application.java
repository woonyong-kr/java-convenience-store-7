package store;

import store.console.CStoreController;
import store.console.InputView;
import store.console.OutputView;

public class Application {
    public static void main(String[] args) {
        CStoreController controller = new CStoreController(
                new InputView(),
                new OutputView()
        );
        controller.run();
    }
}
