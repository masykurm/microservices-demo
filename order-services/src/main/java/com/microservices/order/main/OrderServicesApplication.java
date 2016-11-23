package com.microservices.order.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.microservices.order.controller.OrderCommandController;
import com.microservices.order.repo.OrderItemRepository;

@SpringBootApplication

@ComponentScan(basePackageClasses = {OrderCommandController.class})
@EnableMongoRepositories(basePackageClasses=OrderItemRepository.class)
public class OrderServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServicesApplication.class, args);
	}
}
