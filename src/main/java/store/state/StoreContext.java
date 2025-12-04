package store.state;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import store.support.io.Output;
import store.support.service.Service;
import store.support.state.runtime.StateContext;

public class StoreContext implements StateContext {
    private static final String ERROR_SERVICE_NOT_FOUND = "[ERROR] 등록되지 않은 서비스입니다: ";

    private final Map<Class<? extends Service>, Service> services;
    private Class<?> pendingTransition;
    private boolean finished;

    public StoreContext(Service ... services) {
        this.services = Arrays.stream(services)
                .collect(Collectors.toMap(Service::getClass, service -> service));
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(Class<T> type) {
        Service service = services.get(type);
        if (service == null) {
            throw new IllegalArgumentException(ERROR_SERVICE_NOT_FOUND + type.getName());
        }
        return (T) service;
    }

    @Override
    public void transitionTo(Class<?> nextState) {
        this.pendingTransition = nextState;
    }

    @Override
    public void finish() {
        this.finished = true;
    }

    @Override
    public boolean hasPendingTransition() {
        return pendingTransition != null;
    }

    @Override
    public Class<?> consumePendingTransition() {
        Class<?> next = pendingTransition;
        pendingTransition = null;
        return next;
    }

    @Override
    public boolean isFinished() {
        return finished;
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
}
