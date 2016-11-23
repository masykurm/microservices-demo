package com.microservices.cart.api;

public class CartItemCheckOutEvent {
	
	private final long cartItemIdentifier;
	
	public CartItemCheckOutEvent(long cartItemIdentifier) {
		// TODO Auto-generated constructor stub
		System.out.println("[CartItemCheckOutCommand] Command created");
		this.cartItemIdentifier = cartItemIdentifier;
	}


	public long getCartItemIdentifier() {
		return cartItemIdentifier;
	}
	
}
