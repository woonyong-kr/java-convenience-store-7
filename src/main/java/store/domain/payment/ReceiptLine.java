package store.domain.payment;

public class ReceiptLine {
    private final String name;
    private final int quantity;
    private final int price;

    public ReceiptLine(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
