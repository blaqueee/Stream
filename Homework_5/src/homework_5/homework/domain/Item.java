package homework_5.homework.domain;

import java.util.Objects;

public class Item {
    // Этот блок кода менять нельзя! НАЧАЛО!
    private final String name;
    private final double price;
    private final String type;
    private int amount = 0;

    public Item(String name, String type, double price) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public Item(Item item, int amount) {
        this.name = item.name;
        this.type = item.type;
        this.price = item.price;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(item.price, price) == 0 &&
                Objects.equals(name, item.name) &&
                Objects.equals(type, item.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, type);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
    // Этот блок кода менять нельзя! КОНЕЦ!

    public Item getItem() {
        return this;
    }

    public void printItem() {
        System.out.printf("| %-23s | %-13s | %-8s | %-8s |%n",
                name, type, price, amount);
        System.out.println("+-------------------------+---------------+----------+----------+");
    }
}
