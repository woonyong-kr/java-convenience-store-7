package store.support.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class FileLoader implements Loader {

    private static final String LINE_DELIMITER = "\n";
    private static final String ERROR_NOT_FOUND = "[ERROR] 파일을 찾을 수 없습니다: ";
    private static final String ERROR_READ_FAILED = "[ERROR] 파일 읽기에 실패했습니다: ";

    @Override
    public String load(String filePath) {
        Path path = Path.of(filePath);
        validateExists(path);

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return reader.lines().collect(Collectors.joining(LINE_DELIMITER));
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_READ_FAILED + filePath, e);
        }
    }

    private void validateExists(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(ERROR_NOT_FOUND + path);
        }
    }
}
