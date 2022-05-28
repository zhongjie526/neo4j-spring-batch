package com.neo4j.demo.config.batch.job.ingest;


import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.Purchase;
import com.neo4j.demo.model.Transfer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("ingest")
@Configuration
public class IngestStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    @Autowired private  @Qualifier("CustomerCSVReader") ItemReader<Customer> customerCSVReader;
    @Autowired private  ItemWriter<Customer> customerItemWriter;

    @Autowired private  ItemReader<Transfer> transferItemReader;
    @Autowired private  ItemWriter<Transfer> transferItemWriter;

    @Autowired private  ItemReader<Purchase> purchaseItemReader;
    @Autowired private  ItemWriter<Purchase> purchaseItemWriter;

    public IngestStepConfig(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean("CustomerStep")
    public Step customerStep(){
        return  stepBuilderFactory.get("Customer Step")
                .<Customer, Customer>chunk(100)
                .reader(customerCSVReader)
                .writer(customerItemWriter)
                .build();
    }

    @Bean("TransferStep")
    public Step transferStep(){
        return stepBuilderFactory.get("Transfer Step")
                .<Transfer, Transfer>chunk(100)
                .reader(transferItemReader)
                .writer(transferItemWriter)
                .build();
    }

    @Bean("PurchaseStep")
    public Step purchaseStep(){
        return stepBuilderFactory.get("Purchase Step")
                .<Purchase, Purchase>chunk(100)
                .reader(purchaseItemReader)
                .writer(purchaseItemWriter)
                .build();
    }

}
