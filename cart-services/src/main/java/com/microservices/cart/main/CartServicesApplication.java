package com.microservices.cart.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.microservices.cart.controller.CartController;
import com.microservices.cart.repo.CartItemRepository;

@SpringBootApplication
@ComponentScan(basePackageClasses = CartController.class)
@EnableMongoRepositories(basePackageClasses=CartItemRepository.class)
public class CartServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartServicesApplication.class, args);
	}
}
