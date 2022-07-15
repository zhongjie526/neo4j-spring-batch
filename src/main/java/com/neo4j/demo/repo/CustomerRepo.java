package com.neo4j.demo.repo;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.CustomerList;
import org.neo4j.cypherdsl.core.*;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.support.CypherdslStatementExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface CustomerRepo extends Neo4jRepository<Customer, String>, CypherdslStatementExecutor<Customer> {

    Customer findCustomerByAccountNumber(String accountNumber);

    Customer findFirstCustomerByAccountNumber(String accountNumber);

    boolean existsCustomerByAccountNumber(String accountNumber);

    boolean existsCustomerByCardNumber(String cardNumber);

    Customer findCustomerByCardNumber(String cardNumber);

    Customer findCustomerByFirstName(String firstName);

    @Query("MATCH (n:Customer) RETURN n LIMIT 25")
    Stream<Customer> findAllByCustomQueryAndStream();

    static Statement whoHasFirstNameWithTransfers(String name){
        Node a = Cypher.node("Customer").named("a");
        Node b = Cypher.anyNode("b");
        Relationship r = a.relationshipTo(b, "TRANSFER");
        return Cypher.match(r)
                .where(a.property("firstName").isEqualTo(Cypher.anonParameter(name)))
                .returning(b)
                .limit(1)
                .build();
    }

    @Query("MATCH (n:Customer) where not ((n) --> ()) return n")
    List<Customer> findCustomersWithNoTransfersOut();

    @Query("match (a:Customer)-[]->(b:Customer)-[]->(c:Customer)-[]->(a:Customer)\n" +
            "return [a,b]")
    List<Customer[]> findCustomerDoubles();

    @Query("match (a:Customer)-[]->(b:Customer)-[]->(c:Customer)-[]->(a:Customer)\n" +
            "return [a,b,c]")
    List<Customer[]> findCustomerTriples();

    @Query("match (a)-[:TRANSFER*..2]-(b) where a.firstName='Jack' return distinct b")
    List<Customer> twoHops();

    @Query("match (a:Customer)-[:TRANSFER]->(b:Customer)-[:TRANSFER]->(c:Customer)-[:TRANSFER]->(a:Customer)\n" +
            "return distinct a")
    List<Customer> findCustomers();

    class CustomerTriple{
        Customer customer1;
    }

    static Statement loopTransfers(){
        Node a = Cypher.node("Customer").named("a");
        Node b = Cypher.node("Customer").named("b");
        Node c = Cypher.node("Customer").named("c");

        Relationship r1 = a.relationshipTo(b, "TRANSFER");
        Relationship r2 = b.relationshipTo(c, "TRANSFER");
        Relationship r3 = c.relationshipTo(a, "TRANSFER");

        return Cypher.match(r1).match(r2).match(r3).returning(a, b, c).build();
    }

    static Statement whoHasNoTransfers(){
        Node a = Cypher.node("Customer").named("a");
        Node b = Cypher.node("Customer").named("b");
        Relationship r = a.relationshipTo(b, "TRANSFER");
        return Cypher.match(r.inverse())
                .returning(b)
                .limit(1)
                .build();
    }
}
