package com.microservices.cart.query;


import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CartItemEntity {
	@Id
	private String cartItemId;
	private String cartId;
	private String productId;
	private String quantity;
	private String price;
	private String checkOutStatus;
	
	
	public CartItemEntity(){
		this(0,0,0,0);
	}

	public CartItemEntity(long cartId,long productId, long quantity, long price) {
		this.cartId = String.valueOf(cartId);
        this.cartItemId = String.valueOf(CartUtil.getCartIdentifier(cartId, productId));
        this.productId = String.valueOf(productId);
        this.quantity = String.valueOf(quantity);
        this.price = String.valueOf(price);
        this.setCheckOutStatus(CartUtil.CART_OPEN);
    }

	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCheckOutStatus() {
		return checkOutStatus;
	}

	public void setCheckOutStatus(String checkOutStatus) {
		this.checkOutStatus = checkOutStatus;
	}


	
}
