
package com.neo4j.demo;

import com.neo4j.demo.repo.CustomerRepo;
import com.neo4j.demo.repo.TransferRepo;
import com.neo4j.demo.service.CustomerService;
import com.neo4j.demo.service.GraphService;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.driver.Driver;

import org.neo4j.driver.Result;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Theme(value = "mytodo")
@PWA(name = "My Todo", shortName = "My Todo", offlineResources = {})
@NpmPackage(value = "line-awesome", version = "1.3.0")
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableNeo4jRepositories
public class Application extends SpringBootServletInitializer implements AppShellConfigurator, CommandLineRunner  {

	private final TransferRepo transferRepo;
	private final CustomerRepo customerRepo;
	private final Driver driver;
	private final Neo4jTemplate template;
	private final CustomerService customerService;
	private final GraphService graphService;

	public Application(TransferRepo transferRepo, CustomerRepo customerRepo, Driver driver, Neo4jTemplate template, CustomerService customerService, GraphService graphService) {
		this.transferRepo = transferRepo;
		this.customerRepo = customerRepo;
		this.driver = driver;
		this.template = template;

		this.customerService = customerService;
		this.graphService = graphService;
	}

	public static void main(String[] args)  {
		try {
			SpringApplication.run(Application.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run(String... args) {

		log.info("start");
		driver.verifyConnectivity();

		graphService.deleteFrank();
		graphService.createFrank();
//		graphService.addMovie("Oz Liaisons");


		log.info("finished");

	}

}
