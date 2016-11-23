package com.microservices.cart.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservices.cart.query.CartItemEntity;



//@RepositoryRestResource(collectionResourceRel = "cart", path = "api/cart/retrieve")
public interface CartItemRepository extends MongoRepository<CartItemEntity, String>{
	
	public List<CartItemEntity> findByCartId(String cartId);
	public List<CartItemEntity> findByCartItemId(String cartItemId);
	

}
