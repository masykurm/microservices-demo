package com.microservices.order.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
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


import com.microservices.order.api.OrderItemCancelCommand;
import com.microservices.order.api.OrderItemCompleteCommand;
import com.microservices.order.api.OrderItemConfirmCommand;
import com.microservices.order.api.OrderItemCreateCommand;
import com.microservices.order.commandhandling.OrderItem;
import com.microservices.order.commandhandling.OrderItemCommandHandler;
import com.microservices.order.query.OrderItemEntity;
import com.microservices.order.query.OrderUtil;
import com.microservices.order.repo.OrderItemRepository;

@RestController
public class OrderCommandController {
	private CommandBus commandBus;
	private CommandGateway commandGateway;
	private EventStore eventStore;
	
	private EventSourcingRepository<OrderItem> repository;
	private EventBus eventBus;
//	private OrderItemEventHandler orderEventHandler;
	private OrderItemCommandHandler orderCommandHandler;
	
	@Autowired
	private OrderItemRepository orderRepository;
	
	
	public OrderCommandController(){
		commandBus = new SimpleCommandBus();
		setCommandGateway(new DefaultCommandGateway(commandBus));

		eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("./events")));
		eventBus = new SimpleEventBus();
//		orderEventHandler = new OrderItemEventHandler();
		repository = new EventSourcingRepository<>(OrderItem.class, eventStore);
		
		orderCommandHandler = new OrderItemCommandHandler(repository,orderRepository);
		System.out.format("[OrderCommandController] constructor : orderItem repository [%s]", orderRepository);
		
//		orderEventHandler.setRepository(orderRepository);
		
		//Register the eventHandler
//		AnnotationEventListenerAdapter adapter= AnnotationEventListenerAdapter.subscribe(orderEventHandler, eventBus);
//		eventBus.subscribe(adapter);

		setRepository(new EventSourcingRepository<OrderItem>(OrderItem.class, eventStore));
		this.repository.setEventBus(eventBus);
		
		//Register the commandHandler
		AnnotationCommandHandlerAdapter.subscribe(orderCommandHandler, commandBus);
		AggregateAnnotationCommandHandler.subscribe(OrderItem.class, this.repository,commandBus);
	}
	
	/**
	 * Function : Create new Order
	 * Input : customerId, list of Product Id, total Price
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Generate Order ID
	 *   - Send OrderCreateCommand
	 *   - Send response with the Order ID
	 * 
	 * Example: http://localhost:8080/api/order/create?customerId=1&prodId=1&price=12500
	 **/
	//TODO: Develop createNewOrder function here
	@RequestMapping(
			value ="/api/order/create",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> createNewOrder(
			@RequestParam(value="customerId", defaultValue="99") String customerId,
			@RequestParam(value="prodId") String[] productId,
			@RequestParam(value="price", defaultValue="1") String totalPrice){
				
		System.out.println("[OrderCommandController]  Processing new order !!");
		
		String orderId = OrderUtil.generateOrderId(Long.valueOf(customerId));
		orderCommandHandler.getRepositoryService().setRepository(orderRepository);
		commandGateway.send(new OrderItemCreateCommand(Long.valueOf(orderId), Long.valueOf(customerId), Long.valueOf(totalPrice), productId));
		System.out.format("[OrderCommandController] orderItem repository [%s] , size [%d]", orderRepository, orderRepository.count());
		
		String asyncResponseVal = String.format("Processing action OrderItemCreateCommand: customerId[%s], orderId[%s], totalPrice[%s] , productIds[%d], orderId[%s] ", customerId,orderId,totalPrice, productId.length, orderId);
		System.out.println("[OrderCommandController] "+asyncResponseVal);
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Confirm Order
	 * Input : orderId 
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Send OrderItemConfirmCommand
	 *   - Send response with message order still being processed
	 * Example: http://localhost:8080/api/order/confirm?orderId=1&customerId=1
	 **/
	//TODO: Develop confirmOrder function here
	@RequestMapping(
			value ="/api/order/confirm",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> confirmOrder(
			@RequestParam(value="orderId", defaultValue="99") String orderId,
			@RequestParam(value="customerId") String customerId){
				
		System.out.println("[OrderCommandController]  Processing confirmOrder !!");
		
		orderCommandHandler.getRepositoryService().setRepository(orderRepository);
		commandGateway.send(new OrderItemConfirmCommand(Long.valueOf(orderId), Long.valueOf(customerId)));
		System.out.format("[OrderCommandController] orderItem repository [%s] , size [%d]\n", orderRepository, orderRepository.count());
		
		String asyncResponseVal = String.format("Processing action OrderItemConfirmCommand: customerId[%s], orderId[%s]", customerId,orderId);
		System.out.println("[OrderCommandController] "+asyncResponseVal);
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Cancel Order
	 * Input : orderId 
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Send OrderItemCancelCommand
	 *   - Send response with message order still being processed
	 * 
	 **/
	//TODO: Develop cancelOrder function here
	@RequestMapping(
			value ="/api/order/cancel",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> cancelOrder(
			@RequestParam(value="orderId", defaultValue="99") String orderId,
			@RequestParam(value="customerId") String customerId){
				
		System.out.println("[OrderCommandController]  Processing cancelOrder !!");
		
		orderCommandHandler.getRepositoryService().setRepository(orderRepository);
		commandGateway.send(new OrderItemCancelCommand(Long.valueOf(orderId), Long.valueOf(customerId)));
		System.out.format("[OrderCommandController] orderItem repository [%s] , size [%d]", orderRepository, orderRepository.count());
		
		String asyncResponseVal = String.format("Processing action OrderItemCancelCommand: customerId[%s], orderId[%s]", customerId,orderId);
		System.out.println("[OrderCommandController] "+asyncResponseVal);
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Complete Order
	 * Input : orderId 
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Send OrderItemCompleteCommand
	 *   - Send response with message order still being processed
	 * 
	 **/
	//TODO: Develop completeOrder function here
	@RequestMapping(
			value ="/api/order/complete",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> completeOrder(
			@RequestParam(value="orderId", defaultValue="99") String orderId,
			@RequestParam(value="customerId") String customerId){
				
		System.out.println("[OrderCommandController]  Processing cancelOrder !!");
		
		orderCommandHandler.getRepositoryService().setRepository(orderRepository);
		commandGateway.send(new OrderItemCompleteCommand(Long.valueOf(orderId), Long.valueOf(customerId)));
		System.out.format("[OrderCommandController] orderItem repository [%s] , size [%d]", orderRepository, orderRepository.count());
		
		String asyncResponseVal = String.format("Processing action OrderItemCompleteCommand: customerId[%s], orderId[%s]", customerId,orderId);
		System.out.println("[OrderCommandController] "+asyncResponseVal);
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Get Order Status
	 * Input : orderId 
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Call orderRepository.findByOrderId(orderId)
	 *   - Send response with list of order found
	 * 
	 * Example: http://localhost:8080/api/order/status?orderId=1
	 **/
	//TODO: Develop getOrderStatus function here
	@RequestMapping(
			value ="/api/order/status",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getOrderStatus(
			@RequestParam(value="orderId", defaultValue="103") String orderId){
		
		System.out.format("[OrderCommandController] finding out order status: orderId[%s] repoSize[%d]  \n", orderId, orderRepository.count());
		List<OrderItemEntity> customerOrder = orderRepository.findByOrderId(Long.valueOf(orderId));
		System.out.format("[OrderCommandController] found out customer Order: orderObject[%s] \n", customerOrder);
		List<OrderItemEntity> response = new ArrayList<OrderItemEntity>();
		for(OrderItemEntity c: customerOrder){
			System.out.format("[OrderCommandController] OrderItemEntiy: status[%s] \n", c.getOrderId());
			response.add(c);
		}
		
		System.out.format("[CartController] response build: entities[%s], size[%d] \n ", customerOrder, customerOrder.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Get Customer Order (all statuses)
	 * Input : customerId
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - Call orderRepository.findByCustomerId(customerId)
	 *   - Send response with list of order found
	 * 
	 **/
	//TODO: Develop getCustomerOrder function here
	@RequestMapping(
			value ="/api/order/retrieve",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCustomerOrder(
			@RequestParam(value="customerId", defaultValue="103") String customerId){
		
		System.out.format("[OrderCommandController] finding out customer Cart: customerId[%s] , cartId[%s], repoSize[%d]  \n", customerId, customerId, orderRepository.count());
		List<OrderItemEntity> customerOrder = orderRepository.findByCustomerId(Long.valueOf(customerId));
		System.out.format("[OrderCommandController] found out customer Order: orderObject[%s] \n", customerOrder);
		List<OrderItemEntity> response = new ArrayList<OrderItemEntity>();
		for(OrderItemEntity c: customerOrder){
			System.out.format("[OrderCommandController] OrderItemEntiy: status[%s] \n", c.getOrderId());
			response.add(c);
		}
		
		System.out.format("[CartController] response build: entities[%s], size[%d] \n ", customerOrder, customerOrder.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	/**
	 * Function : Clear customer order- this is for internal testing only
	 * Input : customerId
	 * Output: Async. message order still being processed
	 * Process: 
	 *   - 
	 *   - 
	 *   - 
	 * Example: http://localhost:8080/api/order/clearOrder?customerId=1
	 **/
	//TODO:  Clear Order
	@RequestMapping(
			value ="/api/order/clearOrder",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> clearAllOrder(
			@RequestParam(value="customerId", defaultValue="103") String customerId){
		

		orderRepository.deleteAll();
		String asyncResponseVal = String.format("Processing action clear cart: size=[%d] \n",orderRepository.count());
		Response response = new Response(Response.SUCCESS,asyncResponseVal);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	
	public EventSourcingRepository<OrderItem> getRepository() {
		return repository;
	}


	public void setRepository(EventSourcingRepository<OrderItem> repository) {
		this.repository = repository;
	}

	public OrderItemRepository getOrderRepository() {
		return orderRepository;
	}

	public void setOrderRepository(OrderItemRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public CommandGateway getCommandGateway() {
		return commandGateway;
	}

	public void setCommandGateway(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

}
