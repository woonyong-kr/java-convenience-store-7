package store.support.text.parser;

import java.util.function.Function;

@FunctionalInterface
public interface TextParser<T> extends Function<String, T> {

    T parse(String text);

    @Override
    default T apply(String text) {
        return parse(text);
    }
}