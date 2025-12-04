package store.support.io;

import camp.nextstep.edu.missionutils.Console;
import store.support.convert.Parser;
import store.support.convert.Validator;

public class Input {

    private Input() {
    }

    public static String readLine() {
        return Console.readLine();
    }

    public static String readLine(Validator<String> validator) {
        String input = readLine();
        validator.validate(input);
        return input;
    }

    public static <T> T readLine(Parser<T> parser) {
        String input = readLine();
        return parser.parse(input);
    }

    public static <T> T readLine(Validator<String> validator, Parser<T> parser) {
        String input = readLine();
        validator.validate(input);
        return parser.parse(input);
    }
}
