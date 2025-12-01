package store.console;

import camp.nextstep.edu.missionutils.Console;
import java.util.function.Consumer;
import java.util.function.Function;
import store.support.text.TextParser;
import store.support.validation.Validator;

public class InputView {

    public InputView() {

    }

    public String readLine() {
        return Console.readLine();
    }

    public String readLine(String prompt) {
        System.out.println(prompt);
        return readLine();
    }

    public String readLine(String prompt, Validator<String> validator) {
        String input = readLine(prompt);
        validator.accept(input);
        return input;
    }

    public <T> T readValue(String prompt, TextParser<T> mapper) {
        String input = readLine(prompt);
        return mapper.apply(input);
    }
}
