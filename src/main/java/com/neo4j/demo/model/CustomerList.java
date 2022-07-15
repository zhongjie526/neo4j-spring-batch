package com.neo4j.demo.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
public class CustomerList implements Serializable {

    @Getter @Setter Customer customer1;
    @Getter @Setter Customer customer2;
    @Getter @Setter Customer customer3;


    @Override
    public String toString() {
        return getCustomer1().getFirstName()+"--->"+getCustomer2().getFirstName()+"--->"+getCustomer3().getFirstName();
    }

}
