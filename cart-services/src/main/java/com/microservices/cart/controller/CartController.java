package com.microservices.cart.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerAdapter;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.cart.api.CartItemCheckOutCommand;
import com.microservices.cart.api.CartItemCreateCommand;
import com.microservices.cart.api.CartItemRemoveCommand;
import com.microservices.cart.api.CartItemUpdateQuantityCommand;
import com.microservices.cart.commandhandling.CartItem;
import com.microservices.cart.commandhandling.CartItemCommandHandler;
import com.microservices.cart.query.CartItemEntity;
import com.microservices.cart.query.CartUtil;
import com.microservices.cart.repo.CartItemRepository;




@RestController
public class CartController {
	
	private CommandBus commandBus;
	private CommandGateway commandGateway;
	private EventStore eventStore;
//	private CartItemEventStore cartItemEventStore;
	private EventSourcingRepository<CartItem> repository;
	private EventBus eventBus;
	@Autowired
	private CartItemRepository cirepo;
	private CartItemCommandHandler cartCommandHandler;
	

	public CartController(){

		commandBus = new SimpleCommandBus();
		commandGateway  = new DefaultCommandGateway(commandBus);
		System.out.format("Initializing cartItemEventStore \n");
//		this.cartItemEventStore = new CartItemEventStore();
//		this.eventStore =  this.cartItemEventStore.getEventStore();
		eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("./events")));
		eventBus = new SimpleEventBus();
		repository = new EventSourcingRepository<CartItem>(CartItem.class, eventStore);
		repository.setEventBus(eventBus);
		System.out.format("Repository & cirepo : [%s],[%s] \n",repository,cirepo);
		cartCommandHandler = new CartItemCommandHandler(repository,cirepo);
		
		//Register the commandHandler
		AnnotationCommandHandlerAdapter.subscribe(cartCommandHandler, commandBus);
//		AggregateAnnotationCommandHandler.subscribe(CartItem.class, this.repository,commandBus);
		
	}
	
	/**
	 * Function : Create new cart / add new item in customer cart
	 * Input : customerId, productId, quantity, price
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Send CartItemCreateCommand
	 *   - Send response with Response status HTTP OK
	 * 
	 * Example: http://localhost:8080/api/cart/add?customerId=1&prodId=1&quantity=1&price=120000 
	 **/
	//TODO: Develop addtoCart function here
	@RequestMapping(
			value ="/api/cart/add",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> addtoCart(
			@RequestParam(value="customerId", defaultValue="103") String customerId,
			@RequestParam(value="prodId", defaultValue="1119922226") String productId,
			@RequestParam(value="quantity", defaultValue="1") String qty,
			@RequestParam(value="price", defaultValue="1") String prices){
				
		System.out.println("[CartController]  Processing add item to cart !!");
		//TODO: Capture request parameter : cartId, product id, product Name, product short description, quantity
		long cartId= Long.valueOf(customerId).longValue();
		long prodId= Long.valueOf(productId).longValue();
		long quantity = Long.valueOf(qty).longValue();
		int price = Integer.valueOf(prices).intValue();
				
		cartCommandHandler.getRepositoryService().setRepository(cirepo);
		commandGateway.send(new CartItemCreateCommand(cartId, prodId, quantity, price, CartUtil.CART_OPEN));
		
		System.out.format("[CartController] cartItem repository [%s] , size [%d]", cirepo, cirepo.count());
		
		String asyncResponseVal = String.format("Processing action CartItemCreateCommand: customerId[%s], cartId[%d], productId[%d] , quantity [%d], price[%d]", customerId,cartId,prodId,quantity, price );
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Remove product from customer cart
	 * Input : customerId, productId, quantity
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Send CartItemRemoveCommand
	 *   - Send response with Response status HTTP OK
	 * 
	 * Example: http://localhost:8080/api/cart/remove?customerId=1&prodId=1&quantity=1 
	 **/
	//TODO: Develop removeFromCart function here
	@RequestMapping(
			value ="/api/cart/remove",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> removeFromCart(
			@RequestParam(value="customerId", defaultValue="103") String customerId,
			@RequestParam(value="prodId", defaultValue="1119922226") String productId,
			@RequestParam(value="quantity", defaultValue="1") String qty){
				
		System.out.println("[CartController]  Processing remove item from cart !!");
		//TODO: Capture request parameter : cartId, product id, product Name, product short description, quantity
		long cartId= Long.valueOf(customerId).longValue();
		long prodId= Long.valueOf(productId).longValue();
		long quantity = Long.valueOf(qty).longValue();
		
		
		cartCommandHandler.getRepositoryService().setRepository(cirepo);
		commandGateway.send(new CartItemRemoveCommand(cartId, prodId, quantity));
		System.out.format("[CartController] cartItem repository [%s] , size [%d]", cirepo, cirepo.count());
		
		String asyncResponseVal = String.format("Processing action CartItemRemoveCommand: customerId[%s], cartId[%d], productId[%d] , quantity [%d]", customerId,cartId,prodId,quantity);
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Update product quantity in customer cart
	 * Input : customerId, productId, quantity
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Send CartItemRemoveCommand
	 *   - Send response with Response status HTTP OK
	 * Example: http://localhost:8080/api/cart/updateQty?customerId=1&prodId=1&quantity=1
	 **/
	//TODO: Develop updateCartQuantity in here
	@RequestMapping(
			value ="/api/cart/updateQty",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateCartQuantity(
			@RequestParam(value="customerId", defaultValue="103") String customerId,
			@RequestParam(value="prodId", defaultValue="1119922226") String productId,
			@RequestParam(value="quantity", defaultValue="1") String qty){
				
		System.out.println("[CartController]  Processing update item from cart !!");
		//TODO: Capture request parameter : cartId, product id, product Name, product short description, quantity
		long cartId= Long.valueOf(customerId).longValue();
		long prodId= Long.valueOf(productId).longValue();
		long quantity = Long.valueOf(qty).longValue();
		
		cartCommandHandler.getRepositoryService().setRepository(cirepo);
		commandGateway.send(new CartItemUpdateQuantityCommand(cartId, prodId, quantity));
		
		System.out.format("[CartController] cartItem repository [%s] , size [%d]", cirepo, cirepo.count());
		
		String asyncResponseVal = String.format("Processing action CartItemUpdateQuantityCommand : customerId[%s], cartId[%d], productId[%d] , quantity [%d]", customerId,cartId,prodId,quantity);
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Checkout active cart
	 * Input : customerId
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - 
	 *   - 
	 *   - 
	 * Example: http://localhost:8080/api/cart/checkout?customerId=1
	 **/
	//TODO: Develop checkOutCart function here
	@RequestMapping(
			value ="/api/cart/checkout",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> checkOutCart(
			@RequestParam(value="customerId", defaultValue="103") String customerId){
		
		
		//TODO: Checkout customer cart in here
		System.out.format("[CartController] finding out customer Cart: customerId[%s] , cartId[%s]  \n", customerId, customerId);
		List<CartItemEntity> customerCart = cirepo.findByCartId(customerId);
		cartCommandHandler.getRepositoryService().setRepository(cirepo);
		for(CartItemEntity c: customerCart){
			if (c.getCheckOutStatus().equals(CartUtil.CART_OPEN)){
				long cartItemIdentifier = Long.valueOf(c.getCartItemId());//CartUtil.getCartIdentifier(Long.valueOf(c.getCartId()),Long.valueOf(c.getProductId()));
				commandGateway.send(new CartItemCheckOutCommand(cartItemIdentifier));
				
			}
		}
		
		String asyncResponseVal = String.format("Processing action CartItemCheckOutCommand: customerId[%s], ", customerId);
		Response re = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(re, HttpStatus.OK);
	}
	
	/**
	 * Function : Clear customer cart - this is for internal testing only
	 * Input : customerId
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - 
	 *   - 
	 *   - 
	 * Example: http://localhost:8080/api/cart/clearCart
	 **/
	//TODO:  Retrieve Customer's cart with Checkout Status = OPEN
	@RequestMapping(
			value ="/api/cart/clearCart",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> clearAllCart(
			@RequestParam(value="customerId", defaultValue="103") String customerId){
		

		cirepo.deleteAll();
		String asyncResponseVal = String.format("Processing action clear cart: size=[%d] \n",cirepo.count());
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Retrieve products with status=OPEN from customer cart
	 * Input : customerId
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - 
	 *   - 
	 *   - 
	 * Example: http://localhost:8080/api/cart/retrieve?customerId=1
	 **/
	//TODO: Develop retrieveCart function in here 
	@RequestMapping(
			value ="/api/cart/retrieve",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> retrieveCart(
			@RequestParam(value="customerId", defaultValue="103") String customerId){
		
		System.out.format("[CartController] finding out customer Cart: customerId[%s] , cartId[%s], repoSize[%d] \n", customerId, customerId, cirepo.count());
		List<CartItemEntity> customerCart = cirepo.findByCartId(customerId);
		
		System.out.format("[CartController] found out customer Cart: cartObject[%s] \n", customerCart);
		List<CartItemEntity> response = new ArrayList<CartItemEntity>();
		for(CartItemEntity c: customerCart){
			System.out.format("[CartController] CartItemEntiy: status[%s] \n", c.getCheckOutStatus());
			if (c.getCheckOutStatus().equals(CartUtil.CART_OPEN)){
					response.add(c);
			}
		}
		
		//TODO: Retrieve customer's cart from the database
		System.out.format("[CartController] response build: entities[%s], size[%d] \n ", customerCart, customerCart.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Retrieve products with all statuses from customer cart
	 * Input : customerId
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - 
	 *   - 
	 *   - 
	 * Example: http://localhost:8080/api/cart/retrieveAll?customerId=1
	 **/
	//TODO: Develop retrieveAllCart function in here 
	@RequestMapping(
			value ="/api/cart/retrieveAll",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> retrieveAllCart(
			@RequestParam(value="customerId", defaultValue="103") String customerId){
		
		System.out.format("[CartController] finding out customer Cart: customerId[%s] , cartId[%s], repoSize[%d]  \n", customerId, customerId, cirepo.count());
		List<CartItemEntity> customerCart = cirepo.findByCartId(customerId);
		System.out.format("[CartController] found out customer Cart: cartObject[%s] \n", customerCart);
		List<CartItemEntity> response = new ArrayList<CartItemEntity>();
		for(CartItemEntity c: customerCart){
			System.out.format("[CartController] CartItemEntiy: status[%s] \n", c.getCheckOutStatus());
					response.add(c);
		}
		
		//TODO: Retrieve customer's cart from the database
		System.out.format("[CartController] response build: entities[%s], size[%d] \n ", customerCart, customerCart.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
		
		
	
	

	public EventSourcingRepository<CartItem> getRepository() {
		return repository;
	}

	public void setRepository(EventSourcingRepository<CartItem> repository) {
		this.repository = repository;
	}
}

