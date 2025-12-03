package store.support.validation;

import java.util.regex.Pattern;

public class YesNoValidator implements Validator<String> {
    private static final String ERROR_INVALID_FORMAT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final Pattern PATTERN = Pattern.compile("^[yn]$", Pattern.CASE_INSENSITIVE);

    public YesNoValidator() {
    }

    @Override
    public void validate(String input) {
        validateNotEmpty(input);
        validateFormat(input);
    }

    private void validateNotEmpty(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(ERROR_INVALID_FORMAT);
        }
    }

    private void validateFormat(String input) {
        if (!PATTERN.matcher(input).matches()) {
            throw new IllegalArgumentException(ERROR_INVALID_FORMAT);
        }
    }
}
