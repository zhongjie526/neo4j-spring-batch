
package com.neo4j.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Node
public class Purchase {

	@Id @Getter @Setter private String transactionId;
	@Property @Getter @Setter private String cardNumber;
	@Property @Getter @Setter private String merchant;
	@Property @Getter @Setter private double amount;
	@Property @Getter @Setter private Date purchaseDateTime;
	@Property @Getter @Setter private String cardIssuer;


}
