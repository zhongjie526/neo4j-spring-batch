package com.neo4j.demo.service;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.TransferRel;
import com.neo4j.demo.repo.CustomerRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CustomerService {
    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public List<Customer> findCustomers(){
        Customer customer = customerRepo.findFirstCustomerByAccountNumber("174-15-3773");

        Set<TransferRel> transfers= customer.getTransfersMade();

        List<Customer> recipients = StreamSupport.stream(transfers.spliterator(), false)
                .map(t->t.getRecipient())
                .collect(Collectors.toList());

        return  recipients;
    }


}
