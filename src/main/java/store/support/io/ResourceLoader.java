package store.support.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import store.support.convert.Parser;

public class ResourceLoader implements Loader {

    private static final ResourceLoader INSTANCE = new ResourceLoader();
    private static final String LINE_DELIMITER = "\n";
    private static final String ERROR_NOT_FOUND = "[ERROR] 리소스 파일을 찾을 수 없습니다: ";
    private static final String ERROR_READ_FAILED = "[ERROR] 리소스 파일 읽기에 실패했습니다: ";

    public static String read(String resourcePath) {
        return INSTANCE.load(resourcePath);
    }

    public static <T> T read(String resourcePath, Parser<T> parser) {
        return INSTANCE.load(resourcePath, parser);
    }

    @Override
    public String load(String resourcePath) {
        try (InputStream input = getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(LINE_DELIMITER));
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_READ_FAILED + resourcePath, e);
        }
    }

    private InputStream getResourceAsStream(String resourcePath) {
        InputStream input = ResourceLoader.class.getResourceAsStream(resourcePath);
        if (input == null) {
            throw new IllegalArgumentException(ERROR_NOT_FOUND + resourcePath);
        }
        return input;
    }
}
