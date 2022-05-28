package com.neo4j.demo.repo;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.Purchase;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepo extends Neo4jRepository<Purchase, String> {

}
