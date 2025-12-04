package store.service;

import java.util.List;
import store.domain.order.Order;
import store.domain.order.PurchaseResult;
import store.domain.payment.Receipt;
import store.domain.product.Product;
import store.domain.product.Promotion;
import store.domain.product.PromotionPolicy;
import store.support.service.Service;

public class PaymentService extends Service {

    public Receipt createReceipt(List<Order> orders, List<Product> products, List<Promotion> promotions, boolean useMembership) {
        List<PurchaseResult> results = orders.stream()
                .map(order -> createPurchaseResult(order, products, promotions))
                .toList();
        return new Receipt(results, useMembership);
    }

    private PurchaseResult createPurchaseResult(Order order, List<Product> products, List<Promotion> promotions) {
        Product product = findProduct(products, order.getName());
        Promotion promotion = findPromotion(promotions, product.getPromotion());
        PromotionPolicy policy = product.createPromotionPolicy(promotion);
        return PurchaseResult.of(order, product, policy);
    }

    private Product findProduct(List<Product> products, String name) {
        return products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    private Promotion findPromotion(List<Promotion> promotions, String name) {
        if (name == null) {
            return null;
        }
        return promotions.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
