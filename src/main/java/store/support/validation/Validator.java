package store.support.validation;

import java.util.function.Consumer;

@FunctionalInterface
public interface Validator<T> extends Consumer<T> {

    void validate(T value);

    @Override
    default void accept(T value) {
        validate(value);
    }
}
