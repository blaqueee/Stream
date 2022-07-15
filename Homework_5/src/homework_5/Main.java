package homework_5;

import homework_5.homework.RestaurantOrders;

import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

// используя статические imports
// мы импортируем *всё* из Collectors и Comparator.
// теперь нам доступны такие операции как
// toList(), toSet() и прочие, без указания уточняющего слова Collectors. или Comparator.
// вот так было до импорта Collectors.toList(), теперь стало просто toList()


import homework_5.homework.domain.Customer;
import homework_5.homework.domain.Item;
import homework_5.homework.domain.Order;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        var orders = RestaurantOrders.read("orders_100.json").getOrders();
        doAction(orders);

    }


    private static List<Order> getOrdersWithMaxPrice(List<Order> orders) {
        return orders.stream()
                .sorted(Collections.reverseOrder(comparing(Order::getTotal)))
                .limit(15)
                .collect(toList());
    }

    private static List<Order> getOrdersWithMinPrice(List<Order> orders) {
        return orders.stream()
                .sorted(comparing(Order::getTotal))
                .limit(15)
                .collect(toList());
    }

    private static List<Order> getOrdersToHome(List<Order> orders) {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .collect(toList());
    }

    private static Order getMaxOrderToHome(List<Order> orders) {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .max(comparingDouble(Order::getTotal)).get();
    }

    private static Order getMinOrderToHome(List<Order> orders) {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .min(comparingDouble(Order::getTotal)).get();
    }

    private static List<Order> getOrdersInRange(List<Order> orders, double minOrderTotal, double maxOrderTotal) {
        return orders.stream()
                .sorted(comparing(Order::getTotal))
                .dropWhile(e -> e.getTotal() <= minOrderTotal)
                .takeWhile(e -> e.getTotal() < maxOrderTotal)
                .collect(toList());
    }

    private static double getSumOfOrders(List<Order> orders) {
        return orders.stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    private static List<String> getUniqueEmails(List<Order> orders) {
        return new ArrayList<>(orders.stream()
                .map(e -> e.getCustomer().getEmail())
                .collect(toCollection(TreeSet::new)));
    }

    private static Map<Customer, List<Order>> getOrdersOfCustomers(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getCustomer));
    }

    private static Map<Customer, Double> getSumOfOrdersOfCustomer(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getCustomer,
                        summingDouble(Order::getTotal)));
    }

    private static Map<Customer, Double> getMaxSumOfOrders(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getCustomer,
                        summingDouble(Order::getTotal)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Map<Customer, Double> getMinSumOfOrders(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getCustomer,
                        summingDouble(Order::getTotal)))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Map<Integer, List<Item>> getAmountOfSoldItems(List<Order> orders) {
        return orders.stream()
                .map(Order::getItems)
                .flatMap(Collection::stream)
                .collect(groupingBy(item -> item,
                        summingInt(Item::getAmount)))
                .entrySet().stream()
                .collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, toList())));
    }

    private static Map<String, TreeSet<String>> getEmailsByItems(List<Order> orders) {
        var list = orders.stream()
                .collect(
                        groupingBy(order -> order.getCustomer().getEmail(),
                            flatMapping(order -> order.getItems().stream()
                                .map(Item::getName), toList()))
                ).entrySet().stream()
                .collect(
                        groupingBy(entryList -> entryList.getValue().stream().findAny().get(),
                                mapping(Map.Entry::getKey, toCollection(TreeSet::new))));
        return list;
    }

    private static void doAction(List<Order> orders) {
        while (true) {
            switch (askAction()) {
                case 1:
                    orders.stream()
                            .sorted(comparing(order -> order.getCustomer().getFullName()))
                            .forEach(Order::printOrderInfo);
                    break;
                case 2:
                    var list = getOrdersWithMaxPrice(orders);
                    list.forEach(Order::printOrderInfo);
                    break;
                case 3:
                    list = getOrdersWithMinPrice(orders);
                    list.forEach(Order::printOrderInfo);
                    break;
                case 4:
                    list = getOrdersToHome(orders);
                    list.forEach(Order::printOrderInfo);
                    break;
                case 5:
                    var order = getMaxOrderToHome(orders);
                    order.printOrderInfo();
                    break;
                case 6:
                    order = getMinOrderToHome(orders);
                    order.printOrderInfo();
                    break;
                case 7:
                    list = getOrdersInRange(orders, 70, 120);
                    list.forEach(Order::printOrderInfo);
                    break;
                case 8:
                    var total = getSumOfOrders(orders);
                    System.out.println("+----------+----------+");
                    System.out.printf("| TOTAL    | %-8.2f |%n", total);
                    System.out.println("+----------+----------+");
                    break;
                case 9:
                    var addresses = getUniqueEmails(orders);

                    System.out.println("+-----+-----------------------------------+");
                    for (int i = 0; i < addresses.size(); i++) {
                        System.out.printf("| %-3d | %-33s |%n", i + 1, addresses.get(i));
                        System.out.println("+-----+-----------------------------------+");
                    }
                    break;
                case 10:
                    var map = getOrdersOfCustomers(orders);
                    map.forEach((k, v) -> {
                        k.printCustomerInfo();
                        v.forEach(Order::printOrder);
                        System.out.println();
                    });
                    break;
                case 11:
                    var mapForDouble = getSumOfOrdersOfCustomer(orders);
                    mapForDouble.forEach((k, v) -> {
                        k.printCustomerInfo();
                        System.out.println("+-------------------------+---------+");
                        System.out.printf("| TOTAL                   | %-7.2f |%n", v);
                        System.out.println("+-------------------------+---------+");
                    });
                    break;
                case 12:
                    mapForDouble = getMaxSumOfOrders(orders);
                    mapForDouble.forEach((k, v) -> {
                        k.printCustomerInfo();
                        System.out.println("+-------------------------+---------+");
                        System.out.printf("| TOTAL                   | %-7.2f |%n", v);
                        System.out.println("+-------------------------+---------+");
                    });
                    break;
                case 13:
                    mapForDouble = getMinSumOfOrders(orders);
                    mapForDouble.forEach((k, v) -> {
                        k.printCustomerInfo();
                        System.out.println("+-------------------------+---------+");
                        System.out.printf("| TOTAL                   | %-7.2f |%n", v);
                        System.out.println("+-------------------------+---------+");
                    });
                    break;
                case 14:
                    var mapForAmounts = new TreeMap<>(getAmountOfSoldItems(orders));
                    System.out.println("+-----+-------------------------+");
                    mapForAmounts.forEach(
                            (k, v) -> {
                                System.out.printf("| %-3d ", k);
                                for (int i = 0; i < v.size(); i++) {
                                    System.out.printf("| %23s |%n", v.get(i).getName());
                                    if (i != v.size() - 1)
                                        System.out.print("|     ");
                                }
                                System.out.println("+-----+-------------------------+");
                            }
                    );
                    break;
                case 15:
                    var treeMap = new TreeMap<>(getEmailsByItems(orders));
                    System.out.println("+----------------------+----------------------------------+");
                    treeMap.forEach((k, v) -> {
                        System.out.printf("| %-20s ", k);
                        ArrayList<String> arrList = new ArrayList<>(v);
                        for (int i = 0; i < v.size(); i++) {
                            System.out.printf("| %32s |%n", arrList.get(i));
                            if (i != v.size() - 1)
                                System.out.print("|                      ");
                        }
                        System.out.println("+----------------------+----------------------------------+");
                    });
                    break;
                case 16:
                    return;
            }
        }
    }

    private static int askAction() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            printActions();
            try {
                String choice = sc.nextLine().replaceAll("\\s+", "");
                return checkAction(choice);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int checkAction(String action) {
        if (action.equals(""))
            throw new RuntimeException("Choice can't be empty!");
        int choice = Integer.parseInt(action);
        if (choice < 1 || choice > 16)
            throw new RuntimeException("Can't find this action!");
        return choice;
    }

    private static void printActions() {
        System.out.println("""
                1  -> Print all orders
                2  -> Get 15 orders with MAX total
                3  -> Get 15 orders with MIN total
                4  -> Get orders with delivery to home
                5  -> Get home delivery order with MAX total
                6  -> Get home delivery order with MIN total
                7  -> Get orders in certain range (70 - 120):
                8  -> Get total SUM of all orders
                9  -> Get all email addresses
                10 -> Get orders grouped by customers
                11 -> Get SUM of orders grouped by customers
                12 -> Get MAX total of one customer
                13 -> Get MIN total of one customer
                14 -> Get amounts of sold items
                15 -> Get emails by items (BONUS)
                16 -> Exit program
                """);
        System.out.print("Choose action: ");
    }
}