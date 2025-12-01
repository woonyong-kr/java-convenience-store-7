package store.support.text;

import java.util.function.Function;

@FunctionalInterface
public interface TextMapper<S, T> extends Function<S, T> {

    T map(S source);

    @Override
    default T apply(S source) {
        return map(source);
    }
}