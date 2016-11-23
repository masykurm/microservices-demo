package com.microservices.order.api;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class OrderItemCreateCommand {

	@TargetAggregateIdentifier
	private final long orderId;
	private final long customerId;
	private final long price;
	private final String[] productId;
	
	public OrderItemCreateCommand(long orderId, long customerId, long price, String[] productId){
		this.orderId = orderId;
		this.customerId = customerId;
		this.price = price;
		this.productId = productId;
	}

	public long getOrderId() {
		return orderId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public long getPrice() {
		return price;
	}

	public String[] getProductId() {
		return productId;
	}
}
