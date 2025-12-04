package store.validation;

import java.util.Arrays;
import java.util.regex.Pattern;
import store.support.convert.Validator;

public class InputPurchaseValidator implements Validator<String> {
    private static final String ERROR_INVALID_FORMAT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String DELIMITER = ",";
    private static final Pattern PATTERN = Pattern.compile("^\\[[가-힣a-zA-Z]+-\\d+\\]$");

    public InputPurchaseValidator() {
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
        Arrays.stream(input.split(DELIMITER))
                .map(String::trim)
                .forEach(this::validateItemFormat);
    }

    private void validateItemFormat(String item) {
        if (!PATTERN.matcher(item).matches()) {
            throw new IllegalArgumentException(ERROR_INVALID_FORMAT);
        }
    }
}
