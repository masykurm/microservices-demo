package com.microservices.cart.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.cart.api.CartItemCheckOutEvent;
import com.microservices.cart.api.CartItemCreatedEvent;
import com.microservices.cart.api.CartItemRemovedEvent;
import com.microservices.cart.api.CartItemUpdateQuantityEvent;
import com.microservices.cart.query.CartItemEntity;
import com.microservices.cart.query.CartUtil;

@Service
public class CartItemRepositoryService {
	private CartItemRepository repository;


	@Autowired
	public CartItemRepositoryService(CartItemRepository repository){
		this.repository = repository;
	}
	public CartItemRepository getRepository() {
		return repository;
	}
	
	public void setRepository(CartItemRepository repository) {
		this.repository = repository;
	}
	
	/***
	 * Function : EventHandler service to handle CartItemCreatedEvent 
	 **/
	public void handleCartItemCreatedEvent(CartItemCreatedEvent evt){
		
		long cartId = evt.getCartId();
		long productId= evt.getProductId();
		long quantity = evt.getQuantity();
		long price = evt.getPrice();
		long cartItemIdentifier = evt.getCartItemIdentifier();
		System.out.format ("[CartItemRepositoryService] saving cartItem : ID=[%d], Product ID=[%d], Quantity=[%d] \n",cartId, productId, quantity);
		try {

		
		//TODO: Find if there exists CartItem stored in the Mongo based on cartItemId;
		System.out.println("[CartItemRepositoryService] finding cartItemEntity in repository: "+repository.count());
		List<CartItemEntity> cartItemList = repository.findByCartItemId(String.valueOf(cartItemIdentifier));
		System.out.println("[CartItemRepositoryService] finding cartItemEntity result: "+cartItemList);
		//TODO: If no cart Item found , then create new one
		if(cartItemList == null || cartItemList.isEmpty()){
			System.out.println("[CartItemRepositoryService] Creating new cartItemEntity ");
			CartItemEntity cartItemEntity = new CartItemEntity(
												cartId,
												productId,
												quantity,
												price);

			cartItemEntity.setCheckOutStatus(CartUtil.CART_OPEN);
			//TODO: Save cart item to the repository
			System.out.format("[CartItemRepositoryService] Saving cartItemEntity : cartId[%s], productId[%s], quantity[%s], price[%s], status[%s]\n",
							cartItemEntity.getCartId(),
							cartItemEntity.getProductId(),
							cartItemEntity.getQuantity(),
							cartItemEntity.getPrice(),
							cartItemEntity.getCheckOutStatus());
			repository.save(cartItemEntity);
			System.out.println("[CartItemRepositoryService] cartItemEntity in mongo now "+repository.count());
		} else {
			//TODO: If cart Item found , update the quantity
			System.out.println("[CartItemRepositoryService] updating quantity of cartItemEntity in mongo : "+cartItemList.size());
			for (CartItemEntity c : cartItemList){
				System.out.format("[CartItemRepositoryService] iterating cartItemEntity in mongo : cartId[%s], cartItemId[%s], productId[%s], quantity[%s] \n",c.getCartId(),c.getCartItemId(),c.getProductId(),c.getQuantity());
				if(c.getCartItemId().equals(String.valueOf(cartItemIdentifier))){
					//TODO: If the item found with cart status=checkout, then use current quantity, else update the quantity
					if(c.getCheckOutStatus().equals(CartUtil.CART_OPEN)){
						String oldQuantity = c.getQuantity();
						long newQuantity = Long.valueOf(c.getQuantity())+quantity;
						System.out.format("[CartItemRepositoryService] found same cartItemEntity updating quantity from [%s] to [%d] \n",c.getQuantity(),newQuantity);
						c.setQuantity(String.valueOf(newQuantity));
						repository.delete(c);
						repository.save(c);
						System.out.format("[CartItemRepositoryService] done updating quantity from [%s] to [%d] \n", oldQuantity, newQuantity);
					} else {
						System.out.format("[CartItemRepositoryService] found checkedout cart, saving new CartItemEntity \n");
						CartItemEntity cartItemEntity = new CartItemEntity(
								cartId,
								productId,
								quantity,
								price);
						repository.save(cartItemEntity);
						System.out.println("[CartItemRepositoryService] cartItemEntity in mongo now "+repository.count());
					}
					break;
				} 
			}
		}
		System.out.println("saveCartItem operation completed:");
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	/***
	 * Function : EventHandler service to handle CartItemCheckOutEvent 
	 **/
	public void handleCartItemCheckOutEvent(CartItemCheckOutEvent evt){
		long cartItemIdentifier = evt.getCartItemIdentifier();
		List<CartItemEntity> cartItemList = repository.findByCartItemId(String.valueOf(cartItemIdentifier));
		if(cartItemList == null || cartItemList.isEmpty()){
			System.out.println("[CartItemRepositoryService] no cartItem found");
		} else {
			for (CartItemEntity c: cartItemList){
				if(c.getCartItemId().equals(String.valueOf(cartItemIdentifier)) &&
				   c.getCheckOutStatus().equals(CartUtil.CART_OPEN)){
							String oldStatus = c.getCheckOutStatus();
							System.out.format("[CartItemRepositoryService] found same cartItemEntity updating checkout status from [%s] to [%s] \n",c.getCheckOutStatus(),CartUtil.CART_CHECKEDOUT);
							c.setCheckOutStatus(CartUtil.CART_CHECKEDOUT);
							repository.delete(c);
							repository.save(c);
							System.out.format("[CartItemRepositoryService] done updating checkoutstatus from [%s] to [%s] \n", oldStatus, CartUtil.CART_CHECKEDOUT);
				}
			}
		}
	}
	
	/***
	 * Function : EventHandler service to handle CartItemRemovedEvent 
	 **/
	public void handleCartItemRemovedEvent (CartItemRemovedEvent evt){
		long cartId  = evt.getCartId(); 
		long productId = evt.getProductId();
		long cartItemIdentifier  = CartUtil.getCartIdentifier(cartId, productId);
		long quantity = evt.getQuantity();
		System.out.format ("[CartItemRepositoryService] removing cartItem : ID=[%d], Product ID=[%d], Quantity=[%d] \n",cartId, productId, quantity);
		try {

		
		//TODO: Find if there exists CartItem stored in the Mongo based on cartItemId;
		System.out.println("[CartItemRepositoryService] finding cartItemEntity in repository: "+repository.count());
		List<CartItemEntity> cartItemList = repository.findByCartItemId(String.valueOf(cartItemIdentifier));
		System.out.println("[CartItemRepositoryService] finding cartItemEntity result: "+cartItemList);
		//TODO: If no cart Item found , then create new one
		if(cartItemList == null || cartItemList.isEmpty()){
			System.out.println("[CartItemRepositoryService] no cartItem found");
		} else {
			//TODO: If cart Item found , update the quantity
			System.out.println("[CartItemRepositoryService] removing cartItemEntity in mongo : "+cartItemList.size());
			for (CartItemEntity c : cartItemList){
				System.out.format("[CartItemRepositoryService] iterating cartItemEntity in mongo : cartId[%s], cartItemId[%s], productId[%s], quantity[%s] \n",c.getCartId(),c.getCartItemId(),c.getProductId(),c.getQuantity());
				if(c.getCartItemId().equals(String.valueOf(cartItemIdentifier))&&
						c.getCheckOutStatus().equals(CartUtil.CART_OPEN)){
					
					System.out.format("[CartItemRepositoryService] found cartItemEntity with identifier [%s]\n ",c.getCartItemId());
					System.out.format("[CartItemRepositoryService] removing from mongo \n ");
					repository.delete(c);
					System.out.format("[CartItemRepositoryService] succesfully removing from mongo. \n");
					break;
				}
			}
		}
		System.out.println("removeCartItem operation completed:");
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	/***
	 * Function : EventHandler service to handle CartItemUpdateQuantityEvent 
	 **/
	public void handleCartItemUpdateQuantityEvent(CartItemUpdateQuantityEvent evt){
		
		System.out.format ("[CartItemRepositoryService] saving cartItem : ID=[%d], Product ID=[%d], Quantity=[%d] \n",evt.getCartId(), evt.getProductId(), evt.getQuantity());
		try {

		long cartItemIdentifier = CartUtil.getCartIdentifier(evt.getCartId(), evt.getProductId());
		//TODO: Find if there exists CartItem stored in the Mongo based on cartItemId;
		System.out.println("[CartItemRepositoryService] finding cartItemEntity in repository: "+repository.count());
		List<CartItemEntity> cartItemList = repository.findByCartItemId(String.valueOf(cartItemIdentifier));
		System.out.println("[CartItemRepositoryService] finding cartItemEntity result: "+cartItemList);
		//TODO: If no cart Item found , then create new one
		if(cartItemList == null || cartItemList.isEmpty()){
			System.out.println("[CartItemRepositoryService] no cartItem found");
		} else {
			//TODO: If cart Item found , update the quantity
			System.out.println("[CartItemRepositoryService] updating quantity of cartItemEntity in mongo : "+cartItemList.size());
			for (CartItemEntity c : cartItemList){
				System.out.format("[CartItemRepositoryService] iterating cartItemEntity in mongo : cartId[%s], cartItemId[%s], productId[%s], quantity[%s] \n",c.getCartId(),c.getCartItemId(),c.getProductId(),c.getQuantity());
				if(c.getCartItemId().equals(String.valueOf(cartItemIdentifier)) &&
						c.getCheckOutStatus().equals(CartUtil.CART_OPEN)){
					String oldQuantity = c.getQuantity();
					System.out.format("[CartItemRepositoryService] found same cartItemEntity updating quantity from [%s] to [%d] \n",c.getQuantity(),evt.getQuantity());
					c.setQuantity(String.valueOf(evt.getQuantity()));
					repository.delete(c);
					repository.save(c);
					System.out.format("[CartItemRepositoryService] done updating quantity from [%s] to [%d] \n", oldQuantity, evt.getQuantity());
					break;
				}
			}
		}
		System.out.println("updateCartItemQuantity operation completed:");
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	
	
	
}
