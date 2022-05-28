package com.neo4j.demo.config.batch.writer;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.repo.CustomerRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CustomerWriter implements ItemWriter<Customer> {

    private final CustomerRepo repo;

    public CustomerWriter(CustomerRepo repo) {
        this.repo = repo;
    }

    @Override
    public void write(List<? extends Customer> customers)  {repo.saveAll(customers);}
}
