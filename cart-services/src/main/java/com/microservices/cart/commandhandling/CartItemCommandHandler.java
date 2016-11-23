package com.microservices.cart.commandhandling;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;

import com.microservices.cart.api.CartItemCheckOutCommand;
import com.microservices.cart.api.CartItemCreateCommand;
import com.microservices.cart.api.CartItemRemoveCommand;
import com.microservices.cart.api.CartItemUpdateQuantityCommand;
import com.microservices.cart.repo.CartItemRepository;
import com.microservices.cart.repo.CartItemRepositoryService;

public class CartItemCommandHandler {
	private Repository<CartItem> repository;
	private CartItemRepositoryService repositoryService;
	
	public CartItemCommandHandler(Repository<CartItem> repository, CartItemRepository cartRepository){
		this.repository = repository;
		this.repositoryService = new CartItemRepositoryService(cartRepository);
	}
	
	@CommandHandler
	public void createCart(CartItemCreateCommand command){
		System.out.println("[CartItemCommandHandler] createCart commandHandler invoked");
		System.out.println("[CartItemCommandHandler] saving new CartItem in the Event Repository");
		CartItem ci = new CartItem(
							command.getCartId(),	
							command.getProductId(),
							command.getQuantity(),
							command.getPrice(),this.repositoryService);
		repository.add(ci);
		ci.create();
		
	}
	@CommandHandler
	public void removeCart(CartItemRemoveCommand command){
		System.out.println("[CartItemCommandHandler] removeCart commandHandler invoked");
		System.out.println("[CartItemCommandHandler] load CartItem from the Event Repository");
		CartItem cartItem = repository.load(command.getCartItemIdentifier());
		System.out.format("[CartItemCommandHandler] removing CartItem in the Event Repository : cartItem[%s] ,cartItemIdentifier[%s] \n", cartItem, command.getCartItemIdentifier());
		cartItem.remove();
		//repository.(orderItem);
		System.out.println("[CartItemCommandHandler] cartItem removed");
	}
	
	@CommandHandler
	public void updateQuantity(CartItemUpdateQuantityCommand command){
		System.out.println("[CartItemCommandHandler] updateQuantity commandHandler invoked");
		System.out.println("[CartItemCommandHandler] load CartItem from the Event Repository");
		CartItem cartItem = repository.load(command.getCartItemIdentifier());
		System.out.format("[CartItemCommandHandler] updating quantity CartItem in the Event Repository : cartItem[%s] ,cartItemIdentifier[%s] \n", cartItem, command.getCartItemIdentifier());
		cartItem.setService(repositoryService);
		cartItem.updateQuantity(command.getQuantity());
		System.out.println("[CartItemCommandHandler] cartItem removed");
	}
	
	@CommandHandler
	public void checkOutCart(CartItemCheckOutCommand command){
		try {
			System.out.println("[CartItemCommandHandler] checkOutCart commandHandler invoked");
			System.out.format("[CartItemCommandHandler] load CartItem from the Event Repository : cartIdentifier[%d], repository [%s],repositoryService [%s] \n",command.getCartItemIdentifier(),repository,repositoryService);
			CartItem cartItem = repository.load(command.getCartItemIdentifier());
			System.out.format("[CartItemCommandHandler] checking out CartItem in the Event Repository : cartItem[%s] ,cartItemIdentifier[%s:%s], cartItemStatus[%s] \n", cartItem, cartItem.getCartItemId(),command.getCartItemIdentifier(), cartItem.getCheckoutStatus());
			cartItem.setService(repositoryService);
			cartItem.checkout();
			System.out.println("[CartItemCommandHandler] cartItem checked out");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public Repository<CartItem> getRepository() {
		return repository;
	}

	public void setRepository(Repository<CartItem> repository) {
		this.repository = repository;
	}

	public CartItemRepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(CartItemRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
}
