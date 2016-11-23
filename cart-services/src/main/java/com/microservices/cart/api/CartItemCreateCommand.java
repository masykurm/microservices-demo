package com.microservices.cart.api;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import com.microservices.cart.query.CartUtil;

public class CartItemCreateCommand {

		@TargetAggregateIdentifier
		private final long cartItemIdentifier; 
		private final long cartId;
		private final long productId;
		private final long quantity;
		private final long price;
		private final String checkoutStatus;
		
		public CartItemCreateCommand(long cartId, long productId, long quantity, long price,String checkoutStatus) {
			// TODO Auto-generated constructor stub
			System.out.println("[CartItemCreateCommand] Command created");
			this.cartId= cartId;
			this.productId= productId;
			this.quantity = quantity;
			this.price = price;
			this.cartItemIdentifier = CartUtil.getCartIdentifier(this.cartId, this.productId);
			this.checkoutStatus = checkoutStatus;
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

		public long getPrice() {
			return price;
		}

		public String getCheckoutStatus() {
			return checkoutStatus;
		}

		
}
