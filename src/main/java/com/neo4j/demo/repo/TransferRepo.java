package com.neo4j.demo.repo;

import com.neo4j.demo.model.Transfer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepo extends Neo4jRepository<Transfer, String> {


}
