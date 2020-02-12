package org.example.validator;

import org.example.model.Order;
import org.example.validator.generic.AbstractValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

public class OrderValidator extends AbstractValidator<Order> {
    private static final String PRODUCT_NAME_ERROR_MESSAGE = "product name should contain only upper case letters and spaces";
    private static final String CUSTOMER_ERROR_MESSAGE = "customer object cannot be null";
    private static final String PRODUCT_ERROR_MESSAGE = "product object cannot be null";
    private static final String PRICE_ERROR_MESSAGE = "price value should have positive value";
    private static final String QUANTITY_ERROR_MESSAGE = "quantity value should have positive value";
    private static final String DATE_ERROR_MESSAGE = "order date should be today or in the future";
    private static final String NAME_AND_SURNAME_ERROR_MESSAGE = "client name and surname should contain only upper case letters and spaces";
    private static final String AGE_ERROR_MESSAGE = "customer age should be a value higher or equaled to 18";
    private static final String EMAIL_ERROR_MESSAGE = "email address should have appropriate format";

    public OrderValidator() {
    }

    public Map<String, String> validate(Order order) {
        this.errors.clear();
        if (!isProductNameValid(order)) {
            this.errors.put("PRODUCT NAME", PRODUCT_NAME_ERROR_MESSAGE);
        }

        if (!isCustomerValid(order)) {
            this.errors.put("CUSTOMER", CUSTOMER_ERROR_MESSAGE);
        }

        if (!isProductValid(order)) {
            this.errors.put("PRODUCT", PRODUCT_ERROR_MESSAGE);
        }

        if (!isPriceValid(order)) {
            this.errors.put("PRICE", PRICE_ERROR_MESSAGE);
        }

        if (!isQuantityValid(order)) {
            this.errors.put("QUANTITY", QUANTITY_ERROR_MESSAGE);
        }

        if (!isDateValid(order)) {
            this.errors.put("DATE", DATE_ERROR_MESSAGE);
        }

        if (!isNameAndSurnameValid(order)) {
            this.errors.put("NAME&SURNAME", NAME_AND_SURNAME_ERROR_MESSAGE);
        }

        if (!isAgeValid(order)) {
            this.errors.put("AGE", AGE_ERROR_MESSAGE);
        }

        if (!isEmailValid(order)) {
            this.errors.put("EMAIL", EMAIL_ERROR_MESSAGE);
        }

        return this.errors;
    }

    private static boolean isProductNameValid(Order order) {
        return order.getProduct() != null && order.getProduct().getName().matches("[A-Z\\s]+");
    }

    private static boolean isCustomerValid(Order order) {
        return order.getCustomer() != null;
    }

    private static boolean isProductValid(Order order) {
        return order.getProduct() != null;
    }

    private static boolean isPriceValid(Order order) {
        return order.getProduct().getPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    private static boolean isQuantityValid(Order order) {
        return order.getQuantity() > 0;
    }

    private static boolean isDateValid(Order order) {
        return order.getOrderDate().isAfter(LocalDate.now().minus(Period.ofDays(1)));
    }

    private static boolean isNameAndSurnameValid(Order order) {
        return order.getCustomer().getName() != null && order.getCustomer().getSurname() != null && order.getCustomer().getName().matches("[A-Z\\s]+") && order.getCustomer().getSurname().matches("[A-Z\\s]+");
    }

    private static boolean isAgeValid(Order order) {
        return order.getCustomer().getAge() >= 18;
    }

    private static boolean isEmailValid(Order order) {
        return order.getCustomer().getEmail().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
}