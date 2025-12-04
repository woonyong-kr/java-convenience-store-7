package store.console;

import store.support.convert.Mapper;

public class OutputView {

    public OutputView() {
    }

    public void printLine(String message) {
        System.out.println(message);
    }

    public <T> void printLine(T value, Mapper<T> mapper) {
        System.out.println(mapper.map(value));
    }
}