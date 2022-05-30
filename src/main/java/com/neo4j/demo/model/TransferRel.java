package com.neo4j.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Date;

@RelationshipProperties
public class TransferRel {

    @RelationshipId @GeneratedValue
    @Getter @Setter private Long id;

    @Getter @Setter private String transactionId;

    @TargetNode
    @Getter @Setter private Customer recipient;

    @Getter @Setter private  Date transferDate;
    @Getter @Setter private  double transferAmount;

    public TransferRel(String transactionId, Customer recipient) {
        this.transactionId = transactionId;
        this.recipient = recipient;
    }
}
