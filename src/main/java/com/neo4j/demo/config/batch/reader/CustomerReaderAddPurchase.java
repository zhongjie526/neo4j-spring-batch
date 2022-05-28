package com.neo4j.demo.config.batch.reader;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.service.GraphService;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("build")
@Component("CustomerReaderAddPurchase")
public class CustomerReaderAddPurchase implements ItemReader<Customer> {

    private List<Customer> customers;
    private int nextCustomerIndex;
    private final GraphService graphService;

    public CustomerReaderAddPurchase(GraphService graphService) {
        this.graphService = graphService;
        initialize();
    }

    private void initialize(){
        customers = graphService.addPurchaseToCustomer();
        nextCustomerIndex=0;
    }

    @Override
    public Customer read()  {
        Customer nextCustomer = null;

        if(nextCustomerIndex < customers.size()){
            nextCustomer = customers.get(nextCustomerIndex);
            nextCustomerIndex++;
        } else {
            nextCustomerIndex=0;
        }
        return nextCustomer;
    }
}
