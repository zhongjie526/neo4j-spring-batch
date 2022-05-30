
package com.neo4j.demo;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.TransferRel;
import com.neo4j.demo.repo.CustomerRepo;
import com.neo4j.demo.repo.TransferRepo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableNeo4jRepositories
public class Application implements CommandLineRunner{

	private final TransferRepo transferRepo;
	private final CustomerRepo customerRepo;

	public Application(TransferRepo transferRepo, CustomerRepo customerRepo) {
		this.transferRepo = transferRepo;
		this.customerRepo = customerRepo;
	}

	public static void main(String[] args)  {
		try {
			SpringApplication.run(Application.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Transactional
	@Override
	public void run(String... args) throws Exception {
		log.info("start");
//		transferRepo.findAll().forEach(transfer -> {
//			String senderAccountNumber = transfer.getSenderAccountNumber();
//			if (senderAccountNumber != null && customerRepo.existsCustomerByAccountNumber(senderAccountNumber)) {
//				Customer sendingCustomer = customerRepo.findCustomerByAccountNumber(senderAccountNumber);
//				String accountNumber = transfer.getReceiverAccountNumber();
//				if (accountNumber != null && customerRepo.existsCustomerByAccountNumber(accountNumber)) {
//					Customer receivingCustomer = customerRepo.findCustomerByAccountNumber(accountNumber);
//					log.info("Transfer: "+transfer.getReceiverAccountNumber());
//					TransferRel transferRel = new TransferRel(transfer.getTransactionId(), receivingCustomer);
//					transferRel.setTransferAmount(transfer.getAmount());
//					transferRel.setTransferDate(transfer.getTransferDatetime());
//					log.info("transferRel: " + transferRel.getTransferAmount());
//
//					sendingCustomer.getCustomerTransferredTo().add(transferRel);
//					customerRepo.save(sendingCustomer);
//				}
//			}
//		});
	}
}
