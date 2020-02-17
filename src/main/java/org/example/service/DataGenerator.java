package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.converter.OrdersStoreJsonConverter;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataGenerator {
    private List<Customer> customers;
    private List<Product> products;


    public void generateData() {
        List<Order> orders = List.of(
                Order.builder()
                        .customer(getRandomCustomer())
                        .product(getRandomProduct())
                        .quantity(getRandomQuantity())
                        .orderDate(getRandomDate())
                        .build(),
                Order.builder()
                        .customer(getRandomCustomer())
                        .product(getRandomProduct())
                        .quantity(getRandomQuantity())
                        .orderDate(getRandomDate())
                        .build(),
                Order.builder()
                        .customer(getRandomCustomer())
                        .product(getRandomProduct())
                        .quantity(getRandomQuantity())
                        .orderDate(getRandomDate())
                        .build(),
                Order.builder()
                        .customer(getRandomCustomer())
                        .product(getRandomProduct())
                        .quantity(getRandomQuantity())
                        .orderDate(getRandomDate())
                        .build(),
                Order.builder()
                        .customer(getRandomCustomer())
                        .product(getRandomProduct())
                        .quantity(getRandomQuantity())
                        .orderDate(getRandomDate())
                        .build());

        final String ORDERS_STORE_JSON_FILENAME = "sample.json";
        OrdersStoreJsonConverter ordersStoreJsonConverter = new OrdersStoreJsonConverter(ORDERS_STORE_JSON_FILENAME);
        ordersStoreJsonConverter.toJson(orders);
    }


    private Customer getRandomCustomer() {

        Random rand = new Random();
        return customers.get(rand.nextInt(customers.size()));
    }

    private Product getRandomProduct() {

        Random rand = new Random();
        return products.get(rand.nextInt(products.size()));
    }

    private int getRandomQuantity() {
        Random rand = new Random();
        return rand.nextInt(11) + 1;
    }

    private LocalDate getRandomDate() {

        LocalDate end = LocalDate.now().plusMonths(2);
        long days = ChronoUnit.DAYS.between(LocalDate.now(), end);
       return end.minusDays(new Random().nextInt((int) days + 1));

    }
}

