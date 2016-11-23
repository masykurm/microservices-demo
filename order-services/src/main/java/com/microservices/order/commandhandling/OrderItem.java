package com.microservices.order.commandhandling;

import java.util.Date;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import com.microservices.order.api.OrderItemCancelledEvent;
import com.microservices.order.api.OrderItemCompletedEvent;
import com.microservices.order.api.OrderItemConfirmedEvent;
import com.microservices.order.api.OrderItemCreatedEvent;
import com.microservices.order.query.OrderUtil;
import com.microservices.order.repo.OrderItemRepositoryService;

public class OrderItem extends AbstractAnnotatedAggregateRoot<Object>{

	@AggregateIdentifier
	private long orderId;
	private long customerId;
	private Date createdDate;
	private int orderStatus;
	private long totalPrice;
	private String[] productId;
	private OrderItemRepositoryService service;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1527435769321421602L;
	
	
	public OrderItem(){
		
	}
	
	public OrderItem(long customerId, long orderId, String[] productId, long totalPrice,OrderItemRepositoryService service){
		this.orderId = orderId;
		this.customerId = customerId;
		this.createdDate = new Date();
		this.setProductId(productId);
		this.orderStatus = OrderUtil.ORDER_OPEN;
		this.service = service;
		this.setTotalPrice(totalPrice);
		System.out.println("[OrderItem] orderItem constructor called");
		System.out.println("[OrderItem] applying new OrderItemCreatedEvent");
		apply(new OrderItemCreatedEvent(
					this.orderId, 
					this.customerId,
					this.productId,
					this.totalPrice
					));
	}
	
	@EventHandler
    public void handleOrderItemCreatedEvent(OrderItemCreatedEvent evt){
		System.out.println("[OrderItem] handleOrderItemCreatedEvent eventHandler invoked : ");
		if(this.service != null){
			System.out.format("[OrderItem] create & save new OrderItemEntity: customerId[%d] , orderId[%s], product[%s], service[%s]\n",evt.getCustomerId(),evt.getOrderId(), evt.getProductId(), this.service);
			this.service.handleOrderItemCreatedEvent(evt);
			System.out.format("[OrderItem] new OrderItemEntity saved");
		}

	}
	
	public void confirm(){
		System.out.format("[OrderItem] confirming Order invoked : orderStatus[%d:%d] \n", this.orderStatus,OrderUtil.ORDER_OPEN);
		if(this.orderStatus == OrderUtil.ORDER_OPEN){
			this.orderStatus = OrderUtil.ORDER_CONFIRMED;
			System.out.format("[OrderItem] applying new OrderItemConfirmedEvent orderId[%d], customerId[%d] \n", this.orderId,this.customerId);
			apply(new OrderItemConfirmedEvent(orderId, customerId));
		}
	}
	@EventHandler
    public void handleOrderItemConfirmedEvent(OrderItemConfirmedEvent evt){
		System.out.println("[OrderItem] handleOrderItemConfirmedEvent eventHandler invoked : ");
		if(this.service != null){
			System.out.format("[OrderItem] confirming OrderItemEntity: customerId[%d] , orderId[%s] \n",evt.getCustomerId(),evt.getOrderId());
			this.service.handleOrderItemConfirmedEvent(evt);
			System.out.println("[OrderItem] confirmed OrderItemEntity saved");
		}

	}
	
	
	public void cancel(){
		if(this.orderStatus != OrderUtil.ORDER_CANCELLED){
			this.orderStatus = OrderUtil.ORDER_CANCELLED;
			System.out.println("[OrderItem] applying OrderItemCancelledEvent");
			apply(new OrderItemCancelledEvent(orderId, customerId));
		}
	}
	@EventHandler
    public void handleOrderItemCancelledEvent(OrderItemCancelledEvent evt){
		System.out.println("[OrderItem] handleOrderItemCancelledEvent eventHandler invoked : ");
		if(this.service != null){
			System.out.format("[OrderItem] cancelling OrderItemEntity: customerId[%d] , orderId[%s] \n",evt.getCustomerId(),evt.getOrderId());
			this.service.handleOrderItemCancelledEvent(evt);
			System.out.println("[OrderItem] cancelled OrderItemEntity saved");
		}

	}
	
	public void complete(){
		if(this.orderStatus == OrderUtil.ORDER_CONFIRMED){
			this.orderStatus = OrderUtil.ORDER_COMPLETED;
			System.out.println("[OrderItem] applying OrderItemCompletedEvent");
			apply(new OrderItemCompletedEvent(orderId, customerId));
		}
		
	}

	@EventHandler
    public void handleOrderItemCompletedEvent(OrderItemCompletedEvent evt){
		System.out.println("[OrderItem] handleOrderItemCompletedEvent eventHandler invoked : ");
		if(this.service != null){
			System.out.format("[OrderItem] completing OrderItemEntity: customerId[%d] , orderId[%s] \n",evt.getCustomerId(),evt.getOrderId());
			this.service.handleOrderItemCompletedEvent(evt);
			System.out.println("[OrderItem] completed OrderItemEntity saved");
		}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String[] getProductId() {
		return productId;
	}

	public void setProductId(String[] productId) {
		this.productId = productId;
	}

	public long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(long totalPrice) {
		this.totalPrice = totalPrice;
	}

}
