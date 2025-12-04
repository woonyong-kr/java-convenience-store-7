package store.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import store.domain.product.Promotion;
import store.io.ResourceFileLoader;
import store.convert.parser.PromotionTextParser;

public class PromotionService {
    private static final String PROMOTIONS_FILE_PATH = "/promotions.md";

    private final List<Promotion> promotions;

    public PromotionService(ResourceFileLoader resourceFileLoader) {
        this.promotions = resourceFileLoader.load(PROMOTIONS_FILE_PATH, new PromotionTextParser());
    }

    public List<Promotion> getPromotions() {
        return Collections.unmodifiableList(promotions);
    }

    public Optional<Promotion> findByName(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(promotionName))
                .findFirst();
    }

}
