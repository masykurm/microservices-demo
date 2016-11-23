package com.microservices.order.query;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class OrderItemEntity {

	@Id
	private long orderId;
	private long customerId;
	private String[] productId;
	private Date createdDate;
	private int orderStatus;
	
		
	public OrderItemEntity(){
		
	}
	
	public OrderItemEntity(long customerId, long orderId, String[] productId){
		this.orderId = orderId;
		this.customerId = customerId;
		this.createdDate = new Date();
		this.orderStatus = OrderUtil.ORDER_OPEN;
		this.setProductId(productId);
	}
	

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String[] getProductId() {
		return productId;
	}

	public void setProductId(String[] productId) {
		this.productId = productId;
	}
}
