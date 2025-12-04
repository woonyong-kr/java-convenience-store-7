package store.state;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import store.support.io.Output;
import store.support.service.Service;

public class StoreContext {
    private static final String ERROR_SERVICE_NOT_FOUND = "[ERROR] 등록되지 않은 서비스입니다: ";

    private final Map<Class<? extends Service>,  Service> services;
    private final Map<Class<? extends StoreState>, StoreState> storeState;
    private StoreState currentState;

    public StoreContext(
            Service[] services,
            StoreState ... storeState
    ) {
        this.services = Arrays.stream(services)
                .collect(Collectors.toMap(Service::getClass, service -> service));
        this.storeState = Arrays.stream(storeState)
                .collect(Collectors.toMap(StoreState::getClass, state -> state));
        this.currentState = storeState[0];
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(Class<T> type) {
        Service service = services.get(type);
        if (service == null) {
            throw new IllegalArgumentException(ERROR_SERVICE_NOT_FOUND + type.getName());
        }
        return (T) service;
    }

    public void update() {
        currentState = storeState.get(currentState.update(this));
    }

    public <T> T retryUntilSuccess(Supplier<T> task) {
        while (true) {
            try {
                return task.get();
            } catch (IllegalArgumentException e) {
                Output.printLine(e.getMessage());
            }
        }
    }

    public boolean isFinished() {
        return currentState == null;
    }
}
