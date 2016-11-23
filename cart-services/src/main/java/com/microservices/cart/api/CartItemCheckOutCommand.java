package com.microservices.cart.api;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class CartItemCheckOutCommand {
	@TargetAggregateIdentifier
	private final long cartItemIdentifier;
	
	public CartItemCheckOutCommand(long cartItemIdentifier) {
		// TODO Auto-generated constructor stub
		System.out.println("[CartItemCheckOutCommand] Command created");
		this.cartItemIdentifier = cartItemIdentifier;	
	}

	

	public long getCartItemIdentifier() {
		return cartItemIdentifier;
	}

}
