package com.neo4j.demo.config.batch.reader;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.Purchase;
import com.neo4j.demo.model.Transfer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

@Profile("ingest")
@Configuration
public class ReaderConfig {
    @Value("${customerFile}") private File customerFile;
    @Value("${transferFile}") private File transferFile;
    @Value("${purchaseFile}") private File purchaseFile;

    private final LineMapper<Customer> customerLineMapper;
    private final LineMapper<Transfer> transferLineMapper;
    private final LineMapper<Purchase> purchaseLineMapper;

    public ReaderConfig(LineMapper<Customer> customerLineMapper, LineMapper<Transfer> transferLineMapper, LineMapper<Purchase> purchaseLineMapper) {
        this.customerLineMapper = customerLineMapper;
        this.transferLineMapper = transferLineMapper;
        this.purchaseLineMapper = purchaseLineMapper;
    }

    @Bean("CustomerCSVReader")
    public FlatFileItemReader<Customer> customerItemReader() {return itemReader(Customer.class, customerFile, "Read Customer CSV", customerLineMapper);}

    @Bean
    public FlatFileItemReader<Transfer> transferItemReader(){return itemReader(Transfer.class, transferFile, "Read Transfer CSV", transferLineMapper);}

    @Bean
    public FlatFileItemReader<Purchase> purchaseItemReader(){return itemReader(Purchase.class, purchaseFile, "Read Purchase CSV", purchaseLineMapper);}

    public <T> FlatFileItemReader<T> itemReader(Class T, File file, String readerName, LineMapper<T> lineMapper) {
        FlatFileItemReader<T> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(file));
        reader.setLinesToSkip(1);
        reader.setName(readerName);
        reader.setLineMapper(lineMapper);
        return reader;
    }
}
