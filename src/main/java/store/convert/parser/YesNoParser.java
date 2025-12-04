package store.convert.parser;

import store.support.convert.Parser;

public class YesNoParser implements Parser<Boolean> {

    private static final String YES = "Y";

    @Override
    public Boolean parse(String text) {
        return text.equalsIgnoreCase(YES);
    }
}
