package com.neo4j.demo.config.batch.reader;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.Purchase;
import com.neo4j.demo.model.Transfer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.*;

import java.io.File;
import java.io.IOException;

@Profile("ingest")
@Configuration
public class ReaderConfig {
    private final ResourceLoader resourceLoader;

    @Value("${customerFile}") private Resource customerFileResource;
    @Value("${transferFile}") private Resource transferFileResource;
    @Value("${purchaseFile}") private Resource purchaseFileResource;

    private final LineMapper<Customer> customerLineMapper;
    private final LineMapper<Transfer> transferLineMapper;
    private final LineMapper<Purchase> purchaseLineMapper;

    public ReaderConfig(ResourceLoader resourceLoader, LineMapper<Customer> customerLineMapper, LineMapper<Transfer> transferLineMapper, LineMapper<Purchase> purchaseLineMapper) {
        this.resourceLoader = resourceLoader;
        this.customerLineMapper = customerLineMapper;
        this.transferLineMapper = transferLineMapper;
        this.purchaseLineMapper = purchaseLineMapper;
    }

    @Bean("CustomerCSVReader")
    public FlatFileItemReader<Customer> customerItemReader() throws IOException {
        return itemReader(Customer.class, customerFileResource, "Read Customer CSV", customerLineMapper);
    }

    @Bean
    public FlatFileItemReader<Transfer> transferItemReader() throws IOException {
        return itemReader(Transfer.class, transferFileResource, "Read Transfer CSV", transferLineMapper);}

    @Bean
    public FlatFileItemReader<Purchase> purchaseItemReader() throws IOException {
        return itemReader(Purchase.class, purchaseFileResource, "Read Purchase CSV", purchaseLineMapper);
    }

    public <T> FlatFileItemReader<T> itemReader(Class T, Resource resource, String readerName, LineMapper<T> lineMapper) {
        FlatFileItemReader<T> reader = new FlatFileItemReader<>();
        reader.setResource(resource);
        reader.setLinesToSkip(1);
        reader.setName(readerName);
        reader.setLineMapper(lineMapper);
        return reader;
    }
}
