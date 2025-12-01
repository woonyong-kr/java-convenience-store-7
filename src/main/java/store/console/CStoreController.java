package store.console;

import java.util.function.Supplier;

public class CStoreController {
    private final InputView inputView;
    private final OutputView outputView;

    public CStoreController(
            InputView inputView,
            OutputView outputView
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
    }

    private <T> T retryUntilSuccess(Supplier<T> task) {
        while (true) {
            try {
                return task.get();
            } catch (IllegalArgumentException e) {
                outputView.printLine(e.getMessage());
            }
        }
    }
}
