package store.support.text.parser;

public class YesNoParser extends TextParser<Boolean> {
    private static final String YES = "Y";

    @Override
    public Boolean parse(String text) {
        return text.equalsIgnoreCase(YES);
    }
}
