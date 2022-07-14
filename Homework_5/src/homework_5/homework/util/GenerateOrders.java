package homework_5.homework.util;

import com.google.gson.Gson;
import homework_5.homework.domain.Customer;
import homework_5.homework.domain.Item;
import homework_5.homework.domain.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GenerateOrders {
    private static final Random r = new Random();
    private static List<String> domains;
    private static List<Item> items;
    private static List<Customer> customers;

    static {
        List<String> names = readStrings(Paths.get("data", "raw", "list-nomen"));
        domains = readStrings(Paths.get("data", "raw", "list-domains"));
        items = readItems(Paths.get("data", "raw", "list-foods"));
        customers = names.stream().map(GenerateOrders::makeCustomer).collect(Collectors.toList());
    }

    private GenerateOrders() {
    }

    private static List<String> readStrings(Path filePath) {
        try (var lines = Files.lines(filePath)) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static Customer makeCustomer(String name) {
        return new Customer(name, randomFrom(domains));
    }

    private static List<Item> readItems(Path filePath) {
        try (var lines = Files.lines(filePath)) {
            return lines.map(GenerateOrders::toItem)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static void makeAndSave(Path filePath, int amount) {
        var orders = makeRandomOrders(amount);
        var json = new Gson().toJson(orders);
        try {
            Files.writeString(filePath, json, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Order> makeRandomOrders(int amount) {
        return Stream.generate(GenerateOrders::newOrder).limit(amount).collect(Collectors.toList());
    }

    private static Order newOrder() {
        var customer = randomFrom(customers);
        var orderedItems = Stream.generate(GenerateOrders::randomItem)
                .distinct()
                .limit(r.nextInt(5) + 1)
                .collect(Collectors.toList());
        return new Order(customer, orderedItems, r.nextInt(1000) < 200);
    }

    private static Item randomItem() {
        return new Item(randomFrom(items), r.nextInt(3) + 1);
    }

    private static <T> T randomFrom(List<T> list) {
        return list.get(r.nextInt(list.size()));
    }

    private static Item toItem(String string) {
        // type; name; min-price; max-price
        var parts = string.split(";");
        var minPrice = Double.parseDouble(parts[2]);
        var maxPrice = Double.parseDouble(parts[3]);
        var delta = maxPrice - minPrice;
        var temp = r.nextDouble() * delta + minPrice;
        var price = Math.round(temp * 100) / 100.d;
        return new Item(parts[1].trim(), parts[0].trim(), price);
    }
}
