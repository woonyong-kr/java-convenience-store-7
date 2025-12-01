package store.console;

import java.util.function.Function;

public class OutputView {

    public  OutputView(){

    }

    public void printLine(String message) {
        System.out.println(message);
    }


    public <T> void printLine(T value, Function<T, String> formatter) {
        System.out.println(formatter.apply(value));
    }
}