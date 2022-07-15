package com.neo4j.demo.service;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.CustomerList;
import com.neo4j.demo.model.MovieEntity;
import com.neo4j.demo.repo.CustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.Driver;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Path;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public Stream<String> getPaths(){
        return driver.session().run("match p=(a)-[:TO*]-(c) \n" +
                "where a.Name='H'  and c.Name='P' \n" +
                "return p limit 1").stream().map(record -> {
            System.out.println(record.size());
            Value record1 = record.get(0);
            System.out.println(record1);
            return record1.asPath().toString();
        });
    }

    public Stream<String> getLoops(){
        return driver.session().run("match (a:Customer)-[:TRANSFER]->(b:Customer)-[]->(c:Customer)-[]->(a:Customer) return a,b,c").stream().map(record -> {
            Customer customer1 = Customer.valueToCustomer(record.get("a"));
            Customer customer2 = Customer.valueToCustomer(record.get("b"));
            Customer customer3 = Customer.valueToCustomer(record.get("c"));

            return new CustomerList(customer1,customer2,customer3).toString();
        });
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



    public List<CustomerList> getCustomerDoubles() {

        boolean isFirst = true;
        List<CustomerList> customerLists = new ArrayList<>();
        CustomerList customerList = new CustomerList();
        for(Customer[] customers: customerRepo.findCustomerDoubles()){
//            System.out.println("data:");
//            System.out.println(customers[0]);
            if(isFirst){
                customerList.setCustomer1(customers[0]);
                isFirst = false;
            } else{
                customerList.setCustomer2(customers[0]);
                customerLists.add(customerList);
                customerList = new CustomerList();
                isFirst = true;
            }
        }
        return customerLists;
    }


    public List<CustomerList> getCustomerTriples() {

        int counter = 1;
        List<CustomerList> customerLists = new ArrayList<>();
        CustomerList customerList = new CustomerList();
        for(Customer[] customers: customerRepo.findCustomerTriples()){
            Customer payload = customers[0];

            if(counter%3 ==1){
                customerList.setCustomer1(payload);
            } else if(counter%3==2){
                customerList.setCustomer2(payload);
            } else {
                customerList.setCustomer3(payload);
                customerLists.add(customerList);
                customerList = new CustomerList();
            }
            counter++;
        }
        return customerLists;
    }


    public void deleteFrank(){
        driver.session().run("match (n:Customer {firstName:'Frank',lastName:'Zhang'})-[r]-() delete r;");
        driver.session().run("match (n:Customer {firstName:'Frank',lastName:'Zhang'}) delete n;");
    }

    public void createFrank(){
        driver.session().run("create (n:Customer {firstName:\"Frank\",lastName:\"Zhang\",customerID:\"999999\"})");
    }

    public void addMovie(String movie){
        driver.session().run(String.format("match  (n:Customer {firstName:'Frank',lastName:'Zhang'}),(f:Film {Title:\"%s\"})\n" +
                "create (n)-[:RENTED]->(f)",movie));
    }

    public List<String> getNeighbors(String customerID, int numberOfNeighbors){
        Map<String,Object> params = new HashMap<>();
        params.put("customerID",customerID);
        params.put("numberOfNeighbors", numberOfNeighbors);
        Result result = driver.session().run(String.format("""
                    MATCH (c1:Customer)-[:RENTED]->(f:Film)<-[:RENTED]-(c2:Customer)
                    WHERE c1 <> c2 AND c1.customerID = $customerID
                    WITH c1, c2, COUNT(DISTINCT f) as intersection
                
                    // get count of all the distinct movies that they have rented in total (Union)
                    MATCH (c:Customer)-[:RENTED]->(f:Film)
                    WHERE c in [c1, c2]
                    WITH c1, c2, intersection, COUNT(DISTINCT f) as union
                
                    // compute Jaccard index
                    WITH c1, c2, intersection, union, (intersection * 1.0 / union) as jaccard_index
                   
                    // get top k nearest neighbors based on Jaccard index
                    ORDER BY jaccard_index DESC
                    return  c2.customerID as customer limit $numberOfNeighbors
                """),params);

        return result.stream().map(record -> record.get(0).asString()).collect(Collectors.toList());
    }

    public List<String> getRecommendations(String customerID,List<String> neighbors,int numberOfRecommendations){
        Map<String,Object> params = new HashMap<>();
        params.put("customerID",customerID);
        params.put("neighbors", neighbors);
        params.put("numberOfRecommendations", numberOfRecommendations);

        Result result = driver.session().run(String.format("""
                    MATCH (c1:Customer),
                          (neighbor:Customer)-[:RENTED]->(f:Film)    // all movies rented by neighbors
                    WHERE c1.customerID = $customerID
                      AND neighbor.customerID in $neighbors
                      AND not (c1)-[:RENTED]->(f)                    // filter for movies that our user hasn't rented
                    WITH c1, f, COUNT(DISTINCT neighbor) as countnns // times rented by nns
                    ORDER BY c1.customerID, countnns DESC              
                    RETURN f.Title as recommendations limit $numberOfRecommendations
                """),params);

        return result.stream().map(record -> record.get(0).asString()).collect(Collectors.toList());
    }

}
