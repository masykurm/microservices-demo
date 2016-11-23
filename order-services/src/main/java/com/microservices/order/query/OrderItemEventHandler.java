package com.microservices.order.query;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.stereotype.Component;

import com.microservices.order.api.OrderItemCreatedEvent;
import com.microservices.order.repo.OrderItemRepository;

@Component
public class OrderItemEventHandler{

	
	private OrderItemRepository repository;
	
		
	@EventHandler
    public void handleOrderItemCreatedEvent(OrderItemCreatedEvent evt){
		System.out.println("[OrderItemEventHandler] onCreate eventHandler invoked : ");
		System.out.format("[OrderItemEventHandler] create & save new OrderItemEntity: customerId[%d] , orderId[%s], product[%s]\n",evt.getCustomerId(),evt.getOrderId(), evt.getProductId());
		OrderItemEntity orderItemEntity = new OrderItemEntity(evt.getCustomerId(),evt.getOrderId(),evt.getProductId());
		repository.save(orderItemEntity);
		System.out.format("[OrderItemEventHandler] new OrderItemEntity saved");

	}
	
	
	public OrderItemRepository getRepository() {
		return repository;
	}

	
	public void setRepository(OrderItemRepository repository) {
		this.repository = repository;
	}
}
