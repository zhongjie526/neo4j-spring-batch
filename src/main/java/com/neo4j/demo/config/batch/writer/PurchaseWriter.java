package com.neo4j.demo.config.batch.writer;

import com.neo4j.demo.model.Purchase;
import com.neo4j.demo.model.Transfer;
import com.neo4j.demo.repo.PurchaseRepo;
import com.neo4j.demo.repo.TransferRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseWriter implements ItemWriter<Purchase> {

    private final PurchaseRepo repo;

    public PurchaseWriter(PurchaseRepo repo) {
        this.repo = repo;
    }

    @Override
    public void write(List<? extends Purchase> purchases)  {
        repo.saveAll(purchases);
    }
}
