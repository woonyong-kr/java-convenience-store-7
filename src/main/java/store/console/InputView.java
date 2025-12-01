package store.console;

import camp.nextstep.edu.missionutils.Console;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public String readLine(String prompt, Consumer<String> validator) {
        String input = readLine(prompt);
        validator.accept(input);
        return input;
    }

    public <T> T readValue(String prompt, Function<String, T> mapper) {
        String input = readLine(prompt);
        return mapper.apply(input);
    }
}
