/*This is class allowing retrieving data from users*/
package org.example.service.utils;

import org.example.exception.AppException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public final class UserDataService {

    private UserDataService() {}

    private final static Scanner scanner = new Scanner(System.in);
    /*Method retrieving String value*/
    public static String getString(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
    /*Method retrieving int value*/
    public static int getInt(String message) {
        System.out.println(message);

        String valueAsString = scanner.nextLine();
        if (!valueAsString.matches("\\d+")) {
            throw new AppException("int value is not correct");
        }

        return Integer.parseInt(valueAsString);
    }
    /*Method retrieving double value*/
    public static double getDouble(String message) {
        System.out.println(message);

        String valueAsString = scanner.nextLine();
        if (!valueAsString.matches("\\d+\\.*\\d+")) {
            throw new AppException("double value is not correct");
        }

        return Double.parseDouble(valueAsString);
    }
    /*Method retrieving BigDecimal value*/
    public static BigDecimal getBigDecimal(String message) {
        System.out.println(message);

        String valueAsString = scanner.nextLine();
        if (!valueAsString.matches("\\d+\\.*\\d+")) {
            throw new AppException("Big Decimal value is not correct");
        }

        return BigDecimal.valueOf(Long.parseLong(valueAsString));
    }
    /*Method retrieving date value*/
    public static LocalDate getDate(String message) {
        try {
            System.out.println(message);
            String value = scanner.nextLine();
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("d-MM-yyyy"));
        } catch (Exception e) {
            throw new AppException("date format is not correct: " + e.getMessage());
        }
    }

    public static void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
