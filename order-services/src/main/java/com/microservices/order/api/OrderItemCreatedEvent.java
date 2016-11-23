package com.microservices.order.api;

public class OrderItemCreatedEvent {
	
	private final long orderId;
	private final long customerId;
	private final long price;
	private final String[] productId;
	
	public OrderItemCreatedEvent(long orderId, long customerId,String[] productId,  long price){
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
