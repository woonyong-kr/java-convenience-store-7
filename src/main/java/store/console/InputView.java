package store.console;

import camp.nextstep.edu.missionutils.Console;
import store.convert.parser.TextParser;
import store.support.convert.Validator;

public class InputView {

    public InputView() {

    }

    public String readLine() {
        return Console.readLine();
    }

    public String readLine(Validator<String> validator) {
        String input = readLine();
        validator.accept(input);
        return input;
    }

    public <T> T readLine(TextParser<T> mapper) {
        String input = readLine();
        return mapper.apply(input);
    }

    public <T> T readLine(Validator<String> validator, TextParser<T> mapper) {
        String input = readLine();
        validator.accept(input);
        return mapper.apply(input);
    }
}
