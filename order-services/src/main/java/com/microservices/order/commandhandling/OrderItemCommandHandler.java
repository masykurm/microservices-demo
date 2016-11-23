package com.microservices.order.commandhandling;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.stereotype.Component;

import com.microservices.order.api.OrderItemCancelCommand;
import com.microservices.order.api.OrderItemCompleteCommand;
import com.microservices.order.api.OrderItemConfirmCommand;
import com.microservices.order.api.OrderItemCreateCommand;
import com.microservices.order.repo.OrderItemRepository;
import com.microservices.order.repo.OrderItemRepositoryService;

@Component
public class OrderItemCommandHandler {

	private Repository<OrderItem> repository;
	private OrderItemRepositoryService repositoryService;
	
	public OrderItemCommandHandler(Repository<OrderItem> repository, OrderItemRepository orderRepository){
		this.repository = repository;
		this.repositoryService = new OrderItemRepositoryService(orderRepository);
	}
	
	@CommandHandler
    public void createOrder(OrderItemCreateCommand command) {
		System.out.println("[OrderItemCommandHandler] createOrder commandHandler invoked");
		System.out.println("[OrderItemCommandHandler] saving new OrderItem in the Event Repository");
        repository.add(new OrderItem(command.getCustomerId(),command.getOrderId(), command.getProductId(),command.getPrice(), repositoryService));
	}
	
	@CommandHandler
	public void confirmOrder(OrderItemConfirmCommand command){
		System.out.println("[OrderItemCommandHandler] confirmOrder commandHandler invoked");
		System.out.format("[OrderItemCommandHandler] load OrderItem from the Event Repository : orderId[%s]", command.getOrderId());
		try {
		OrderItem orderItem = repository.load(command.getOrderId());
		System.out.format("[OrderItemCommandHandler] confirming OrderItem in the Event Repository : \n");
		orderItem.confirm();
		System.out.format("[OrderItemCommandHandler] adding confirmed OrderItem in the Event Repository : orderItem[%s] ,orderItemId[%s] \n", orderItem, command.getOrderId());
		repository.add(orderItem);
		System.out.println("[OrderItemCommandHandler] orderItem confirmed ");
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@CommandHandler
	public void cancelOrder(OrderItemCancelCommand command){
		System.out.println("[OrderItemCommandHandler] cancelOrder commandHandler invoked");
		System.out.println("[OrderItemCommandHandler] load OrderItem from the Event Repository");
		OrderItem orderItem = repository.load(command.getOrderId());
		System.out.format("[OrderItemCommandHandler] cancelling OrderItem in the Event Repository : orderItem[%s] ,orderItemId[%s] \n", orderItem, command.getOrderId());
		orderItem.cancel();
		System.out.format("[OrderItemCommandHandler] adding cancelled OrderItem in the Event Repository : orderItem[%s] ,orderItemId[%s] \n", orderItem, command.getOrderId());
		repository.add(orderItem);
		System.out.println("[OrderItemCommandHandler] orderItem cancelled ");
		
	}
	

	@CommandHandler
	public void completeOrder(OrderItemCompleteCommand command){
		System.out.println("[OrderItemCommandHandler] completeOrder commandHandler invoked");
		System.out.println("[OrderItemCommandHandler] load OrderItem from the Event Repository");
		OrderItem orderItem = repository.load(command.getOrderId());
		System.out.format("[OrderItemCommandHandler] completing OrderItem in the Event Repository : orderItem[%s] ,orderItemId[%s] \n", orderItem, command.getOrderId());
		orderItem.complete();
		System.out.format("[OrderItemCommandHandler] adding completed OrderItem in the Event Repository : orderItem[%s] ,orderItemId[%s] \n", orderItem, command.getOrderId());
		repository.add(orderItem);
		System.out.println("[OrderItemCommandHandler] orderItem completed");
		
	}

	public Repository<OrderItem> getRepository() {
		return repository;
	}

	public void setRepository(Repository<OrderItem> repository) {
		this.repository = repository;
	}

	public OrderItemRepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(OrderItemRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	
	
	

}
