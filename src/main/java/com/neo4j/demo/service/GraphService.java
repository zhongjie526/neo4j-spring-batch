package com.neo4j.demo.service;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.MovieEntity;
import com.neo4j.demo.repo.CustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.Driver;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class GraphService {

    private final Driver driver;
    private final Neo4jTemplate template;
    private final ReactiveNeo4jTemplate rxTemplate;
    private final CustomerRepo customerRepo;
    private final MappingContext mappingContext;

    public GraphService(Driver driver, Neo4jTemplate template, ReactiveNeo4jTemplate rxTemplate, CustomerRepo customerRepo, MappingContext mappingContext) {
        this.driver = driver;
        this.template = template;
        this.rxTemplate = rxTemplate;
        this.customerRepo = customerRepo;
        this.mappingContext = mappingContext;
    }

    public int getNodes(){
        log.info("Getting count of all nodes");
        return driver.session().run("MATCH(n) RETURN count(*) as nodes").single().get("nodes").asInt();
    }

    public int getNodes(String label) {
        log.info(String.format("Getting count of all nodes where label=%s",label));
        return driver.session().run(String.format("MATCH (:%s) RETURN count(*) as nodes",
                StringUtils.capitalize(label))).single().get("nodes").asInt();
    }

    public void get(){
        template.count(Customer.class);
    }


    public Stream<String> titles() {

        return template.findAll(MovieEntity.class).stream().map(movieEntity -> movieEntity.getTitle());
    }

    public Flux<MovieEntity> movies(){
        return rxTemplate.findAll(MovieEntity.class);
    }

    public List<Customer[]> getTriangle() {
        return customerRepo.findCustomerTriangles();
    }
    public List<Customer> getCustomer() {
        return customerRepo.findCustomers();
    }
}
