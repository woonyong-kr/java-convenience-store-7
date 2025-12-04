package store.support.state.runtime;

public interface StateContext {

    void transitionTo(Class<?> nextState);

    void finish();

    boolean hasPendingTransition();

    Class<?> consumePendingTransition();

    boolean isFinished();
}
