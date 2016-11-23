package com.microservices.order.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservices.order.query.OrderItemEntity;

public interface OrderItemRepository extends MongoRepository<OrderItemEntity, String> {
	
	public List<OrderItemEntity> findByOrderId(long orderId);
	public List<OrderItemEntity> findByCustomerId(long customerId);

}
