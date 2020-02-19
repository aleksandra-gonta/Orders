package org.example.service;

import org.eclipse.collections.impl.collector.Collectors2;
import org.example.converter.CustomersJsonConverter;
import org.example.converter.OrdersStoreJsonConverter;
import org.example.exception.AppException;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.service.utils.EmailService;
import org.example.validator.OrderValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class OrdersService {
    private final Set<Order> orders;

    public OrdersService() {
        this.orders = new HashSet<>();
    }

    public OrdersService(String filename) {
        this.orders = parseAndValidateOrders(filename);
    }


    private static Set<Order> parseAndValidateOrders(String jsonFilename) {

        if (jsonFilename == null) {
            throw new AppException("json filename string is null");
        }

        AtomicInteger counter = new AtomicInteger(1);
        OrderValidator orderValidator = new OrderValidator();


        return new OrdersStoreJsonConverter(jsonFilename)
                .fromJson()
                .orElseThrow(() -> new AppException("cannot parse data from json file " + jsonFilename))
                .stream()
                .filter(order -> {
                    var errors = orderValidator.validate(order);
                    if (orderValidator.hasErrors()) {
                        System.out.println("Validation errors for order no. " + counter.get() + ":");
                        errors.forEach((field, message) -> System.out.println(field + ": " + message));
                        System.out.println("-----------------------------------------------------");
                    }
                    counter.incrementAndGet();
                    return !orderValidator.hasErrors();
                }).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Orders:" + orders;
    }

    public BigDecimal countProductsAveragePriceWithinPeriod(LocalDate start, LocalDate end) {
        return orders
                .stream()
                .filter(product -> product.orderDate.isAfter(start) && product.orderDate.isBefore(end)
                        || product.orderDate.equals(start) || product.orderDate.equals(end))
                .collect(Collectors2.summarizingBigDecimal(e -> (e.getProduct().getPrice()))).getAverage();


    }

    public Map<Category, Product> findMostExpensiveProductInCategory() {
        return orders
                .stream()
                .map(Order::getProduct)
                .collect(Collectors.groupingBy(Product::getCategory,
                        collectingAndThen(Collectors.maxBy(Comparator.comparing(Product::getPrice)), Optional::orElseThrow)));
    }

    public void sendCustomerOrdersList() {

        Map<Customer, List<Product>> shoppingList = orders
                .stream()
                .collect(Collectors.groupingBy(Order::getCustomer))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Order::getProduct).collect(toList())

                ));

        EmailService emailService = new EmailService();

        shoppingList.forEach((key, value) -> emailService.send(key.getEmail(), "SHOPPING LIST", "<h1> Your order: </h1>" + "" + value.toString()));
    }


    public LocalDate findDateOfMaxOrders() {

        return orders
                .stream()
                .collect(Collectors.groupingBy(Order::getOrderDate, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .get();
    }

    public LocalDate findDateOfMinOrders() {
        return orders
                .stream()
                .collect(Collectors.groupingBy(Order::getOrderDate, Collectors.counting()))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .get();


    }

    public Customer findCustomerWhoPaidMost() {

        return orders
                .stream()
                .collect(Collectors.groupingBy(Order::getCustomer))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue()
                                .stream()
                                .map(el -> el.getProduct().getPrice().multiply(BigDecimal.valueOf(el.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .get();


    }

    public Category findMostPopularProductCategory() {
        return orders
                .stream()
                .map(Order::getProduct)
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .get();

    }

    public Map<Month, Category> showPopularProductCategoryOfMonths() {
        return orders
                .stream()
                .collect(Collectors.groupingBy(Order::getOrderDate))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getMonth(),
                        e -> e.getValue().stream().map(Order::getProduct).max(Comparator.comparing(Product::getCategory)).get()
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        elem -> elem.getValue().getCategory()
                ));

    }

    public Map<Month, Integer> showProductsQuantityOfMonths() {
        return orders
                .stream()
                .collect(Collectors.groupingBy(Order::getOrderDate))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getMonth(),
                        e -> e.getValue().stream().collect(Collectors.summingInt(Order::getQuantity))
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    public List<Customer> showCustomersWhoBoughtMinNumberOfItems(int minNumber) {
        List<Customer> customersMinItems = orders
                .stream()
                .filter(e -> e.getQuantity() >= minNumber)
                .map(Order::getCustomer)
                .distinct()
                .collect(Collectors.toList());

        final String CUSTOMERS_JSON_FILENAME = "customersBuyingMinNumberOfItems.json";
        CustomersJsonConverter customersJsonConverter = new CustomersJsonConverter(CUSTOMERS_JSON_FILENAME);
        customersJsonConverter.toJson(customersMinItems);

        return customersMinItems;
    }

    public BigDecimal countOrdersTotalPriceWithDiscounts() {
        BigDecimal discountOrders = orders
                .stream()
                .filter(e -> e.getCustomer().getAge() < 25 || e.getOrderDate().isAfter(LocalDate.now().minusDays(3)))
                .map(el -> el.getCustomer().getAge() < 25 ?
                        el.getProduct().getPrice().multiply(BigDecimal.valueOf(el.getQuantity()).multiply(BigDecimal.valueOf(0.97)))
                        : el.getProduct().getPrice().multiply(BigDecimal.valueOf(el.getQuantity())).multiply(BigDecimal.valueOf(0.98)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal nonDiscountOrders = orders
                .stream()
                .filter(e -> e.getCustomer().getAge() >= 25 && e.getOrderDate().isBefore(LocalDate.now().minusDays(3)))
                .map(el -> el.getProduct().getPrice().multiply(BigDecimal.valueOf(el.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println(discountOrders);
        System.out.println(nonDiscountOrders);

        return discountOrders.add(nonDiscountOrders);
    }


}

