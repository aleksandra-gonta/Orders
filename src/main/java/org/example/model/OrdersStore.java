/*This is class creating  object storing orders*/
package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrdersStore {
    private Set<Order> orders;
}