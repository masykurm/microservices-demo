package com.microservices.order.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.order.api.OrderItemCancelledEvent;
import com.microservices.order.api.OrderItemCompletedEvent;
import com.microservices.order.api.OrderItemConfirmedEvent;
import com.microservices.order.api.OrderItemCreatedEvent;
import com.microservices.order.query.OrderItemEntity;
import com.microservices.order.query.OrderUtil;

@Service
public class OrderItemRepositoryService {

	@Autowired
	private OrderItemRepository repository;
	
	public OrderItemRepositoryService(){
		
	}
	public OrderItemRepositoryService( OrderItemRepository repository){
		this.repository = repository;
	}
	
	public void handleOrderItemCreatedEvent(OrderItemCreatedEvent evt){
			System.out.println("[OrderItemRepositoryService] handleOrderItemCreatedEvent eventHandler invoked : ");
			System.out.format("[OrderItemRepositoryService] create & save new OrderItemEntity: customerId[%d] , orderId[%s], product[%s], repository [%s]\n",evt.getCustomerId(),evt.getOrderId(), evt.getProductId(),repository);
			try {
				OrderItemEntity orderItemEntity = new OrderItemEntity(evt.getCustomerId(),evt.getOrderId(),evt.getProductId());
				System.out.format("[OrderItemRepositoryService] saving new OrderItemEntity: customerId[%d] , orderId[%s], product[%s], repository [%s]\n",evt.getCustomerId(),evt.getOrderId(), evt.getProductId(),repository);
				repository.save(orderItemEntity);
			} catch(Exception e){
				e.printStackTrace();
			}
			System.out.format("[OrderItemRepositoryService] new OrderItemEntity saved");

   }
	
	public void handleOrderItemConfirmedEvent(OrderItemConfirmedEvent evt){
		System.out.println("[OrderItemRepositoryService] handleOrderItemConfirmedEvent eventHandler invoked : ");
		List<OrderItemEntity> orderItemEntity = repository.findByOrderId(evt.getOrderId());
		for(OrderItemEntity e: orderItemEntity){
			if(e.getOrderStatus() == OrderUtil.ORDER_OPEN){
				System.out.format("[OrderItemRepositoryService] confirming OrderItemEntity: customerId[%d] , orderId[%s] \n",evt.getCustomerId(),evt.getOrderId());
				e.setOrderStatus(OrderUtil.ORDER_CONFIRMED);
				repository.save(e);
				System.out.format("[OrderItemRepositoryService] new OrderItemEntity saved");
			}
		}
	}
	
	public void handleOrderItemCancelledEvent(OrderItemCancelledEvent evt){
		System.out.println("[OrderItemRepositoryService] handleOrderItemCancelledEvent eventHandler invoked : ");
		List<OrderItemEntity> orderItemEntity = repository.findByOrderId(evt.getOrderId());
		for(OrderItemEntity e: orderItemEntity){
			if(e.getOrderStatus() != OrderUtil.ORDER_CANCELLED){
				System.out.format("[OrderItemRepositoryService] cancelling OrderItemEntity: customerId[%d] , orderId[%s] \n",evt.getCustomerId(),evt.getOrderId());
				e.setOrderStatus(OrderUtil.ORDER_CANCELLED);
				repository.save(e);
				System.out.format("[OrderItemRepositoryService] updated OrderItemEntity saved");
			}
		}
	}
	
	
	public void handleOrderItemCompletedEvent(OrderItemCompletedEvent evt){
		System.out.println("[OrderItemRepositoryService] handleOrderItemCompletedEvent eventHandler invoked : ");
		List<OrderItemEntity> orderItemEntity = repository.findByOrderId(evt.getOrderId());
		for(OrderItemEntity e: orderItemEntity){
			if(e.getOrderStatus() == OrderUtil.ORDER_CONFIRMED){
				System.out.format("[OrderItemEventHandler] cancelling OrderItemEntity: customerId[%d] , orderId[%s] \n",evt.getCustomerId(),evt.getOrderId());
				e.setOrderStatus(OrderUtil.ORDER_CANCELLED);
				repository.save(e);
				System.out.format("[OrderItemEventHandler] updated OrderItemEntity saved");
			}
		}
	}
	public OrderItemRepository getRepository() {
		return repository;
	}
	public void setRepository(OrderItemRepository repository) {
		this.repository = repository;
	}
}
