package store.service;

import java.util.List;
import store.domain.product.Product;
import store.io.ResourceFileLoader;
import store.support.text.ProductTextParser;
import store.support.text.TextMapper;

public class ProductService {
    private static final String PRODUCTS_FILE_PATH = "/products.md";

    private final ResourceFileLoader  resourceFileLoader;

    public ProductService(ResourceFileLoader resourceFileLoader) {
        this.resourceFileLoader = resourceFileLoader;
    }

    public void loadProduct() {
        ProductTextParser parser = new ProductTextParser();
        List<Product> Products = resourceFileLoader.readLines(PRODUCTS_FILE_PATH, parser);
    }
}
