   /*This class provides console menu for car service*/
package org.example.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections.MapUtils;
import org.example.exception.AppException;
import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.service.OrdersService;
import org.example.service.utils.UserDataService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

public class MenuService {
    private final OrdersService ordersService;
    /*Method providing the chosen option*/
    public void mainMenu() {
        while (true) {
            try {
                int option = this.chooseOption();
                switch (option) {
                    case 1:
                        this.option1();
                        break;
                    case 2:
                        this.option2();
                        break;
                    case 3:
                        this.option3();
                        break;
                    case 4:
                        this.option4();
                        break;
                    case 5:
                        this.option5();
                        break;
                    case 6:
                        this.option6();
                        break;
                    case 7:
                        this.option7();
                        break;
                    case 8:
                        this.option8();
                        break;
                    case 9:
                        this.option9();
                        break;
                    case 10:
                        this.option10();
                        break;
                    case 11:
                        this.option11();
                        break;
                    case 12:
                        UserDataService.close();
                        System.out.println("have a nice day");
                        return;
                    default:
                        System.out.println("No option with number " + option);
                }
            } catch (AppException var2) {
                System.out.println("----------------------- !!! EXCEPTION !!! --------------------");
                System.out.println(var2.getMessage());
                System.out.println("--------------------------------------------------------------");
            }
        }
    }

    private int chooseOption() {
        System.out.println("1 - count average price of products ordered over a period of time");
        System.out.println("2 - show most expensive products in each category");
        System.out.println("3 - send customer orders list");
        System.out.println("4 - show day of the maximum number of orders");
        System.out.println("5 - show day of the minimum number of orders");
        System.out.println("6 - show customer who paid the most for orders");
        System.out.println("7 - count total price of all the orders with discounts");
        System.out.println("8 - show customers who bought a minimum specific number of the product and save customer information to file");
        System.out.println("9 - show most popular product category");
        System.out.println("10 - show products quantity of the months");
        System.out.println("11 - show most popular product  category in the specific month");
        System.out.println("12 - end of app");
        return UserDataService.getInt("Choose option:");
    }
    /*Methods option1-option11 used in the menu, based on all the methods from OrderService Class*/
    private void option1() {
        LocalDate start = UserDataService.getDate("Enter the beggining date");
        LocalDate end = UserDataService.getDate("Enter the end date");
        BigDecimal average = this.ordersService.countProductsAveragePriceWithinPeriod(start, end);
        System.out.println("Average price of product bought between " + start + " and " + end + " is " + average);
    }

    private void option2() {
        Map<Category, Product> mostExpensiveProductInCategory = this.ordersService.findMostExpensiveProductInCategory();
        this.convertMap(mostExpensiveProductInCategory);
    }

    private void option3() {
        this.ordersService.sendCustomerOrdersList();

    }

    private void option4() {
        System.out.println("Date of the maximum number of orders is: " + this.ordersService.findDateOfMaxOrders());

    }

    private void option5() {
        System.out.println("Date of the minimum number of orders is: " + this.ordersService.findDateOfMinOrders());
    }

    private void option6() {
        System.out.println("Customer who paid the most: " + this.ordersService.findCustomerWhoPaidMost());
    }

    private void option7() {
        System.out.println("total price of all the orders with discounts: " + this.ordersService.countOrdersTotalPriceWithDiscounts());
    }

    private void option8() {
        int min = UserDataService.getInt("Enter the minimum number of the product");
        System.out.println("Customers who bought  minimum " + min + " items " + this.ordersService.showCustomersWhoBoughtMinNumberOfItems(min));
    }

    private void option9() {
        System.out.println("most popular product category is: " + this.ordersService.findMostPopularProductCategory());
    }

    private void option10() {
        System.out.println("products quantity of the months:");

        Map<Month, Integer> quantityPerMonth = this.ordersService.showProductsQuantityOfMonths();
        convertMap(quantityPerMonth);
    }

    private void option11() {
        System.out.println("most popular product  category in the specific month");
        Map<Month, Category> popularProducts = this.ordersService.showPopularProductCategoryOfMonths();
        convertMap(popularProducts);
    }

    private static <T> String toJson(T element) {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
        return gson.toJson(element);
    }

    private void convertMap(Map<?, ?> map) {
        MapUtils.verbosePrint(System.out, "", map);
    }

    public MenuService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }
}
