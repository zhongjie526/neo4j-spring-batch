package com.neo4j.demo.repo;

import com.neo4j.demo.model.Customer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends Neo4jRepository<Customer, String> {

    Customer findCustomerByAccountNumber(String accountNumber);

    boolean existsCustomerByAccountNumber(String accountNumber);

    boolean existsCustomerByCardNumber(String cardNumber);

    Customer findCustomerByCardNumber(String cardNumber);


}
