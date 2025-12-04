package store.support.state.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import store.support.state.annotation.Action;
import store.support.state.annotation.Order;

public class StateEngine {

    private static final String ERROR_INSTANTIATE = "[ERROR] 상태 인스턴스 생성에 실패했습니다: ";
    private static final String ERROR_INVOKE = "[ERROR] @Action 메서드 실행에 실패했습니다: ";
    private static final String ERROR_INJECT = "[ERROR] Context 필드 주입에 실패했습니다: ";
    private static final String ERROR_INITIAL_STATE = "[ERROR] 초기 상태를 찾을 수 없습니다: ";
    private static final String ERROR_UNREGISTERED_STATE = "[ERROR] 등록되지 않은 상태로 전이를 시도했습니다: ";
    private static final String ERROR_INVALID_ACTION = "[ERROR] @Action 메서드는 파라미터가 없거나 StateContext 하나만 받아야 합니다: ";

    private final Map<Class<?>, Object> stateInstances;
    private final Map<Class<?>, List<Method>> actionMethodsCache;
    private final StateContext context;
    private Object currentState;

    public StateEngine(StateContext context, List<Class<?>> stateClasses, Class<?> initialStateClass) {
        this.context = context;
        this.stateInstances = instantiateStates(stateClasses);
        this.actionMethodsCache = cacheActionMethods(stateClasses);
        this.currentState = stateInstances.get(initialStateClass);
        validateInitialState(initialStateClass);
    }

    private Map<Class<?>, Object> instantiateStates(List<Class<?>> stateClasses) {
        Map<Class<?>, Object> instances = new HashMap<>();

        for (Class<?> stateClass : stateClasses) {
            Object instance = instantiate(stateClass);
            injectContext(instance);
            instances.put(stateClass, instance);
        }

        return instances;
    }

    private Object instantiate(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(ERROR_INSTANTIATE + clazz.getName(), e);
        }
    }

    private void injectContext(Object stateInstance) {
        for (Field field : stateInstance.getClass().getDeclaredFields()) {
            if (isContextField(field)) {
                injectField(field, stateInstance);
            }
        }
    }

    private boolean isContextField(Field field) {
        return StateContext.class.isAssignableFrom(field.getType());
    }

    private void injectField(Field field, Object stateInstance) {
        try {
            field.setAccessible(true);
            field.set(stateInstance, context);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(ERROR_INJECT + field.getName(), e);
        }
    }

    private Map<Class<?>, List<Method>> cacheActionMethods(List<Class<?>> stateClasses) {
        Map<Class<?>, List<Method>> cache = new HashMap<>();

        for (Class<?> stateClass : stateClasses) {
            List<Method> methods = findActionMethods(stateClass);
            cache.put(stateClass, methods);
        }

        return cache;
    }

    private List<Method> findActionMethods(Class<?> stateClass) {
        return Arrays.stream(stateClass.getDeclaredMethods())
                .filter(this::isActionMethod)
                .sorted(Comparator.comparingInt(this::getOrderValue))
                .toList();
    }

    private boolean isActionMethod(Method method) {
        return method.isAnnotationPresent(Action.class);
    }

    private int getOrderValue(Method method) {
        Order order = method.getAnnotation(Order.class);
        if (order == null) {
            return Integer.MAX_VALUE;
        }
        return order.value();
    }

    private void validateInitialState(Class<?> initialStateClass) {
        if (currentState == null) {
            throw new IllegalStateException(ERROR_INITIAL_STATE + initialStateClass.getName());
        }
    }

    public void run() {
        while (!context.isFinished() && currentState != null) {
            update();
        }
    }

    public void update() {
        invokeActionMethods();
        handleTransition();
    }

    private void invokeActionMethods() {
        List<Method> actionMethods = actionMethodsCache.get(currentState.getClass());

        for (Method method : actionMethods) {
            invokeMethod(method);
            if (shouldStopExecution()) {
                break;
            }
        }
    }

    private void invokeMethod(Method method) {
        try {
            method.setAccessible(true);
            invokeWithParameters(method);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(ERROR_INVOKE + method.getName(), e);
        }
    }

    private void invokeWithParameters(Method method) throws ReflectiveOperationException {
        Class<?>[] paramTypes = method.getParameterTypes();

        if (hasNoParameters(paramTypes)) {
            method.invoke(currentState);
            return;
        }
        if (hasContextParameter(paramTypes)) {
            method.invoke(currentState, context);
            return;
        }
        throw new IllegalStateException(ERROR_INVALID_ACTION + method.getName());
    }

    private boolean hasNoParameters(Class<?>[] paramTypes) {
        return paramTypes.length == 0;
    }

    private boolean hasContextParameter(Class<?>[] paramTypes) {
        return paramTypes.length == 1 && StateContext.class.isAssignableFrom(paramTypes[0]);
    }

    private boolean shouldStopExecution() {
        return context.hasPendingTransition() || context.isFinished();
    }

    private void handleTransition() {
        if (!context.hasPendingTransition()) {
            return;
        }

        Class<?> nextStateClass = context.consumePendingTransition();
        if (nextStateClass == null) {
            currentState = null;
            return;
        }

        transitionToState(nextStateClass);
    }

    private void transitionToState(Class<?> nextStateClass) {
        Object nextState = stateInstances.get(nextStateClass);
        if (nextState == null) {
            throw new IllegalStateException(ERROR_UNREGISTERED_STATE + nextStateClass.getName());
        }
        currentState = nextState;
    }
}
