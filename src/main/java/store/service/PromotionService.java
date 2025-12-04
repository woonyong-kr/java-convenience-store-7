package store.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import store.convert.parser.PromotionTextParser;
import store.domain.product.Promotion;
import store.support.io.ResourceLoader;
import store.support.service.Service;

public class PromotionService extends Service {

    private static final String PROMOTIONS_FILE_PATH = "/promotions.md";

    private final List<Promotion> promotions;

    public PromotionService() {
        this.promotions = ResourceLoader.read(PROMOTIONS_FILE_PATH, new PromotionTextParser());
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
