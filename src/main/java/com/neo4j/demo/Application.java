
package com.neo4j.demo;

import com.neo4j.demo.service.GraphService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Profile({"ingest","build"})
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableNeo4jRepositories
public class Application {

	public static void main(String[] args)  {
		try {
			SpringApplication.run(Application.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
