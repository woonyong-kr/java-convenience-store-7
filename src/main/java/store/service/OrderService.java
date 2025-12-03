package store.service;

import java.util.Collections;
import java.util.List;
import store.domain.order.Order;

public class OrderService {
    private List<Order> currentOrder;

    public void registerOrder(List<Order> order) {
        this.currentOrder = order;
    }

    public List<Order> getCurrentOrder() {
        return Collections.unmodifiableList(currentOrder);
    }

    public void clearOrder() {
        this.currentOrder = null;
    }
}
