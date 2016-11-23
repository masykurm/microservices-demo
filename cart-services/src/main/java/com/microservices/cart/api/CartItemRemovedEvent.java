package com.microservices.cart.api;

import com.microservices.cart.query.CartUtil;

public class CartItemRemovedEvent {
	private final long cartItemIdentifier; 
	private final long cartId;
	private final long productId;
	private final long quantity;
	
	public CartItemRemovedEvent(long cartId, long productId, long quantity) {
		// TODO Auto-generated constructor stub
		System.out.println("[CartItemRemoveCommand] Command created");
		this.cartId= cartId;
		this.productId= productId;
		this.quantity = quantity;
		this.cartItemIdentifier = CartUtil.getCartIdentifier(this.cartId, this.productId);
	
	}

	public long getCartId() {
		return cartId;
	}

	public long getQuantity() {
		return quantity;
	}

	public long getCartItemIdentifier() {
		return cartItemIdentifier;
	}

	public long getProductId() {
		return productId;
	}
}
