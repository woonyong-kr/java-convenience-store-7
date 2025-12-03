package store.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import store.support.text.parser.TextParser;

public class ResourceFileLoader implements FileLoader<String> {
    private static final String ERROR_RESOURCE_NOT_FOUND = "[ERROR] 리소스 파일 없음: ";
    private static final String ERROR_RESOURCE_READ_FAILED = "[ERROR] 리소스 파일 읽기 실패: ";

    @Override
    public String load(String resourcePath) {
        try (InputStream input = getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_RESOURCE_READ_FAILED + resourcePath, e);
        }
    }

    public <T> T load(String resourcePath, TextParser<T> parser) {
        return parser.parse(load(resourcePath));
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
