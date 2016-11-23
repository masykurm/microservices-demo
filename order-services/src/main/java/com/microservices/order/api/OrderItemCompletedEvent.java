package com.microservices.order.api;

public class OrderItemCompletedEvent {
	private final long orderId;
	private final long customerId;
	
	public OrderItemCompletedEvent(long orderId, long customerId){
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
