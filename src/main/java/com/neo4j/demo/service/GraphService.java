package com.neo4j.demo.service;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.repo.CustomerRepo;
import com.neo4j.demo.repo.PurchaseRepo;
import com.neo4j.demo.repo.TransferRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Profile("build")
@Slf4j
@Component
public class GraphService {

    private final CustomerRepo customerRepo;
    private final TransferRepo transferRepo;
    private final PurchaseRepo purchaseRepo;


    public GraphService(CustomerRepo customerRepo, TransferRepo transferRepo, PurchaseRepo purchaseRepo) {
        this.customerRepo = customerRepo;
        this.transferRepo = transferRepo;
        this.purchaseRepo = purchaseRepo;
    }

    public List<Customer> addTransferToCustomer(){
        log.info("Adding Transfers to Customers");

        Map<String,Customer> customerAccountNumberMap =  customerRepo.findAll().stream().collect(Collectors.toMap(Customer::getAccountNumber, Function.identity()));
        AtomicInteger i = new AtomicInteger(1);
        transferRepo.findAll().stream().forEach(transfer -> {
            String senderAccountNumber =transfer.getSenderAccountNumber();
            if (senderAccountNumber != null && customerAccountNumberMap.containsKey(senderAccountNumber)) {
                Customer customer = customerAccountNumberMap.get(senderAccountNumber);
                String receiverAccountNumber = transfer.getReceiverAccountNumber();
                if (receiverAccountNumber != null && customerAccountNumberMap.containsKey(receiverAccountNumber)) {
                    Customer customerTransferredTo = customerAccountNumberMap.get(receiverAccountNumber);
                    log.info(i+" : "+customer.getFirstName()+" transferred to "+customerTransferredTo.getFirstName());
                    customer.getCustomerTransferredTo().add(customerTransferredTo);
                }
            }
            i.getAndIncrement();
        });

        return new ArrayList<>(customerAccountNumberMap.values());
    }

    public List<Customer> addPurchaseToCustomer(){

        log.info("Adding Purchases to Customers");

        Map<String,Customer> customerCardNumberMap =  customerRepo.findAll().stream().collect(Collectors.toMap(Customer::getCardNumber, Function.identity()));

        AtomicInteger i = new AtomicInteger(1);
        purchaseRepo.findAll().stream().forEach(purchase -> {
            String cardNumber = purchase.getCardNumber();
            if(cardNumber !=null && customerCardNumberMap.containsKey(cardNumber)){
                Customer customer = customerCardNumberMap.get(cardNumber);
                log.info(i+" : "+customer.getFirstName() + " made a purchase with amount: " + purchase.getAmount());
                customer.getPurchasesMade().add(purchase);
            }
            i.getAndIncrement();
        });

        return new ArrayList<>(customerCardNumberMap.values());

    }


}
