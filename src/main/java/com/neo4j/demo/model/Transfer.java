package com.neo4j.demo.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.Date;



public class Transfer {

    @Id
    @Getter @Setter private String transactionId;
    @Getter @Setter private String senderAccountNumber;
    @Getter @Setter private String receiverAccountNumber;
    @Getter @Setter private double amount;
    @Getter @Setter private Date transferDatetime;
}
