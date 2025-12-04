package store.support.state.runtime;

import java.lang.reflect.Constructor;
import java.util.List;

public class StateRunner {

    private static final String ERROR_CONTEXT_INSTANTIATE = "[ERROR] Context 인스턴스 생성에 실패했습니다: ";

    private StateRunner() {
    }

    public static void run(String basePackage) {
        StateContext context = createContext(basePackage);
        runWithContext(basePackage, context);
    }

    private static StateContext createContext(String basePackage) {
        Class<? extends StateContext> contextClass = StateScanner.findContextClass(basePackage);
        if (contextClass == null) {
            return new DefaultStateContext();
        }
        return instantiateContext(contextClass);
    }

    private static StateContext instantiateContext(Class<? extends StateContext> contextClass) {
        try {
            Constructor<? extends StateContext> constructor = contextClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(ERROR_CONTEXT_INSTANTIATE + contextClass.getName(), e);
        }
    }

    private static void runWithContext(String basePackage, StateContext context) {
        List<Class<?>> stateClasses = StateScanner.scanStateClasses(basePackage);
        Class<?> initialState = StateScanner.findInitialState(basePackage);
        StateEngine engine = new StateEngine(context, stateClasses, initialState);
        engine.run();
    }

    public static void run(String basePackage, StateContext context) {
        runWithContext(basePackage, context);
    }
}
