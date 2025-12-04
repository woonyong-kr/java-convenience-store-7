package store.support.io;

import store.support.convert.Parser;

public interface Loader {

    String load(String path);

    default <T> T load(String path, Parser<T> parser) {
        return parser.parse(load(path));
    }
}
