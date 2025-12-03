package store.service;

import java.util.Collections;
import java.util.List;
import store.domain.product.Product;
import store.io.ResourceFileLoader;
import store.support.text.parser.ProductTextParser;

public class ProductService {
    private static final String PRODUCTS_FILE_PATH = "/products.md";
    private static final String ERROR_PRODUCT_NOT_FOUND = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String ERROR_STOCK_EXCEEDED = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final List<Product> products;

    public ProductService(ResourceFileLoader resourceFileLoader) {
        products = resourceFileLoader.load(PRODUCTS_FILE_PATH, new ProductTextParser());
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Product findByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ERROR_PRODUCT_NOT_FOUND));
    }

    public void checkOrder(String name, int quantity) {
        if (findByName(name).getTotalStock() < quantity) {
            throw new IllegalArgumentException(ERROR_STOCK_EXCEEDED);
        }
    }
}
