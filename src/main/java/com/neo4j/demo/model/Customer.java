
package com.neo4j.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Node
public class Customer {

	@Id @Getter @Setter private String CIF;
	@Property @Getter @Setter private int age;
	@Property @Getter @Setter private String emailAddress;
	@Property @Getter @Setter private String firstName;
	@Property @Getter @Setter private String lastName;
	@Property @Getter @Setter private String phoneNumber;
	@Property @Getter @Setter private String gender;
	@Property @Getter @Setter private String address;
	@Property @Getter @Setter private String country;
	@Property @Getter @Setter private String jobTitle;
	@Property @Getter @Setter private String cardNumber;
	@Property @Getter @Setter private String accountNumber;

	@Getter @Setter
	@Relationship(type="TRANSFERRED_TO",direction = Relationship.Direction.OUTGOING)
	private Set<Customer> customerTransferredTo = new HashSet<>();

	@Getter @Setter
	@Relationship(type="MAKE_PURCHASE",direction = Relationship.Direction.OUTGOING)
	private Set<Purchase> purchasesMade = new HashSet<>();

}
