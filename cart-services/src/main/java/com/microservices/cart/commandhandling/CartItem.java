package com.microservices.cart.commandhandling;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.microservices.cart.api.CartItemCheckOutEvent;
import com.microservices.cart.api.CartItemCreatedEvent;
import com.microservices.cart.api.CartItemRemovedEvent;
import com.microservices.cart.api.CartItemUpdateQuantityEvent;
import com.microservices.cart.query.CartUtil;
import com.microservices.cart.repo.CartItemRepositoryService;

public class CartItem extends AbstractAnnotatedAggregateRoot<Object>{
	
	@AggregateIdentifier
	private long cartItemId;
	private long cartId;
	private static final long serialVersionUID=-1527435769321421602L;
	private long productId;
	private long quantity;
	private long price;
	
	private String checkoutStatus;
	
	@Autowired
	private CartItemRepositoryService service;
	
	public CartItem(){
		System.out.println("[CartItem] cartItem constructor #1 called ");
	}

	public CartItem(long cartId,long productId, long quantity, long price,CartItemRepositoryService service) {
		System.out.println("[CartItem] cartItem constructor #2 called ");
		this.cartId = cartId;
        this.productId = productId;
        this.cartItemId = CartUtil.getCartIdentifier(cartId, productId);
        this.quantity = quantity;
        this.price = price;
        this.setService(service);
        this.setCheckoutStatus(CartUtil.CART_OPEN);
    }
	
		
	public void create(){
		apply(new CartItemCreatedEvent(
					this.cartId, 
					this.productId, 
					this.quantity,
					this.price,this.checkoutStatus));
	}	
	
	@EventHandler
	public void handleCartItemCreatedEvent(CartItemCreatedEvent evt){
		System.out.println("[CartItem] handleCartItemCreatedEvent eventHandler invoked : ");
		this.cartId = evt.getCartId();
		this.cartItemId = evt.getCartItemIdentifier();
		this.checkoutStatus = evt.getCheckoutStatus();
		if(this.service != null) {
			System.out.format("[CartItem] create & save new CartItemEntity: \n");
			this.service.handleCartItemCreatedEvent(evt);
			System.out.println("[CartItem] new CartItemEntity saved");
		}
	}
	
	public void remove(){
		if(this.checkoutStatus.equals(CartUtil.CART_OPEN)){
			apply (new CartItemRemovedEvent(this.cartId, productId, quantity));
		}
	}
	@EventHandler
	public void handleCartItemRemovedEvent(CartItemRemovedEvent evt){
		System.out.println("[CartItem] handleCartItemRemovedEvent eventHandler invoked : ");
		if(this.service != null) {
			System.out.format("[CartItem] removing CartItemEntity: \n");
			this.service.handleCartItemRemovedEvent(evt);
			System.out.format("[CartItem] new CartItemEntity removed");
		}
	
	}

	public void updateQuantity(long quantity){
		if(this.checkoutStatus.equals(CartUtil.CART_OPEN)){
			apply (new CartItemUpdateQuantityEvent(this.cartId, productId, quantity));
		}
	}
	
	@EventHandler
	public void handleCartItemUpdateQuantityEvent(CartItemUpdateQuantityEvent evt){
		System.out.println("[CartItem] handleCartItemUpdateQuantityEvent eventHandler invoked : ");
		if(this.service != null) {
			System.out.format("[CartItem] updating quantity CartItemEntity: \n");
			this.service.handleCartItemUpdateQuantityEvent(evt);
			System.out.format("[CartItem] new CartItemEntity saved");
		}
	
	}
	
	public void checkout(){
		System.out.format("[CartItem] checkoutStatus of CartItemEntity: \n status=[%s], service[%s]\n",this.checkoutStatus, this.service);
		if(this.checkoutStatus.equals(CartUtil.CART_OPEN)){
			this.checkoutStatus = CartUtil.CART_CHECKEDOUT;
			apply (new CartItemCheckOutEvent(this.cartItemId));
		}
		
	}
	
	@EventSourcingHandler
	public void handleCartItemCheckOutEvent(CartItemCheckOutEvent evt){
		System.out.println("[CartItem] handleCartItemCheckOutEvent eventHandler invoked : ");
		if(this.service != null) {
			System.out.format("[CartItem] checkout CartItemEntity: service[%s]\n",this.service);
			this.service.handleCartItemCheckOutEvent(evt);
			System.out.format("[CartItem] all CartItemEntity checked out");
		}
	}
	
	public long getProductId() {
        return productId;
    }
    
    public void setProduct(long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long l) {
        this.quantity = l;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        quantity--;
    }


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	
	
	public long getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(long cartItemId) {
		this.cartItemId = cartItemId;
	}

	public String getCheckoutStatus() {
		return checkoutStatus;
	}

	public void setCheckoutStatus(String checkoutStatus) {
		this.checkoutStatus = checkoutStatus;
	}

	public CartItemRepositoryService getService() {
		return service;
	}

	public void setService(CartItemRepositoryService service) {
		this.service = service;
	}

}
