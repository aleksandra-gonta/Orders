    /*Main class of the application*/
package org.example;

import org.example.model.Customer;
import org.example.model.Product;
import org.example.model.enums.Category;
import org.example.service.DataGenerator;
import org.example.service.OrdersService;
import org.example.ui.MenuService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        List<Customer> customers = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        customers.add(new Customer("ANNA", "KOWALSKA", 20, "ANNA.KOWALSKA@GMAIL.COM"));
        customers.add(new Customer("JAN", "NOWAK", 25, "J.NOWAK@GMAIL.COM"));
        customers.add(new Customer("ZENON", "MARCZUK", 40, "ZENON_MARCZUK@GMAIL.COM"));
        customers.add(new Customer("HENRYKA", "ALSKA", 32, "H.ALSKA@GMAIL.COM"));
        customers.add(new Customer("HANNA", "KOWAL", 19, "H.KOWAL@GMAIL.COM"));
        customers.add(new Customer("JANINA", "NOREK", 28, "J.NOREK@GMAIL.COM"));
        customers.add(new Customer("ZUZANNA", "MARKOWSKA", 54, "Z_MARKO@GMAIL.COM"));
        customers.add(new Customer("HALINA", "ANRUSZ", 22, "H.ANRUSZ@GMAIL.COM"));

        products.add(new Product("PANTS", BigDecimal.valueOf(100), Category.A));
        products.add(new Product("PARFUMES", BigDecimal.valueOf(400), Category.C));
        products.add(new Product("BOOK", BigDecimal.valueOf(50), Category.B));
        products.add(new Product("SHIRT", BigDecimal.valueOf(300), Category.A));
        products.add(new Product("SHAMPOO", BigDecimal.valueOf(30), Category.C));
        products.add(new Product("BOOK", BigDecimal.valueOf(30), Category.B));
        products.add(new Product("SHOES", BigDecimal.valueOf(200), Category.A));
        products.add(new Product("CREAM", BigDecimal.valueOf(200), Category.C));
        products.add(new Product("BOOK", BigDecimal.valueOf(45), Category.B));

        DataGenerator dataGenerator = new DataGenerator(customers, products);
        dataGenerator.generateData();

        final String JSON_FILENAME = "sample.json";
        OrdersService ordersService = new OrdersService(JSON_FILENAME);

        MenuService menuService = new MenuService(ordersService);
        menuService.mainMenu();

    }
}
