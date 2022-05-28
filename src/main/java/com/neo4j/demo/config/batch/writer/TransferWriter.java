package com.neo4j.demo.config.batch.writer;

import com.neo4j.demo.model.Transfer;
import com.neo4j.demo.repo.TransferRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransferWriter implements ItemWriter<Transfer> {

    private final TransferRepo repo;

    public TransferWriter(TransferRepo repo) {
        this.repo = repo;
    }

    @Override
    public void write(List<? extends Transfer> transfers)  {
        repo.saveAll(transfers);
    }
}
