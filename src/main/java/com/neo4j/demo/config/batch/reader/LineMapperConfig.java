package com.neo4j.demo.config.batch.reader;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.Purchase;
import com.neo4j.demo.model.Transfer;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.time.format.DateTimeFormatter;

@Configuration
public class LineMapperConfig {

    @Bean
    public LineMapper<Customer> customerLineMapper(){return lineMapper(Customer.class, FileHeader.customerHeader);}

    @Bean
    public LineMapper<Transfer> transferLineMapper(){return lineMapper(Transfer.class, FileHeader.transferHeader);}

    @Bean
    public LineMapper<Purchase> purchaseLineMapper(){return lineMapper(Purchase.class, FileHeader.purchaseHeader);}

    public <T> LineMapper<T> lineMapper(Class T,String[] headers) {
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames(headers);
        BeanWrapperFieldSetMapper<T> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(T);
        fieldSetMapper.setConversionService(conversionService());
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    public FormattingConversionService conversionService(){
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);
        DateTimeFormatterRegistrar dateTimeRegistrar = new DateTimeFormatterRegistrar();
        dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dateTimeRegistrar.registerFormatters(conversionService);

        DateFormatterRegistrar dateRegistrar = new DateFormatterRegistrar();
        dateRegistrar.setFormatter(new DateFormatter("yyyy-MM-dd"));
        dateRegistrar.registerFormatters(conversionService);
        return conversionService;
    }
}
