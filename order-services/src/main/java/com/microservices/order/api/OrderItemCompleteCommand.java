package com.microservices.order.api;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class OrderItemCompleteCommand {
	@TargetAggregateIdentifier
	private final long orderId;
	private final long customerId;
	
	public OrderItemCompleteCommand(long orderId, long customerId){
		this.orderId = orderId;
		this.customerId = customerId;
	}
	
	public long getOrderId() {
		return orderId;
	}

	public long getCustomerId() {
		return customerId;
	}
}
