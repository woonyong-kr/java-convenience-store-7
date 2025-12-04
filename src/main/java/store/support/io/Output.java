package store.support.io;

import store.support.convert.Mapper;

public class Output {

    private Output() {
    }

    public static void print(String message) {
        System.out.print(message);
    }

    public static void printLine(String message) {
        System.out.println(message);
    }

    public static void printLine() {
        System.out.println();
    }

    public static <T> void print(T value, Mapper<T> mapper) {
        System.out.print(mapper.map(value));
    }

    public static <T> void printLine(T value, Mapper<T> mapper) {
        System.out.println(mapper.map(value));
    }
}
