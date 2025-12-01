package store.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import store.support.text.TextParser;

public class ResourceFileLoader {
    private static final String ERROR_RESOURCE_NOT_FOUND = "[ERROR] 리소스 파일을 찾을 수 없습니다: ";
    private static final String ERROR_RESOURCE_READ_FAILED = "[ERROR] 리소스 파일을 읽는 중 오류가 발생했습니다: ";

    public List<String> readLines(String resourcePath) {
        try (InputStream input = getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_RESOURCE_READ_FAILED + resourcePath, e);
        }
    }

    public <T> List<T> readLines(String resourcePath, TextParser<T> parser) {
        return readLines(resourcePath).stream()
                .skip(1)
                .map(parser)
                .toList();
    }

    private InputStream getResourceAsStream(String resourcePath) {
        InputStream input = ResourceFileLoader.class.getResourceAsStream(resourcePath);
        validateNotNull(input, ERROR_RESOURCE_NOT_FOUND + resourcePath);
        return input;
    }

    private void validateNotNull(InputStream input, String error) {
        if (input == null) {
            throw new IllegalArgumentException(error);
        }
    }
}
