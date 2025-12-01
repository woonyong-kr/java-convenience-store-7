package store.console;

import java.util.function.Function;
import store.support.text.TextMapper;

public class OutputView {

    public  OutputView(){

    }

    public void printLine(String message) {
        System.out.println(message);
    }


    public <T> void printLine(T value, TextMapper<T, String> formatter) {
        System.out.println(formatter.apply(value));
    }
}