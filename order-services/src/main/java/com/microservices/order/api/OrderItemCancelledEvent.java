package com.microservices.order.api;

public class OrderItemCancelledEvent {
	private final long orderId;
	private final long customerId;
	
	public OrderItemCancelledEvent(long orderId, long customerId){
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
