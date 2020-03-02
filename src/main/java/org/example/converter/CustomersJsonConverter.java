/*This is converter class extending JsonConverter class for JSON files conversion*/
package org.example.converter;

import org.example.converter.generic.JsonConverter;
import org.example.model.Customer;

import java.util.List;

public class CustomersJsonConverter extends JsonConverter<List<Customer>> {

    public CustomersJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}


