package store.console;

import camp.nextstep.edu.missionutils.Console;
import store.support.convert.Parser;
import store.support.convert.Validator;

public class InputView {

    public InputView() {
    }

    public String readLine() {
        return Console.readLine();
    }

    public String readLine(Validator<String> validator) {
        String input = readLine();
        validator.validate(input);
        return input;
    }

    public <T> T readLine(Parser<T> parser) {
        String input = readLine();
        return parser.parse(input);
    }

    public <T> T readLine(Validator<String> validator, Parser<T> parser) {
        String input = readLine();
        validator.validate(input);
        return parser.parse(input);
    }
}
