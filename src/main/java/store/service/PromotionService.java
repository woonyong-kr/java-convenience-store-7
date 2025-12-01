package store.service;

import java.util.List;
import store.io.ResourceFileLoader;

public class PromotionService {
    private static final String PROMOTIONS_FILE_PATH = "/promotions.md";

    private ResourceFileLoader resourceFileLoader;

    public PromotionService(ResourceFileLoader resourceFileLoader) {
        this.resourceFileLoader = resourceFileLoader;
    }

    public void loadPromotions() {
        List<String> lines = resourceFileLoader.readLines(PROMOTIONS_FILE_PATH);
        lines.stream()
                .skip(1)
                .forEach(line -> {});
    }
}
