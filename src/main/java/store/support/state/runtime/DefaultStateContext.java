package store.support.state.runtime;

public class DefaultStateContext implements StateContext {

    private Class<?> pendingTransition;
    private boolean finished;

    public DefaultStateContext() {
        this.pendingTransition = null;
        this.finished = false;
    }

    @Override
    public void transitionTo(Class<?> nextState) {
        this.pendingTransition = nextState;
    }

    @Override
    public void finish() {
        this.finished = true;
        this.pendingTransition = null;
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
}
