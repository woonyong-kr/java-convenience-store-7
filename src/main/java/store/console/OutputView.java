package store.console;

import store.support.text.mapper.TextMapper;

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