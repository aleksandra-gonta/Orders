package org.example.converter;


import org.example.converter.generic.JsonConverter;
import org.example.model.Order;

import java.util.List;

public class OrdersStoreJsonConverter extends JsonConverter<List<Order>> {
    public OrdersStoreJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
