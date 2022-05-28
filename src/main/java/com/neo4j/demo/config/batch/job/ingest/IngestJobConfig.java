package com.neo4j.demo.config.batch.job.ingest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("ingest")
@Configuration
@EnableBatchProcessing
@Primary
public class IngestJobConfig extends DefaultBatchConfigurer {

    private final JobBuilderFactory jobBuilderFactory;
    @Autowired private @Qualifier("CustomerStep") Step customerStep;
    @Autowired private @Qualifier("TransferStep") Step transferStep;
    @Autowired private @Qualifier("PurchaseStep")Step purchaseStep;

    public IngestJobConfig(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("CSV Ingestion")
                .incrementer(new RunIdIncrementer())
                .start(customerStep)
                .next(transferStep)
                .next(purchaseStep)
                .build();
    }









}
