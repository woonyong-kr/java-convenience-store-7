package store.support.convert;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@FunctionalInterface
public interface Mapper<T> extends Function<T, String> {

    String LINE_DELIMITER = "\n";
    String DATE_PATTERN = "yyyy-MM-dd";
    String NUMBER_PATTERN = "#,###";
    DecimalFormat NUMBER_FORMAT = new DecimalFormat(NUMBER_PATTERN);
    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN);

    String map(T source);

    @Override
    default String apply(T source) {
        return map(source);
    }

    static String number(int value) {
        return NUMBER_FORMAT.format(value);
    }

    static String number(long value) {
        return NUMBER_FORMAT.format(value);
    }

    static String date(LocalDate value) {
        return value.format(DATE_FORMAT);
    }

    static String join(String delimiter, String... values) {
        return String.join(delimiter, values);
    }

    static String joinLines(String... values) {
        return join(LINE_DELIMITER, values);
    }
}
