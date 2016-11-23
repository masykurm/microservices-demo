package com.microservices.cart.repo;

import org.axonframework.eventstore.mongo.DefaultMongoTemplate;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.eventstore.mongo.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Component
public class CartItemEventStore {
	private final MongoTemplate template;

	private MongoEventStore eventStore;
	private final Mongo mongo;
	
	public CartItemEventStore(){
		System.out.println("1");
				
		this.mongo = new MongoClient("localhost", 27017);
		System.out.println("2");
		this.template = new DefaultMongoTemplate(mongo,"mscartservices","cartItemEventCollection","cartItemSnapshotCollection",null,null);
		System.out.println("3");
		try {
			this.eventStore = new MongoEventStore(template);
		}catch(Exception e){
			e.printStackTrace();
			this.eventStore = null;
		}

	}


	public Mongo getMongo() {
		return mongo;
	}



	public MongoTemplate getTemplate() {
		return template;
	}



	public MongoEventStore getEventStore() {
		return eventStore;
	}
}
