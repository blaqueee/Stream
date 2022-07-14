package homework_5.homework.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Order {
    // Этот блок кода менять нельзя! НАЧАЛО!
    private final Customer customer;
    private final List<Item> items;
    private final boolean homeDelivery;
    private transient double total = 0.0d;

    public Order(Customer customer, List<Item> orderedItems, boolean homeDelivery) {
        this.customer = customer;
        this.items = List.copyOf(orderedItems);
        this.homeDelivery = homeDelivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return homeDelivery == order.homeDelivery &&
                Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, homeDelivery);
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isHomeDelivery() {
        return homeDelivery;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotal() {
        return total;
    }
    // Этот блок кода менять нельзя! КОНЕЦ!

    //----------------------------------------------------------------------
    //------   Реализация ваших методов должна быть ниже этой линии   ------
    //----------------------------------------------------------------------

    public void calculateTotal() {
        total = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getAmount())
                .sum();
    }

    public void printOrder() {
        System.out.println("+-------------------------+---------------+----------+----------+\n" +
                "|           NAME          |      TYPE     |   PRICE  |  AMOUNT  |\n" +
                "+-------------------------+---------------+----------+----------+");
        items.forEach(Item::printItem);
    }

    public void printOrderInfo() {
        customer.printCustomerInfo();
        printOrderWithTotal();
    }

    public void printOrderWithTotal() {
        Supplier<String> delivery = (() -> {
            if (isHomeDelivery()) return "YES";
            return "NO";
        });

        printOrder();
        System.out.println("+-------------------------+---------+");
        System.out.printf("| HOME DELIVERY           | %-7s |%n", delivery.get());
        System.out.println("+-------------------------+---------+");
        System.out.printf("| TOTAL                   | %-7.2f |%n", total);
        System.out.println("+-------------------------+---------+\n");
    }
}
