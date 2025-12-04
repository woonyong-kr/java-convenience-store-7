package store.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import store.support.convert.Parser;
import store.support.io.Loader;

public class ResourceFileLoader implements Loader {

    private static final String LINE_DELIMITER = "\n";
    private static final String ERROR_RESOURCE_NOT_FOUND = "[ERROR] 리소스 파일을 찾을 수 없습니다: ";
    private static final String ERROR_RESOURCE_READ_FAILED = "[ERROR] 리소스 파일 읽기에 실패했습니다: ";

    @Override
    public String load(String resourcePath) {
        try (InputStream input = getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(LINE_DELIMITER));
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_RESOURCE_READ_FAILED + resourcePath, e);
        }
    }

    @Override
    public <T> T load(String resourcePath, Parser<T> parser) {
        return parser.parse(load(resourcePath));
    }

    private InputStream getResourceAsStream(String resourcePath) {
        InputStream input = ResourceFileLoader.class.getResourceAsStream(resourcePath);
        validateNotNull(input, resourcePath);
        return input;
    }

    private void validateNotNull(InputStream input, String resourcePath) {
        if (input == null) {
            throw new IllegalArgumentException(ERROR_RESOURCE_NOT_FOUND + resourcePath);
        }
    }
}
