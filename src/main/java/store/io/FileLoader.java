package store.io;

@FunctionalInterface
public interface FileLoader<T> {
    T load(String filePath);
}