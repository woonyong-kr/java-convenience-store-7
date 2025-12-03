package store.domain.order;

public class Order {
    private static final String ERROR_QUANTITY_EXCEEDED = "[ERROR] 수량을 초과하여 감소할 수 없습니다.";

    private final String name;
    private int quantity;

    public Order(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void reduceQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException(ERROR_QUANTITY_EXCEEDED);
        }
        this.quantity -= quantity;
    }
}
